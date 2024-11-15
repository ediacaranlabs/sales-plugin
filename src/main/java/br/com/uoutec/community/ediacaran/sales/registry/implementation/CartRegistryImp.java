package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.security.RuntimeSecurityPermission;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.CartRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ExistOrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.lock.NamedLock;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.entity.registry.AbstractRegistry;
import br.com.uoutec.filter.invoker.annotation.EnableFilters;

@Singleton
public class CartRegistryImp 
	extends AbstractRegistry implements CartRegistry{

	public static final String CART_LOCK_GROUP_NAME = "CART";

	public static final String basePermission = "app.registry.sales.cart.";
	
	private boolean enabledCouponSupport;

	private String couponPublicResource;

	private boolean enabledShippingSupport;

	private String shippingPublicResource;

	@Inject
	private OrderRegistry orderRegistry;
	
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Inject
	private NamedLock lock;

	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
	@Inject
	private SubjectProvider subjectProvider;
	
	@Override
	public Cart getNewCart() {
		return new Cart();
	}

	@Override
	public void destroy(Cart cart) {
	}

	public boolean isAvailability(Cart cart) 
			throws ProductTypeHandlerException, ProductTypeRegistryException, SystemUserRegistryException{
		
		SystemUserID userID = getSystemUserID();
		SystemUser sysemUser = getSystemUser(userID);
		
		ItensCollection itens = cart.getItensCollection();
		
		boolean result = true;
		
		for(ProductRequest p: itens.getItens()){
			ProductType productType = productTypeRegistry.getProductType(p.getProduct().getProductType());
			ProductTypeHandler handler = productType.getHandler();
			
			boolean availability = handler.isAvailability(sysemUser, cart, itens, p);
			
			result = result & availability;
			p.setAvailability(availability);
		}
		
		return result;
	}
	
	@EnableFilters(CartRegistry.class)
	public void remove(Cart cart, String serial) throws ProductTypeRegistryException, ProductTypeHandlerException {
		
		ItensCollection itens = cart.getItensCollection();
		ProductRequest item = itens.get(serial);
		
		if(item == null) {
			return;
		}
		
		ProductType productType = productTypeRegistry.getProductType(item.getProduct().getProductType());
		ProductTypeHandler productTypeHandler = productType.getHandler();
		
		productTypeHandler.removeItem(cart, itens, item);
		
	}
	
	@Override
	@EnableFilters(CartRegistry.class)
	public void setQuantity(Cart cart, String serial, 
			int quantity) throws MaxItensException, ProductTypeRegistryException, 
			ProductTypeHandlerException {

		ItensCollection itens = cart.getItensCollection();
		
		ProductRequest item = itens.get(serial);
		
		if(item == null) {
			throw new ProductTypeRegistryException("item not found: " + serial);
		}
		
		ProductType productType = productTypeRegistry.getProductType(item.getProduct().getProductType());
		ProductTypeHandler productTypeHandler = productType.getHandler();
		
		productTypeHandler.updateQty(cart, itens, item, quantity);
		
	}

	@EnableFilters(CartRegistry.class)
	public ProductRequest add(Cart cart, Product product, 
			Map<String, String> addData, int units) throws MaxItensException, 
			ProductTypeRegistryException, ProductTypeHandlerException {

		ProductType productType = productTypeRegistry.getProductType(product.getProductType());
		ProductTypeHandler productTypeHandler = productType.getHandler();
		
		ProductRequest productRequest = new ProductRequest();
		productRequest.setAvailability(true);
		productRequest.setAddData(addData);
		productRequest.setCost(product.getCost());
		productRequest.setCurrency(product.getCurrency());
		productRequest.setUnits(units);
		productRequest.setPeriodType(product.getPeriodType());
		productRequest.setProduct(product);
		productRequest.setName(product.getName());
		productRequest.setMaxExtra(productType.getMaxExtra());
		productRequest.setSerial(productTypeHandler.getSerial(productRequest));
		productRequest.setShortDescription(productTypeHandler.getShortDescription(productRequest));
		productRequest.setDescription(productTypeHandler.getDescription(productRequest));

		productTypeHandler.addItem(cart, cart.getItensCollection(), productRequest);
		
		return productRequest;
	}
	
	@EnableFilters(CartRegistry.class)
	public void calculateTotal(Cart cart){
	}

	@EnableFilters(CartRegistry.class)
	public Checkout checkout(Cart cart, Payment payment, 
			String message) throws OrderRegistryException, PaymentGatewayException, SystemUserRegistryException{
		
		SystemUserID userID = getSystemUserID();
		SystemUser user = getSystemUser(userID);
		
		return safeCheckout(cart, user, payment, message);
	}
	
	@EnableFilters(CartRegistry.class)
	public Checkout checkout(Cart cart, SystemUserID userID, Payment payment, 
			String message) throws
			OrderRegistryException, PaymentGatewayException, SystemUserRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "checkout"));
		
		SystemUser user = getSystemUser(userID);
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return safeCheckout(cart, user, payment, message);
	}

	@EnableFilters(CartRegistry.class)
	public Checkout checkout(Cart cart, SystemUser user, Payment payment, 
			String message) throws OrderRegistryException, PaymentGatewayException{
		
		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "checkout"));
		
		return safeCheckout(cart, user, payment, message);
	}

	private Checkout safeCheckout(Cart cart, SystemUser user, Payment payment, 
			String message) throws OrderRegistryException, PaymentGatewayException{
		
		PaymentGateway paymentGateway = 
				paymentGatewayRegistry.getPaymentGateway(payment.getPaymentType());
		
		if(paymentGateway == null){
			throw new PaymentGatewayException(
					"Gateway de pagamento não configurado: " + 
					payment.getPaymentType());
		}
		
		Order order;
		boolean activeLock = false;
		String lockID = CART_LOCK_GROUP_NAME + cart.getId();
		
		try{
			activeLock = lock.lock(lockID);
			 order = orderRegistry.createOrder(
				cart, user, payment, message, paymentGateway);
		}
		catch(ExistOrderRegistryException e){
			order = orderRegistry.findByCartID(cart.getId());
		}
		finally {
			lock.unlock(lockID, activeLock);			
		}
		
		return new Checkout(order, paymentGateway);
	}

	public boolean isEnabledCouponSupport() {
		return enabledCouponSupport;
	}

	public void setEnabledCouponSupport(boolean enabledCouponSupport) {
		this.enabledCouponSupport = enabledCouponSupport;
	}

	public String getCouponPublicResource() {
		return couponPublicResource;
	}

	public void setCouponPublicResource(String couponPublicResource) {
		this.couponPublicResource = couponPublicResource;
	}

	public boolean isEnabledShippingSupport() {
		return enabledShippingSupport;
	}

	public void setEnabledShippingSupport(boolean enabledShippingSupport) {
		this.enabledShippingSupport = enabledShippingSupport;
	}

	public String getShippingPublicResource() {
		return shippingPublicResource;
	}

	public void setShippingPublicResource(String shippingPublicResource) {
		this.shippingPublicResource = shippingPublicResource;
	}

	@Override
	public void flush() {
	}

	private SystemUser getSystemUser(SystemUserID userID) throws SystemUserRegistryException {
		SystemUser user = systemUserRegistry.getBySystemID(String.valueOf(userID.getSystemID()));
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return user;
	}
	
	private SystemUserID getSystemUserID() throws SystemUserRegistryException {
		Subject subject = subjectProvider.getSubject();
		
		if(!subject.isAuthenticated()) {
			throw new SystemUserRegistryException();
		}
		
		Principal principal = subject.getPrincipal();
		java.security.Principal jaaPrincipal = principal.getUserPrincipal();
		
		return ()->jaaPrincipal.getName();
	}
	
}
