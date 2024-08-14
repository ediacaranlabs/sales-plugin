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

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.entity.Cart;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.PaymentPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.CartRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.SystemUserEntityTypes;
import br.com.uoutec.community.ediacaran.user.entity.RequestProperties;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.RequestPropertiesPubEntity;
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="/cart", defaultActionName="/")
@Action(value="/products", view=@View("cart/products"))
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class CartPubResource {

	public static final String CART_BEAN_NAME	= "cart";
	
	public static final String CART_BEAN_SCOPE	= ScopeType.SESSION;
	
	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private CountryRegistry countryRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayProvider;

	@Transient
	@Inject
	private OrderRegistry orderRegistry;
	
	@Transient
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Transient
	@Inject
	private ProductRegistry productRegistry;
	
	@Transient
	@Inject
	private SystemUserEntityTypes systemUserEntityTypes;
	
	@Transient
	@Inject
	private CartRegistry cartRegistry;
	
	@Transient
	@Inject
	private VarParser varParser;
	
	public CartPubResource(){
	}
	
	@Action(value="/")
	@View("cart/index")
	@Result("vars")
	@ResponseErrors(rendered=false, name="exception")
	public Map<String, Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		
		try{
			Subject subject = subjectProvider.getSubject();
			
			Map<String,Object> result = new HashMap<String, Object>();
			
			if(subject.isAuthenticated()) {
				List<PaymentGateway> paymentGatewayList = paymentGatewayProvider.getPaymentGateways();
				result.put("payment_gateway_list", paymentGatewayList);
			}
			
			return result;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "index", "load",  ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
	@Action(value="/payment-details")
	@View("cart/payment-details")
	@Result("vars")
	public Map<String, Object> paymentDetails() throws InvalidRequestException{
		
		try{
			Subject subject = subjectProvider.getSubject();
			
			Map<String,Object> result = new HashMap<String, Object>();
			
			if(subject.isAuthenticated()) {
				List<PaymentGateway> paymentGatewayList = paymentGatewayProvider.getPaymentGateways();
				result.put("payment_gateway_list", paymentGatewayList);
			}
			
			return result;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "paymentDetails", "load",  ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
	@Action("/payment-type")
	@RequestMethod(RequestMethodTypes.GET)
	@ResponseErrors(rendered=false, name="exception")
	public ResultAction paymentType(
			@Basic(bean="code")String code,
			@Basic(
					bean=CART_BEAN_NAME, 
					scope=CART_BEAN_SCOPE, mappingType=MappingTypes.VALUE)
			Cart cart) throws InvalidRequestException{
		
		try{
			PaymentGateway pg = paymentGatewayProvider.getPaymentGateway(code);

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
			String error = this.errorMappingProvider.getError(CartPubResource.class, "paymentType", "paymentLoad", ex);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	@Action("/units/{product:[A-Za-z0-9\\-]{1,128}}/{qty:\\d{1,3}}")
	@RequestMethod({RequestMethodTypes.GET, RequestMethodTypes.POST})
	@View("cart/products")
	@ResponseErrors(rendered=false, name="productException")
	public void updateUnits(
			@Basic(
					bean=CART_BEAN_NAME, 
					scope=CART_BEAN_SCOPE, 
					mappingType=MappingTypes.VALUE)
			Cart cart,
			@Basic(bean="qty")
			Integer qty,
			@Basic(bean="product")
			String productIndex) throws InvalidRequestException{
		
		ProductRequest productRequest;

		try{
			productRequest = cart.get(productIndex);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "updateUnits", "loadProduct", ex);
			throw new InvalidRequestException(error, ex);
		}

		try{
			this.cartRegistry.setQuantity(cart, productRequest, qty);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "updateUnits", "updateQuantity", ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
	@Action("/add")
	@RequestMethod(RequestMethodTypes.POST)
	public void add(
			@Basic(
					bean=CART_BEAN_NAME, 
					scope=CART_BEAN_SCOPE, 
					mappingType=MappingTypes.VALUE)
			Cart cart,
			@Basic(bean="product")
			Integer productID,
			@Basic(bean="addData")
			Map<String,String> addData,
			@DetachedName 
			@NotNull 
			RequestPropertiesPubEntity requestPropertiesPubEntity
	) throws InvalidRequestException{
		
		Product product;
		RequestProperties requestProperties;

		try{
			requestProperties = requestPropertiesPubEntity.rebuild(false, true, true);
			addData.put("host", requestProperties.getRemoteAddress());
			product = this.productRegistry.findById(productID);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "add", "loadProductData", ex);
			WebFlowController.redirect()
				.put("productException", new InvalidRequestException(error, ex))
				.to("/cart/");
			return;
		}

		if(product == null){
			Throwable ex = new IllegalStateException("product");
			String error = this.errorMappingProvider.getError(CartPubResource.class, "add", "productNotFound", ex);
			
			WebFlowController.redirect()
			.put("productException", new InvalidRequestException(error))
			.to("/cart/");
			return;
		}
		
		ProductRequest productRequest = new ProductRequest();
		productRequest.setAvailability(true);
		productRequest.setAddData(addData);
		productRequest.setCost(product.getCost());
		productRequest.setCurrency(product.getCurrency());
		productRequest.setUnits(1);
		productRequest.setPeriodType(product.getPeriodType());
		productRequest.setAdditionalCost(product.getAdditionalCost());
		productRequest.setProduct(product);
		
		try{
			this.cartRegistry.add(cart, productRequest);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "add", "addProduct", ex);
			
			WebFlowController.redirect()
			.put("productException", new InvalidRequestException(error, ex))
			.to("/cart/");
			return;
		}

		WebFlowController.redirectTo("/cart/");
	}
	
	@Action("/remove")
	@RequestMethod(RequestMethodTypes.POST)
	public void remove(
			@Basic(
					bean=CART_BEAN_NAME, 
					scope=CART_BEAN_SCOPE, mappingType=MappingTypes.VALUE)
			Cart cart,
			@Pattern(regexp="[A-Za-z0-9\\#\\-]{1,128}")
			@Basic(bean="product")
			String productIndex) throws InvalidRequestException{
		
		ProductRequest productRequest;

		try{
			productRequest = cart.get(productIndex);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "remove", "loadProductData", ex);
			
			WebFlowController.redirect()
			.put("productException", new InvalidRequestException(error, ex))
			.to("/cart/");
			return;
		}

		if(productRequest == null){
			WebFlowController.redirect()
			.to("/cart/");
			return;
		}
		
		try{
			this.cartRegistry.remove(cart, productRequest);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "remove", "removeProduct", ex);
			
			WebFlowController.redirect()
			.put("productException", new InvalidRequestException(error, ex))
			.to("/cart/");
			return;
		}
		
		WebFlowController.redirect()
		.to("/cart/");
	}

	@Action("/checkout")
	@RequestMethod(RequestMethodTypes.POST)
	@View("cart/result_checkout")
	@Result("link")
	@ResponseErrors(rendered=false, name="exception")
	public String checkout(
			@Basic(
					bean=CART_BEAN_NAME, 
					scope=CART_BEAN_SCOPE, mappingType=MappingTypes.VALUE)
			Cart cart,
			@Basic(bean="customer")
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,
			@Basic(bean="payment")
			PaymentPubEntity paymentPubEntity) throws InvalidRequestException{

		if(cart.isNoitems()){
			Throwable ex = new IllegalStateException("cart");
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "emptyCart",  ex);
			throw new InvalidRequestException(error, ex);
		}		

		SystemUser user = null;
		
		try{
			user = authenticatedSystemUserPubEntity.rebuild(true, true, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "loadUserData",  ex);
			throw new InvalidRequestException(error, ex);
		}		
		
		Payment payment;
		
		try{
			payment = paymentPubEntity.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "loadPaymentData",  ex);
			throw new InvalidRequestException(error, ex);
		}		

		String paymentResource = null;
		
		try{
			Checkout checkoutResult = this.cartRegistry.checkout(cart, payment, "Pedido criado via website.");
			paymentResource = checkoutResult.getPaymentGateway().redirectView(user, checkoutResult.getOrder());
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "checkout", "checkout", ex);
			throw new InvalidRequestException(error, ex);
		}
		
		return paymentResource != null? paymentResource : varParser.getValue("${plugins.ediacaran.front.landing_page}");
	}

}