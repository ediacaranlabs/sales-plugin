package br.com.uoutec.community.ediacaran.sales.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.entity.TaxType;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.CartRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethod;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethodRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingOption;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingOptionGroup;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingRateRequest;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;

@Singleton
public class CartService {

	@Inject
	private PaymentGatewayRegistry paymentGatewayProvider;

	@Inject
	private CartRegistry cartRegistry;
	
	@Inject
	private ProductRegistry productRegistry;
	
	@Inject
	private ClientRegistry clientRegistry;

	@Inject
	private ShippingMethodRegistry shippingMethodRegistry;
	
	public List<Address> getAddresses(Client client) throws ClientRegistryException {
		return clientRegistry.getAddresses(client); 
	}

	public List<PaymentGateway> getPaymentGateways(Cart cart, Client user){
		return paymentGatewayProvider.getPaymentGateways(cart, user);
	}

	public PaymentGateway getPaymentGateway(String value) {
		return paymentGatewayProvider.getPaymentGateway(value);
	}
	
	public void setQuantity(Cart cart, String serial, 
			int quantity) throws MaxItensException, ProductTypeRegistryException, 
			ProductTypeHandlerException{
		cartRegistry.setQuantity(cart, serial, quantity);
	}
	
	public ProductRequest add(Cart cart, Product product, 
			Map<String, String> addData, int units) throws MaxItensException, 
			ProductTypeRegistryException, ProductTypeHandlerException{
		return cartRegistry.add(cart, product, addData, units);
	}
	
	public void remove(Cart cart, String serial) 
			throws ProductTypeRegistryException, ProductTypeHandlerException{
		cartRegistry.remove(cart, serial);
	}
	
	@Transactional
	public Checkout checkout(Cart cart, Payment payment, String message) throws
			OrderRegistryException, PaymentGatewayException, SystemUserRegistryException{
		return cartRegistry.checkout(cart, payment, message);
	}
	
	public ProductSearchResult search(ProductSearch value) throws ProductRegistryException {
		return productRegistry.search(value);
	}
	
	public void selectAddress(Address billingAddress, Address shippingAddress, Cart cart) {
		cart.setBillingAddress(billingAddress);
		cart.setShippingAddress(shippingAddress);
	}
	
	public void selectClient(Client client, Cart cart) {
		cart.setClient(client);
	}
	
	public void selectShippingOption(String shippingID, Client client, Cart cart) throws CountryRegistryException {
		
		List<ShippingOption> list = getShippingOptions(client, cart.getBillingAddress(), null, cart);
		
		ShippingOption selected = null;
		Tax shippingTax = null;
		
		for(ShippingOption so: list) {
			if(shippingID.equals(so.getId())) {
				selected = so;
			}
		}

		List<Tax> taxes = cart.getTaxes();
		
		if(taxes == null) {
			taxes = new ArrayList<>();
			cart.setTaxes(taxes);
		}
		
		for(Tax tax: taxes) {
			if("shipping".equals(tax.getId())){
				shippingTax = tax;
				break;
			}
		}
		
		if(selected != null) {
			
			if(shippingTax == null) {
				shippingTax = new Tax();
				taxes.add(shippingTax);
			}
			
			shippingTax.setDescription(selected.getMethod());
			shippingTax.setDiscount(false);
			shippingTax.setId("shipping");
			shippingTax.setName(selected.toString());
			shippingTax.setType(TaxType.UNIT);
			shippingTax.setValue(selected.getValue());
		}
		else
		if(shippingTax != null){
			taxes.remove(shippingTax);
		}
		
	}
	
	public List<ShippingOption> getShippingOptions(Client client, Address dest, String currency, Cart cart) throws CountryRegistryException {
		
		Address origin = ShippingRegistryUtil.getOrigin();
		
		if(dest == null) {
			dest = ShippingRegistryUtil.getAddress(client);
		}
		
		List<ShippingRateRequest> requests = new ArrayList<>();
		
		for(ProductRequest pr: cart.getItens()) {
			
			Map<String, String> data = pr.getAddData();
			String weightSTR	= data.get(ShippingMethod.WEIGHT_PROPERTY);
			String heightSTR	= data.get(ShippingMethod.HEIGHT_PROPERTY);
			String widthSTR		= data.get(ShippingMethod.WIDTH_PROPERTY);
			String depthSTR		= data.get(ShippingMethod.DEPTH_PROPERTY);

			float weight	= weightSTR == null? 0f : Float.parseFloat(weightSTR);
			float height	= heightSTR == null? 0f : Float.parseFloat(heightSTR);
			float width		= widthSTR == null? 0f : Float.parseFloat(widthSTR);
			float depth 	= depthSTR == null? 0f : Float.parseFloat(depthSTR);
			
			ShippingRateRequest shippingRateRequest = new ShippingRateRequest(origin, dest, weight, height, width, depth, Arrays.asList(pr));
			requests.add(shippingRateRequest);
		}
		
		Map<String, List<ShippingOption>> shippingOptions = new HashMap<>();
		
		for(ShippingRateRequest request: requests) {
			
			List<ShippingMethod> shippingMethods = shippingMethodRegistry.getShippingMethods(request);
			
			for(ShippingMethod sm: shippingMethods) {
				
				List<ShippingOption> options = sm.getOptions(request);
				
				for(ShippingOption opt: options) {
					
					List<ShippingOption> optionsGroup = shippingOptions.get(opt.getId() + "-" + opt.getMethod());
					
					if(optionsGroup == null) {
						optionsGroup = new ArrayList<>();
						shippingOptions.put(opt.getId() + "-" + opt.getMethod(), optionsGroup);
					}
					
					optionsGroup.add(opt);
				}
			}
			
		}
		
		List<ShippingOption> result = new ArrayList<>();
		
		for(Entry<String,List<ShippingOption>> entry: shippingOptions.entrySet()) {
			
			if(entry.getValue().isEmpty()) {
				continue;
			}
			
			ShippingOption first = entry.getValue().get(0);
			
			ShippingOption opt = 
					new ShippingOptionGroup(
							first.getId(), 
							first.getMethod(), 
							first.getTitle(), 
							first.getCurrency(), 
							entry.getValue()
					);
			
			result.add(opt);
		}
		
		return result;
	}
	
}
