package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Cart;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
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
import br.com.uoutec.community.ediacaran.system.lock.NamedLock;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.AbstractRegistry;
import br.com.uoutec.filter.invoker.annotation.EnableFilters;

@Singleton
public class CartRegistryImp 
	extends AbstractRegistry implements CartRegistry{

	public static final String CART_LOCK_GROUP_NAME = "CART";
	
	private boolean enabledCouponSupport;

	private String couponPublicResource;

	private boolean enabledShippingSupport;

	private String shippingPublicResource;

	@Inject
	private NamedLock lock;

	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
	@Override
	public Cart getNewCart() {
		return new Cart();
	}

	@Override
	public void destroy(Cart cart) {
	}

	public boolean isAvailability(Cart cart, SystemUser user) 
			throws ProductTypeHandlerException, ProductTypeRegistryException{
		
		ItensCollection itens = cart.getItensCollection();
		
		boolean result = true;
		
		for(ProductRequest p: itens.getItens()){
			ProductTypeHandler handler = 
					productTypeRegistry.getProductTypeHandler(p.getProduct().getProductType());
			
			boolean availability = handler.isAvailability(user, cart, itens, p);
			
			result = result & availability;
			p.setAvailability(availability);
		}
		
		return result;
	}
	
	@EnableFilters(CartRegistry.class)
	public void remove(Cart cart, ProductRequest item) throws ProductTypeRegistryException, ProductTypeHandlerException {
		
		ItensCollection itens = cart.getItensCollection();
		
		ProductTypeHandler productTypeHandler = 
				productTypeRegistry.getProductTypeHandler(
						item.getProduct().getProductType());
		
		productTypeHandler.removeItem(cart, itens, item);
		
	}
	
	@Override
	@EnableFilters(CartRegistry.class)
	public void setQuantity(Cart cart, ProductRequest item, 
			int quantity) throws MaxItensException, ProductTypeRegistryException, 
			ProductTypeHandlerException {

		ItensCollection itens = cart.getItensCollection();
		
		ProductTypeHandler productTypeHandler = 
				productTypeRegistry.getProductTypeHandler(
						item.getProduct().getProductType());
		
		productTypeHandler.updateQty(cart, itens, item, quantity);
		
	}

	@EnableFilters(CartRegistry.class)
	public ProductRequest add(Cart cart, Product product, 
			Map<String, String> addData, int units) throws MaxItensException, 
			ProductTypeRegistryException, ProductTypeHandlerException {
		
		ProductRequest productRequest = new ProductRequest();
		productRequest.setAvailability(true);
		productRequest.setAddData(addData);
		productRequest.setCost(product.getCost());
		productRequest.setCurrency(product.getCurrency());
		productRequest.setUnits(units);
		productRequest.setPeriodType(product.getPeriodType());
		productRequest.setAdditionalCost(product.getAdditionalCost());
		productRequest.setProduct(product);

		this.add(cart, productRequest);
		return productRequest;
	}
	
	@EnableFilters(CartRegistry.class)
	public void add(Cart cart, ProductRequest item) 
			throws MaxItensException, ProductTypeRegistryException, ProductTypeHandlerException{

		ItensCollection itens = cart.getItensCollection();
		
		ProductTypeHandler productTypeHandler = 
				productTypeRegistry.getProductTypeHandler(
						item.getProduct().getProductType());
		
		item.setShortDescription(productTypeHandler.getShortDescription(item));
		item.setDescription(productTypeHandler.getDescription(item));
		productTypeHandler.addItem(cart, itens, item);
	}

	@EnableFilters(CartRegistry.class)
	public void calculateTotal(Cart cart){
	}
	
	@EnableFilters(CartRegistry.class)
	public Checkout checkout(Cart cart, SystemUser user, Payment payment, 
			String message) throws OrderRegistryException, PaymentGatewayException{

		OrderRegistry orderRegistry = 
				EntityContextPlugin.getEntity(OrderRegistry.class);
		
		PaymentGatewayRegistry paymentGatewayRegistry = 
				EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		
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
				cart, payment, message, paymentGateway);
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

	
}
