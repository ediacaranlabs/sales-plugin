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
import org.brandao.brutos.annotation.AcceptRequestType;
import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Actions;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.ResponseType;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.MediaTypes;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.front.pub.widget.Widget;
import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.pub.entity.PaymentPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.EmptyOrderException;
import br.com.uoutec.community.ediacaran.sales.registry.IncompleteClientRegistrationException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.AuthenticationRequiredException;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.SystemUserEntityTypes;
import br.com.uoutec.community.ediacaran.user.entity.RequestProperties;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.entity.SystemUserSearchResult;
import br.com.uoutec.community.ediacaran.user.pub.RequestPropertiesPubEntity;
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.community.ediacaran.user.pub.entity.SystemUserPubEntity;
import br.com.uoutec.community.ediacaran.user.pub.entity.SystemUserSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.user.pub.manager.SystemUserSearchPubEntity;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserSearch;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/cart", defaultActionName="/")
@Actions({
	@Action(value="/products", view=@View("${plugins.ediacaran.sales.template}/admin/cart/products")),
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
	private Cart cart;
	
	@Transient
	@Inject
	private CartService cartService;

	@Transient
	@Inject
	private SystemUserEntityTypes systemUserEntityTypes;

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
		
		SystemUser user;
		
		try {
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			user = authenticatedSystemUserPubEntity.rebuild(true, false, false);
			result.put("user", user);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "index", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		try {
			if(user != null) {
				List<PaymentGateway> paymentGatewayList = cartService.getPaymentGateways(cart, user);
				result.put("payment_gateway_list", paymentGatewayList);
			}
			
			result.put("productTypes", productTypeRegistry.getProductTypes());
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
		
		SystemUser user;
		
		try {
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			user = authenticatedSystemUserPubEntity.rebuild(true, false, false);
			result.put("user", user);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "paymentDetails", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		try{
			if(user != null) {
				List<PaymentGateway> paymentGatewayList = cartService.getPaymentGateways(cart, user);
				result.put("payment_gateway_list", paymentGatewayList);
			}
			
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
			
			if(cart == null){
				throw new IllegalStateException("carrinho não encontrado");
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
			
			ra.add("paymentGateway", pg);
			ra.add("cart",           cart);
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
			product = cart.get(productIndex);
			ProductType productType = productTypeRegistry.getProductType(product.getProduct().getProductType());
			productTypeHandler = productType.getHandler();
			cartService.setQuantity(cart, productIndex, qty);
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
			cartService.add(cart, product, addData, 1);
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
			cartService.remove(cart, productIndex);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "remove", "removeProduct", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action("/checkout")
	@RequestMethod(RequestMethodTypes.POST)
	@View("${plugins.ediacaran.sales.template}/admin/cart/result_checkout")
	@Result("link")
	@ResponseErrors(rendered=false, name="exception")
	public String checkout(
			@Basic(bean="customer")
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,
			@Basic(bean="payment")
			PaymentPubEntity paymentPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{

		if(authenticatedSystemUserPubEntity == null) {
			authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
		}
		
		/* user */
		
		SystemUser user = null;
		
		try{
			user = authenticatedSystemUserPubEntity.rebuild(true, false, false);
		}
		catch(InvalidRequestException ex){
			
			Throwable cause = ex.getCause();
			
			if(cause instanceof AuthenticationRequiredException) {
				return varParser.getValue("${plugins.ediacaran.front.login_page}?redirectTo=${plugins.ediacaran.sales.web_path}/cart");
			}
			
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "checkout", "loadUserData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "checkout", "loadUserData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
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
			Checkout checkoutResult = cartService.checkout(cart, user, payment, "Pedido criado via website.");
			String paymentResource = checkoutResult.getPaymentGateway().redirectView(user, checkoutResult.getOrder());
			return paymentResource != null? paymentResource : varParser.getValue("${plugins.ediacaran.front.landing_page}");			
		}
		catch(EmptyOrderException ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "checkout", "emptyCart", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		catch(IncompleteClientRegistrationException ex){
			return varParser.getValue("${plugins.ediacaran.front.web_path}${plugins.ediacaran.front.panel_context}#!${plugins.ediacaran.front.perfil_page}");
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
	
	@Action("/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	public ProductSearchResultPubEntity searchProduct(
			@DetachedName
			ProductSearchPubEntity productSearch,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		ProductSearch search = null;
		try{
			search = productSearch.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "searchProduct", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		ProductSearchResult result;
		try{
			result = cartService.search(search);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "searchProduct", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		return new ProductSearchResultPubEntity(result);
	}
	
	@Action("/product-form/{protectedID}")
	@RequestMethod(RequestMethodTypes.GET)
	@ResponseErrors(rendered=false, name="exception")
	public ResultAction productForm(
			@DetachedName
			ProductPubEntity productPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{

		Product product;
		ProductTypeHandler productTypeHandler;
		try{
			product = productPubEntity.rebuild(true, false, false);
			
			ProductType productType = productTypeRegistry.getProductType(product.getProductType());
			productTypeHandler = productType.getHandler();
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "productForm", "loadData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		ResultAction ra = new ResultActionImp();
		
		try{
			String view = productTypeHandler.getProductFormView();
			
			if(view != null){
				ra.setView(view, true);
			}
			else{
				ra.setContentType(String.class);
				ra.setContent("");
			}
			
			ra.add("product", product);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "productForm", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		return ra;
		
	}

	@Action("/product-cart/{serial:[A-Za-z0-9\\\\-]{1,128}}")
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
			product = cart.get(serial);
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
	
	@Action({"/user/{protectedID}", "/user"})
	@RequestMethod({"POST", "GET"})
	@Result("vars")
	public ResultAction showUser(
			@DetachedName SystemUserPubEntity systemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		try{
			if(systemUserPubEntity == null) {
				systemUserPubEntity = new SystemUserPubEntity();
			}
			
			Map<String,Object> vars = new HashMap<String, Object>();
			boolean isNew           = systemUserPubEntity.getProtectedID() == null;
			SystemUser systemUser   = systemUserPubEntity.rebuild(!isNew, true, false);
			List<Country> countries = this.countryRegistry.getAll(locale);
			String userDataView     = this.systemUserEntityTypes.getSystemUserManagerEntityView(systemUser);
			
			vars.put("user",           systemUser);
			vars.put("countries",      countries);
			vars.put("subject",        subjectProvider.getSubject());

			ResultAction ra = new ResultActionImp();
			ra.setView(userDataView, true).add("vars", vars);
			return ra;
			
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showUser", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action("/search-users")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	public SystemUserSearchResultPubEntity searchUsers(
			@DetachedName SystemUserSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){
		
		SystemUserSearch search;
		
		try {
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "searchUsers", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		try {
			SystemUserSearchResult result = systemUserRegistry.searchSystemUser(search);
			return new SystemUserSearchResultPubEntity(result);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "searchUsers", "search", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public List<Widget> getWidgets(){
		return null;
	}
	
}
