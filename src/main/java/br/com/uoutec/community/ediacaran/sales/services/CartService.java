package br.com.uoutec.community.ediacaran.sales.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

	/*
	@Inject
	private VarParser varParser;
	*/
	
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
	
	public boolean isSupportShipping(Cart cart) throws ProductTypeRegistryException{
		return cartRegistry.isSupportShipping(cart);
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
	
	public void selectShippingOption(String shippingID, Client client, Cart cart) throws CountryRegistryException, ProductTypeRegistryException {
		
		List<ShippingOption> list = getShippingOptions(client, cart.getBillingAddress(), null, cart);
		
		ShippingOption selected = null;
		//Tax shippingTax = null;
		
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
		
		List<Tax> taxList = new ArrayList<>();
		
		for(Tax tax: taxes) {
			if("shipping".equals(tax.getGroup())){
				taxList.add(tax);
			}
		}
		
		taxes.removeAll(taxList);
		
		if(selected != null) {
			
			taxList.clear();
			List<Tax> tmp = shippingToTaxes(selected);
			taxes.addAll(tmp);
			/*
			if(selected instanceof ShippingOptionGroup) {
				ShippingOptionGroup sgo = (ShippingOptionGroup)selected;
				List<ShippingOption> opts = sgo.getOptions();
				
				for(ShippingOption o: opts) {
					Tax shippingTax = new Tax();
					shippingTax.setDescription(o.getMethod());
					shippingTax.setDiscount(false);
					shippingTax.setId(o.getId());
					shippingTax.setGroup("shipping");
					shippingTax.setName(o.getTitle());
					shippingTax.setType(TaxType.UNIT);
					shippingTax.setValue(o.getValue());
					shippingTax.setCurrency(o.getCurrency());
					taxes.add(shippingTax);
				}
			}
			else {
				Tax shippingTax = new Tax();
				shippingTax.setDescription(selected.getTitle());
				shippingTax.setDiscount(false);
				shippingTax.setId(selected.getId());
				shippingTax.setGroup("shipping");
				shippingTax.setName(selected.getTitle());
				shippingTax.setType(TaxType.UNIT);
				shippingTax.setValue(selected.getValue());
				shippingTax.setCurrency(selected.getCurrency());
				taxes.add(shippingTax);
				
			}
			*/
			taxes.addAll(taxList);
		}
		
	}
	
	private List<Tax> shippingToTaxes(ShippingOption shipping){
		List<Tax> list = new ArrayList<>();
		shippingToTaxes(shipping, list);
		return list;
	}

	private void shippingToTaxes(ShippingOption shipping, List<Tax> list){
		if(shipping instanceof ShippingOptionGroup) {
			ShippingOptionGroup sgo = (ShippingOptionGroup)shipping;
			List<ShippingOption> opts = sgo.getOptions();
			
			for(ShippingOption o: opts) {
				shippingToTaxes(o, list);
			}
		}
		else {
			Tax shippingTax = new Tax();
			shippingTax.setDescription(shipping.getTitle());
			shippingTax.setDiscount(false);
			shippingTax.setGroup("shipping");
			shippingTax.setName(shipping.getTitle());
			shippingTax.setType(TaxType.UNIT);
			shippingTax.setValue(shipping.getValue());
			shippingTax.setCurrency(shipping.getCurrency());
			list.add(shippingTax);
		}
	}
	
	public List<ShippingOption> getShippingOptions(Client client, Address dest, String currency, Cart cart) throws CountryRegistryException, ProductTypeRegistryException {
		
		Address origin = ShippingRegistryUtil.getOrigin();
		
		if(dest == null) {
			dest = ShippingRegistryUtil.getAddress(client);
		}
		
		/*
		String serviceShippingName = varParser.getValue("${plugins.ediacaran.sales.electronic_shipping_method}");
		ShippingMethod electronicShippingMethod = shippingMethodRegistry.getShippingMethod(serviceShippingName);
		ShippingRateRequest shippingRateRequest = new ShippingRateRequest(origin, dest, new ArrayList<>(cart.getItens()));
		
		if(electronicShippingMethod.isApplicable(shippingRateRequest)) {
			return electronicShippingMethod.getOptions(shippingRateRequest);
		}
		*/
		
		Map<String, List<ProductRequest>> productTypeGroup = groupProductType(cart.getItens());
		Map<String, List<ShippingOption>> shippingOptionGroup = groupShippingOptions(productTypeGroup, origin, dest);
		List<List<ShippingOption>> groupOptionsList = createShippingOptionsGroup(shippingOptionGroup.entrySet());
		return createShippingOptionsGroup(groupOptionsList);
		
		/*
		String serviceShippingName = varParser.getValue("${plugins.ediacaran.sales.electronic_shipping_method}");
		ShippingMethod electronicShippingMethod = shippingMethodRegistry.getShippingMethod(serviceShippingName);
		ShippingRateRequest shippingRateRequest = new ShippingRateRequest(origin, dest, new ArrayList<>(cart.getItens()));
		
		if(electronicShippingMethod.isApplicable(shippingRateRequest)) {
			return electronicShippingMethod.getOptions(shippingRateRequest);
		}
			
		List<ShippingRateRequest> requests = new ArrayList<>();
		
		for(ProductRequest pr: cart.getItens()) {

			ProductType productType = 
					productTypeRegistry
						.getProductType(pr.getProduct().getProductType());
			
			if(productType.getHandler().isService(pr)) {
				continue;
			}
			
			shippingRateRequest = new ShippingRateRequest(origin, dest, Arrays.asList(pr));
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
		*/
	}
	
	private Map<String, List<ProductRequest>> groupProductType(Collection<ProductRequest> itens){
		Map<String,List<ProductRequest>> map = new HashMap<>();
		for(ProductRequest pr: itens) {
			List<ProductRequest> list = map.get(pr.getProduct().getProductType());
			if(list == null) {
				list = new ArrayList<>();
				map.put(pr.getProduct().getProductType(), list);
			}
			list.add(pr);
		}
		return map;
	}

	private List<List<ShippingOption>> createShippingOptionsGroup(Set<Entry<String, List<ShippingOption>>> entrySet){
		return createShippingOptionsGroup(new LinkedList<>(entrySet));
	}
	
	private List<List<ShippingOption>> createShippingOptionsGroup(LinkedList<Entry<String, List<ShippingOption>>> entryList){

		if(entryList.isEmpty()) {
			return new ArrayList<>();
		}
		
		Entry<String, List<ShippingOption>> entry = entryList.removeFirst();
		List<List<ShippingOption>> result = new ArrayList<>();

		List<List<ShippingOption>> lists = createShippingOptionsGroup(entryList);
		
		for(ShippingOption s: entry.getValue()) {
			
			
			if(lists.isEmpty()) {
				List<ShippingOption> group = new ArrayList<>();
				group.add(s);
				result.add(group);
			}
			else {
				for(List<ShippingOption> parentGroup: lists) {
					List<ShippingOption> group = new ArrayList<>();
					group.add(s);
					group.addAll(parentGroup);
					result.add(group);
				}
			}

		}
		
		return result;
	}

	public List<ShippingOption> createShippingOptionsGroup(List<List<ShippingOption>> list){
		
		List<ShippingOption> result = new ArrayList<>();
		
		for(List<ShippingOption> group: list) {
			
			if(group.size() == 1) {
				result.add(group.get(0));
				continue;
			}
			
			StringBuilder title = new StringBuilder(); 
			StringBuilder id = new StringBuilder(); 
			BigDecimal value = BigDecimal.ZERO;
			
			for(ShippingOption opt: group) {
				if(!title.toString().isEmpty()) {
					title.append(" + ");
					id.append("-");
				}
				id.append(opt.getId());
				title.append(opt.getTitle());
				value = value.add(opt.getValue());
			}
			
			ShippingOption opt = 
					new ShippingOptionGroup(
							id.toString(), 
							"Multiple", 
							title.toString(), 
							group.get(0).getCurrency(), 
							group
					);
			
			result.add(opt);
		}
		
		Collections.sort(result, (a,b)->a.getValue().compareTo(b.getValue()));
		
		return result;
	}

	private Map<String, List<ShippingOption>> groupShippingOptions(Map<String, List<ProductRequest>> productTypeGroup, Address origin, Address dest) {
		
		Map<String, List<ShippingOption>> shippingOptionGroup = new HashMap<>(); 
		
		for(Entry<String,List<ProductRequest>> entry: productTypeGroup.entrySet()) {
			ShippingRateRequest shippingRateRequest = new ShippingRateRequest(origin, dest, entry.getValue());
			
			List<ShippingMethod> shippingMethods = shippingMethodRegistry.getShippingMethods(shippingRateRequest);
			
			for(ShippingMethod sm: shippingMethods) {
				List<ShippingOption> options = sm.getOptions(shippingRateRequest);
				shippingOptionGroup.put(entry.getKey(), options);
			}
			
		}
		
		return shippingOptionGroup;
	}
}
