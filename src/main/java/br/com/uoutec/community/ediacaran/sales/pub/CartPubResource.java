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
import org.brandao.brutos.web.WebFlowController;

import br.com.uoutec.community.ediacaran.front.pub.widget.Widget;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;
import br.com.uoutec.community.ediacaran.sales.pub.entity.PaymentPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.EmptyOrderException;
import br.com.uoutec.community.ediacaran.sales.registry.IncompleteClientRegistrationException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.AuthenticationRequiredException;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.entity.RequestProperties;
import br.com.uoutec.community.ediacaran.user.pub.RequestPropertiesPubEntity;
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="/cart", defaultActionName="/")
@Action(value="/products", view=@View("${plugins.ediacaran.sales.template}/front/cart/products"))
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class CartPubResource {

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
	private ProductTypeRegistry productTypeRegistry;
	
	public CartPubResource(){
	}
	
	@Action(value="/")
	@View("${plugins.ediacaran.sales.template}/front/cart/index")
	@Result("vars")
	@ResponseErrors(rendered=false, name="exception")
	public Map<String, Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		Client user;
		
		try {
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			user = (Client)authenticatedSystemUserPubEntity.rebuild(true, false, false);
			result.put("user", user);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartPubResource.class, "index", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		try {
			if(user != null) {
				List<PaymentGateway> paymentGatewayList = cartService.getPaymentGateways(cart, user);
				result.put("payment_gateway_list", paymentGatewayList);
			}
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartPubResource.class, "index", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		
		return result;
	}
	
	@Action(value="/payment-details")
	@View("${plugins.ediacaran.sales.template}/front/cart/payment-details")
	@Result("vars")
	public Map<String, Object> paymentDetails(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		Client user;
		
		try {
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			user = (Client)authenticatedSystemUserPubEntity.rebuild(true, false, false);
			result.put("user", user);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartPubResource.class, "paymentDetails", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		try{
			if(user != null) {
				List<PaymentGateway> paymentGatewayList = cartService.getPaymentGateways(cart, user);
				result.put("payment_gateway_list", paymentGatewayList);
			}
			
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "paymentDetails", "load", locale,  ex);
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
			String error = this.errorMappingProvider.getError(CartPubResource.class, "paymentType", "paymentLoad", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	@Action("/units/{product:[A-Za-z0-9\\-]{1,128}}/{qty:\\d{1,3}}")
	@RequestMethod({RequestMethodTypes.GET, RequestMethodTypes.POST})
	@View("${plugins.ediacaran.sales.template}/front/cart/products")
	@ResponseErrors(rendered=false, name="productException")
	public void updateUnits(
			@Basic(bean="qty")
			Integer qty,
			@Basic(bean="product")
			String productIndex,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		try{
			cartService.setQuantity(cart, productIndex, qty);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "updateUnits", "updateQuantity", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
	@Action("/add/{protectedID}")
	@RequestMethod({RequestMethodTypes.POST, RequestMethodTypes.GET})
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
			String error = this.errorMappingProvider.getError(CartPubResource.class, "add", "loadProductData", locale, ex);
			WebFlowController.redirect()
				.put("productException", new InvalidRequestException(error, ex))
				.to(varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/"));
			return;
		}

		if(product == null){
			Throwable ex = new IllegalStateException("product");
			String error = this.errorMappingProvider.getError(CartPubResource.class, "add", "productNotFound", locale, ex);
			
			WebFlowController.redirect()
			.put("productException", new InvalidRequestException(error))
			.to(varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/"));
			return;
		}
		
		try{
			cartService.add(cart, product, addData, 1);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "add", "addProduct", locale, ex);
			
			WebFlowController.redirect()
			.put("productException", new InvalidRequestException(error, ex))
			.to(varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/"));
			return;
		}

		WebFlowController.redirectTo(varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/"));
	}
	
	@Action("/remove")
	@RequestMethod(RequestMethodTypes.POST)
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
			String error = this.errorMappingProvider.getError(CartPubResource.class, "remove", "removeProduct", locale, ex);
			
			WebFlowController.redirect()
			.put("productException", new InvalidRequestException(error, ex))
			.to(varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/"));
			return;
		}
		
		WebFlowController.redirect()
		.to(varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/"));
	}

	@Action("/checkout")
	@RequestMethod(RequestMethodTypes.POST)
	@View("${plugins.ediacaran.sales.template}/front/cart/result_checkout")
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
		
		Client user = null;
		
		try{
			user = (Client)authenticatedSystemUserPubEntity.rebuild(true, false, false);
		}
		catch(InvalidRequestException ex){
			
			Throwable cause = ex.getCause();
			
			if(cause instanceof AuthenticationRequiredException) {
				return varParser.getValue("${plugins.ediacaran.front.login_page}?redirectTo=${plugins.ediacaran.sales.web_path}/cart");
			}
			
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "loadUserData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "loadUserData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		/* Payment */
		
		Payment payment;
		
		try{
			payment = paymentPubEntity.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "loadPaymentData", locale,  ex);
			throw new InvalidRequestException(error, ex);
		}		
		
		/* checkout */
		
		try{
			Checkout checkoutResult = cartService.checkout(cart, payment, "Pedido criado via website.");
			String paymentResource = checkoutResult.getPaymentGateway().redirectView(new PaymentRequest(user, cart));
			return paymentResource != null? paymentResource : varParser.getValue("${plugins.ediacaran.front.landing_page}");			
		}
		catch(EmptyOrderException ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "emptyCart", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		catch(IncompleteClientRegistrationException ex){
			return varParser.getValue("${plugins.ediacaran.front.web_path}${plugins.ediacaran.front.panel_context}#!${plugins.ediacaran.front.perfil_page}");
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "checkout", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}

	public String getProductCartView(String code) throws ProductTypeRegistryException {
		try {
			return productTypeRegistry.getProductType(code).getHandler().getProductCartView();
		}
		catch(Throwable e) {
			return null;
		}
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public List<Widget> getWidgets(){
		return null;
	}
	
}
