package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.ResultAction;
import org.brandao.brutos.ResultActionImp;
import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Actions;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.front.pub.widget.Widget;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.ClientEntityTypes;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.pub.entity.AddressPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.PaymentPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.EmptyOrderException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.entity.RequestProperties;
import br.com.uoutec.community.ediacaran.user.pub.RequestPropertiesPubEntity;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/cart", defaultActionName="/")
@Actions({
	@Action(value="/widgets", view=@View("${plugins.ediacaran.sales.template}/admin/cart/widgets"))
})
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class CartAdminPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private VarParser varParser;
	
	@Transient
	@Inject
	private AdminCart adminCart;
	
	@Transient
	@Inject
	private CartService cartService;

	@Transient
	@Inject
	private ClientEntityTypes clientEntityTypes;

	@Transient
	@Inject
	private CountryRegistry countryRegistry;

	@Transient
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Transient
	@Inject
	private ProductTypeRegistry productTypeRegistry;

	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	public CartAdminPubResource(){
	}
	
	@Action(value="/")
	@View("${plugins.ediacaran.sales.template}/admin/cart/index")
	@Result("vars")
	@ResponseErrors(rendered=false, name="exception")
	public Map<String, Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		try {
			adminCart.setCart(new Cart());
			adminCart.setClient(new Client());
			result.put("client",					adminCart.getClient());
			result.put("payment_gateway_list",		cartService.getPaymentGateways(adminCart.getCart(), adminCart.getClient()));
			result.put("productTypes",				productTypeRegistry.getProductTypes());
			result.put("client_data_view",			clientEntityTypes.getClientEntityView(adminCart.getClient()));
			//result.put("user_data_view_updater",	varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/user"));
			result.put("countries",					countryRegistry.getAll(locale));
			result.put("principal",					subjectProvider.getSubject().getPrincipal());
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "index", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		
		return result;
	}
	
	@Action(value="/payment-details")
	@View("${plugins.ediacaran.sales.template}/admin/cart/payment-details")
	@Result("vars")
	public Map<String, Object> paymentDetails(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		try{
			List<PaymentGateway> paymentGatewayList = cartService.getPaymentGateways(adminCart.getCart(), adminCart.getClient());
			
			result.put("user", 					adminCart.getClient());
			result.put("payment_gateway_list",	paymentGatewayList);
			result.put("principal",				subjectProvider.getSubject().getPrincipal());
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "paymentDetails", "load", locale,  ex);
			throw new InvalidRequestException(error, ex);
		}

		return result;
		
	}
	
	@Action("/payment-type/{code}")
	@RequestMethod(RequestMethodTypes.GET)
	@ResponseErrors(rendered=false, name="exception")
	public ResultAction paymentType(
			@Basic(bean="code")String code,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		try{
			PaymentGateway pg = cartService.getPaymentGateway(code);

			if(pg == null){
				throw new IllegalStateException("não foi encontrado o gateway de pagamento: " + code);
			}
			
			String view = pg.getView();
			
			ResultAction ra = new ResultActionImp();
			
			if(view != null){
				ra.setView(view, true);
			}
			else{
				ra.setContentType(String.class);
				ra.setContent("");
			}
			
			ra.add("paymentGateway",	pg);
			ra.add("cart",				adminCart.getClient());
			return ra;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "paymentType", "paymentLoad", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	@Action("/units/{product:[A-Za-z0-9\\-]{1,128}}/{qty:\\d{1,3}}")
	@RequestMethod({RequestMethodTypes.GET, RequestMethodTypes.POST})
	@ResponseErrors(rendered=false, name="productException")
	public ResultAction updateUnits(
			@Basic(bean="qty")
			Integer qty,
			@Basic(bean="product")
			String productIndex,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		ProductRequest product = null;
		ProductTypeHandler productTypeHandler = null;
		Throwable exception = null;
		try{
			product = adminCart.getCart().get(productIndex);
			ProductType productType = productTypeRegistry.getProductType(product.getProduct().getProductType());
			productTypeHandler = productType.getHandler();
			cartService.setQuantity(adminCart.getCart(), productIndex, qty);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "updateUnits", "updateQuantity", locale, ex);
			exception = new InvalidRequestException(error, ex);
		}
		
		ResultAction ra = new ResultActionImp();
		
		try{
			String view = productTypeHandler == null? null : productTypeHandler.getProductOrderView();
			
			if(view != null){
				ra.setView(view, true);
			}
			else{
				ra.setContentType(String.class);
				ra.setContent("");
			}
			
			ra.add("productRequest", product);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "updateUnits", "view", locale, ex);
			exception = new InvalidRequestException(error, ex);
		}

		ra.add("exception", exception);
		
		return ra;
		
	}
	
	@Action("/add/{protectedID}")
	@RequestMethod({RequestMethodTypes.POST, RequestMethodTypes.GET})
	@View("${plugins.ediacaran.sales.template}/admin/cart/cart_result")
	public void add(
			@DetachedName 
			@NotNull 
			ProductPubEntity productPubEntity,
			@Basic(bean="addData")
			Map<String,String> addPubData,
			@DetachedName 
			@NotNull 
			RequestPropertiesPubEntity requestPropertiesPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Product product;
		RequestProperties requestProperties;
		Map<String,String> addData;

		try{
			addData = addPubData == null? new HashMap<>() : addPubData;
			requestProperties = requestPropertiesPubEntity.rebuild(false, true, true);
			addData.put("host", requestProperties.getRemoteAddress());
			product = productPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "add", "loadProductData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		if(product == null){
			Throwable ex = new IllegalStateException("product");
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "add", "productNotFound", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		try{
			cartService.add(adminCart.getCart(), product, addData, 1);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "add", "addProduct", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

	}
	
	@Action("/remove")
	@RequestMethod(RequestMethodTypes.POST)
	@View("${plugins.ediacaran.sales.template}/admin/cart/cart_result")
	public void remove(
			@Pattern(regexp="[A-Za-z0-9\\#\\-]{1,128}")
			@Basic(bean="product")
			String productIndex,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		try{
			cartService.remove(adminCart.getCart(), productIndex);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "remove", "removeProduct", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action("/checkout")
	@RequestMethod(RequestMethodTypes.POST)
	@View("${plugins.ediacaran.sales.template}/admin/cart/result_checkout")
	@Result(value="checkout", mappingType = MappingTypes.VALUE)
	@ResponseErrors(rendered=false, name="exception")
	public Checkout checkout(
			@Basic(bean="payment")
			PaymentPubEntity paymentPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		/* Payment */
		
		Payment payment;
		
		try{
			payment = paymentPubEntity.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "checkout", "loadPaymentData", locale,  ex);
			throw new InvalidRequestException(error, ex);
		}		
		
		/* checkout */
		
		try{
			return cartService.checkout(adminCart.getCart(), payment, "Pedido criado via website.");
		}
		catch(EmptyOrderException ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "checkout", "emptyCart", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "checkout", "checkout", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}

	public String getProductCartView(String code) throws ProductTypeRegistryException {
		try {
			return productTypeRegistry.getProductType(code).getHandler().getProductOrderView();
		}
		catch(Throwable e) {
			return null;
		}
	}

	@Action("/product/{serial:[A-Za-z0-9\\\\-]{1,128}}")
	@RequestMethod(RequestMethodTypes.GET)
	@ResponseErrors(rendered=false, name="exception")
	public ResultAction productCart(
			@Basic(bean = "serial")
			String serial,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{

		ProductRequest product;
		ProductTypeHandler productTypeHandler;
		try{
			product = adminCart.getCart().get(serial);
			ProductType productType = productTypeRegistry.getProductType(product.getProduct().getProductType());
			productTypeHandler = productType.getHandler();
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "productForm", "loadData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		ResultAction ra = new ResultActionImp();
		
		try{
			String view = productTypeHandler.getProductOrderView();
			
			if(view != null){
				ra.setView(view, true);
			}
			else{
				ra.setContentType(String.class);
				ra.setContent("");
			}
			
			ra.add("productRequest", product);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "productForm", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		return ra;
		
	}

	@Action("/select/client")
	@RequestMethod(RequestMethodTypes.POST)
	@View("${plugins.ediacaran.sales.template}/admin/cart/select_user_result")
	@Result("vars")
	public Map<String,Object> selectUser(
			@Basic(bean="client")
			ClientPubEntity systemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Map<String,Object> vars = new HashMap<String, Object>();
		try{
			Client client = 
					(Client)systemUserPubEntity.rebuild(systemUserPubEntity.getProtectedID() != null, true, true);
			
			adminCart.setClient(client);
			
			vars.put("client", client);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showUser", "view", locale, ex);
			vars.put("exception", new InvalidRequestException(error, ex));
		}
		
		return vars;
	}

	@Action("/address/select")
	@View("${plugins.ediacaran.sales.template}/admin/cart/select_address_result")
	@RequestMethod(RequestMethodTypes.POST)
	@Result("vars")
	public void selectAddress(
			@Basic(bean = "client.selectedBillingAddress")
			AddressPubEntity selectedBillingAddress, 
			@Basic(bean = "billingAddress")
			AddressPubEntity billingAddressPubEntity,
			@Basic(bean = "client.selectedShippingAddress")
			AddressPubEntity selectedShippingAddress, 
			@Basic(bean = "shippingAddress")
			AddressPubEntity shippingAddressPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Address shippingAddress = null;
		Address billingAddress = null;
		
		try {
			if(selectedBillingAddress != null) {
				billingAddress =
						"new".equals(selectedBillingAddress.getProtectedID())?
							billingAddressPubEntity.rebuild(false, true, true) :
							selectedBillingAddress.rebuild(true, false, false);	
			}

			if(selectedShippingAddress != null) {
				if("new".equals(selectedShippingAddress.getProtectedID())) {
					shippingAddress = shippingAddressPubEntity.rebuild(false, true, true);
				}
				else
				if("billing".equals(selectedShippingAddress.getProtectedID())) {
					shippingAddress = billingAddress;
				}
				else {
					shippingAddress = selectedShippingAddress.rebuild(true, false, false);
				}
			}
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "selectAddress", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		try{
			adminCart.setBillingAddress(billingAddress);
			adminCart.setShippingAddress(shippingAddress);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showAddress", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
	public Cart getCart() {
		return adminCart.getCart();
	}
	
	public List<Widget> getWidgets(){
		return null;
	}
	
}
