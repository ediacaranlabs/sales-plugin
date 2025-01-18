package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.EmptyOrderException;
import br.com.uoutec.community.ediacaran.sales.registry.ExistOrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.IncompleteClientRegistrationException;
import br.com.uoutec.community.ediacaran.sales.registry.InvalidUnitsOrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.OrderNotFoundRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderStatusNotAllowedRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.UnavailableProductException;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class OrderRegistryUtil {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	public static Order createOrder(Cart cart, Client client, PaymentGateway paymentGateway) {

		Address defaultAddress = getDefaultAddress(client);
		
		Order order = new Order();
		order.setDate(LocalDateTime.now());
		order.setCartID(cart.getId());
		order.setStatus(OrderStatus.NEW);
		order.setId(null);
		order.setOwner(client.getId());
		order.setItens(new ArrayList<ProductRequest>(cart.getItens()));
		order.setTaxes(cart.getTaxes());
		order.setPaymentType(paymentGateway.getId());
		order.setCurrency(order.getItens().get(0).getCurrency());
		order.setBillingAddress(getBillingAddress(cart.getBillingAddress(), defaultAddress));
		order.setShippingAddress(getShippingAddress(cart.getShippingAddress(), order.getBillingAddress(), defaultAddress, cart.getBillingAddress() == cart.getShippingAddress()));

		return order;
		
		/*
		try {
			if(cart.getBillingAddress() != null) {
				clientRegistry.registerAddress(cart.getBillingAddress(), cart.getClient());
			}
			
			if(cart.getShippingAddress() != null) {
				clientRegistry.registerAddress(cart.getShippingAddress(), cart.getClient());
			}
			
		}
		catch(Throwable e) {
			throw new OrderRegistryException("falha ao processar os produtos", e);
		}
		*/
	}
	
	public static void preOrder(Order order, Cart cart, ProductTypeRegistry productTypeRegistry) throws OrderRegistryException {
		try{
			for(ProductRequest pr: order.getItens()){
				ProductType productType = productTypeRegistry.getProductType(pr.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.preRegisterOrder(cart.getClient(), cart, pr);
			}
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao processar os produtos", e);
		}
		
	}
	
	public static void registerNewOrder(Order order, Client client, Payment payment, String message, 
			PaymentGateway paymentGateway, OrderEntityAccess entityAccess) throws OrderRegistryException {
		try{
			order.setPayment(payment);
			
			save(order, entityAccess);
			
			paymentGateway.payment(new PaymentRequest(client, payment));
			
			checkPayment(payment, order);
			order.getPayment().setStatus(payment.getStatus());
			checkAndUpdateNewOrderStatus(order, toOrderStatus(order.getPayment().getStatus()));
			
			update(order, entityAccess);
		}
		catch(OrderRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao registrar o pedido", e);
		}
		
	}

	public static void registerEvent(String message, Order order, OrderRegistry orderRegistry) throws OrderRegistryException {
		orderRegistry.registryLog(order.getId(), message);
	}
	
	public static void registerEvent(String message, Order order, OrderEntityAccess entityAccess) throws OrderRegistryException {
		try{
			entityAccess.registryLog(order, message);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	public static OrderStatus toOrderStatus(PaymentStatus paymentStatus) throws OrderRegistryException {
		
		switch (paymentStatus) {
		case NEW:
		case ON_HOLD:
			return OrderStatus.ON_HOLD;
		case PENDING_PAYMENT:
		case PENDING_PAYMENT_CONFIRMATION:
		case PAYMENT_REVIEW:
			return OrderStatus.PENDING_PAYMENT;
		case PAYMENT_RECEIVED:
			return OrderStatus.PAYMENT_RECEIVED;
		case SUSPECTED_FRAUD:
			return OrderStatus.PENDING_PAYMENT;
		case REFOUND:
			return OrderStatus.REFOUND;
		default:
			throw new OrderRegistryException("invalid payment status: " + paymentStatus);
		}
		
	}
	public static void postOrder(Order order, Cart cart, ProductTypeRegistry productTypeRegistry) throws OrderRegistryException {
		try{
			for(ProductRequest pr: order.getItens()){
				ProductType productType = productTypeRegistry.getProductType(pr.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.postRegisterOrder(cart.getClient(), cart, pr);
			}
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao processar os produtos", e);
		}
		
	}
	
	public static Payment getPayment(Payment payment, Order order, PaymentGateway paymentGateway, Cart cart) {
		payment.setStatus(PaymentStatus.NEW);
		payment.setPaymentType(paymentGateway.getId());
		payment.setTax(cart.getTotalTax());
		payment.setDiscount(cart.getTotalDiscount());
		payment.setCurrency(order.getItens().get(0).getCurrency());
		payment.setValue(cart.getSubtotal());
		payment.setTotal(cart.getTotal());
		
		order.setPayment(payment);
		
		return payment;
	}
	
	public static void checkCurrency(Order order, String currency) throws OrderRegistryException {
		for(ProductRequest pr: order.getItens()){
			
			if(!currency.equals(pr.getCurrency())){
				throw new OrderRegistryException("moeda divergente: " + 
					currency + " <> " + pr.getCurrency());
			}
		}
		
	}
	public static Address getBillingAddress(Address billingAddress, Address defaultAddress) {
		if(billingAddress == null) {
			return defaultAddress;
		}
		else {
			return getAddress(billingAddress);
		}		
	}
	
	public static Address getShippingAddress(Address shippingAddress, Address billingAddress, Address defaultAddress, boolean useBillingAddress) {
		if(shippingAddress == null) {
			return defaultAddress;
		}
		
		return useBillingAddress? billingAddress : getAddress(shippingAddress);
	}
	
	public static void checkCartToRegistry(Cart cart, Payment payment, PaymentGateway paymentGateway, ProductTypeRegistry productTypeRegistry, 
			OrderEntityAccess entityAccess) throws OrderRegistryException, EntityAccessException {
		
		if(cart.getClient() != null) {
			ContextSystemSecurityCheck
				.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRegisterPermission());
		}
		
		if(cart.isNoitems()){
			throw new EmptyOrderException();
		}

		if(cart.getId() == null || cart.getId().isEmpty()){
			throw new OrderRegistryException("empty cart id");
		}
		
		if(payment == null) {
			throw new OrderRegistryException("payment information not found");
		}
		
		if(paymentGateway == null) {
			throw new OrderRegistryException("payment gateway not found");
		}
		
		if(cart.getClient() == null || cart.getClient().getId() <= 0) {
			throw new OrderRegistryException("owner not found");
		}
		
		if(!cart.getClient().isComplete()) {
			throw new IncompleteClientRegistrationException();
		}
		
		Order o = entityAccess.findByCartID(cart.getId());
		
		if(o != null){
			throw new ExistOrderRegistryException();
		}
		
		try{
			if(!isAvailability(cart, productTypeRegistry)){
				throw new UnavailableProductException("Existem produtos que não estão mais disponíveis");
			}
		}
		catch(UnavailableProductException e){
			throw e;
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao verificar a disponibilidades dos produtos", e);
		}		
	}
	
	public static boolean isAvailability(Cart cart, ProductTypeRegistry productTypeRegistry) throws ProductTypeRegistryException, ProductTypeHandlerException {
		ItensCollection itens = cart.getItensCollection();
		
		boolean result = true;
		
		for(ProductRequest p: itens.getItens()){
			ProductType productType = productTypeRegistry.getProductType(p.getProduct().getProductType());
			ProductTypeHandler productTypeHandler = productType.getHandler();
			boolean availability = productTypeHandler.isAvailability(cart.getClient(), cart, itens, p);
			
			result = result & availability;
			p.setAvailability(availability);
		}
		
		return result;
		
	}
	
	public static Client getActualClient(SubjectProvider subjectProvider, SystemUserRegistry systemUserRegistry, 
			ClientRegistry clientRegistry) throws SystemUserRegistryException, ClientRegistryException {
		SystemUserID systemID = getSystemUserID(subjectProvider);
		SystemUser user = getActualUser(systemID, systemUserRegistry);
		return getActualClient(user.getId(), clientRegistry);
	}
	
	public static SystemUserID getSystemUserID(SubjectProvider subjectProvider) throws SystemUserRegistryException {
		Subject subject = subjectProvider.getSubject();
		
		if(!subject.isAuthenticated()) {
			throw new SystemUserRegistryException();
		}
		
		Principal principal = subject.getPrincipal();
		java.security.Principal jaaPrincipal = principal.getUserPrincipal();
		
		return ()->jaaPrincipal.getName();
	}

	public static SystemUser getActualUser(SystemUserID userID, SystemUserRegistry systemUserRegistry) throws SystemUserRegistryException {
		
		SystemUser user = systemUserRegistry.getBySystemID(String.valueOf(userID.getSystemID()));
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return user;
	}
	
	public static Client getActualClient(int id, ClientRegistry clientRegistry) throws ClientRegistryException {
		Client user = clientRegistry.findClientById(id);
		
		if(user == null) {
			throw new ClientRegistryException(String.valueOf(id));
		}
		
		return user;
	}

	public static Address getDefaultAddress(Client client) {
		Address address = new Address();
		
		address.setAddressLine1(client.getAddressLine1());
		address.setAddressLine2(client.getAddressLine2());
		address.setCity(client.getCity());
		address.setCountry(client.getCountry());
		address.setFirstName(client.getFirstName());
		address.setLastName(client.getLastName());
		address.setRegion(client.getRegion());
		address.setZip(client.getZip());
		
		return address;
	}

	public static Address getAddress(Address value) {
		Address address = new Address();
		
		address.setAddressLine1(value.getAddressLine1());
		address.setAddressLine2(value.getAddressLine2());
		address.setCity(value.getCity());
		address.setCountry(value.getCountry());
		address.setFirstName(value.getFirstName());
		address.setLastName(value.getLastName());
		address.setRegion(value.getRegion());
		address.setZip(value.getZip());
		
		return address;
	}

	public static void save(Order entity, OrderEntityAccess entityAccess) 
			throws PaymentGatewayException, EntityAccessException, ValidationException{
		validateOrder(entity, saveValidations);
		entityAccess.save(entity);
		entityAccess.flush();
	}

	public static void update(Order entity, OrderEntityAccess entityAccess) throws EntityAccessException, ValidationException{
		validateOrder(entity, updateValidations);
		entityAccess.update(entity);
		entityAccess.flush();
	}

	public static void validateOrder(Order order, Class<?> ... groups) throws ValidationException{
		ValidatorBean.validate(order, groups);
	}

	public static void checkAndUpdateNewOrderStatus(Order order, OrderStatus newStatus) throws OrderStatusNotAllowedRegistryException {
		checkNewOrderStatus(order, newStatus);
		order.setStatus(newStatus);		
	}

	public static PaymentGateway getPaymentGateway(Order order, PaymentGatewayRegistry paymentGatewayRegistry) throws OrderRegistryException {
		PaymentGateway paymentGateway = paymentGatewayRegistry.getPaymentGateway(order.getPaymentType());
		
		if(paymentGateway == null) {
			throw new OrderRegistryException("payment gateway not found: " + order.getPaymentType());
		}
		
		return paymentGateway;
	}

	public static void markPaymentAsReceived(Payment payment, Order order) throws OrderRegistryException {
		
		checkPayment(payment, order);
		
		if(payment.getStatus() == PaymentStatus.PAYMENT_RECEIVED) {
			checkAndSetNewOrderStatus(order, OrderStatus.PAYMENT_RECEIVED);
			payment.setReceivedFrom(LocalDateTime.now());
		}
		else{
			payment.setReceivedFrom(null);
		}
		
	}
	
	public static void checkAndSetNewOrderStatus(Order order, OrderStatus newStatus) throws OrderStatusNotAllowedRegistryException {
		checkNewOrderStatus(order, newStatus);
		order.setStatus(newStatus);
	}
	
	public static void checkNewOrderStatus(Order order, OrderStatus newStatus) throws OrderStatusNotAllowedRegistryException {
		
		if(!order.getStatus().isValidNextStatus(newStatus)){
			throw new OrderStatusNotAllowedRegistryException(
					"invalid status #" + order.getId() + ": " + 
					order.getStatus() + " -> " + OrderStatus.PAYMENT_RECEIVED);
		}
	}
	
	public static void checkPayment(Payment payment, Order order) throws OrderRegistryException {
		if(!order.getCurrency().equals(payment.getCurrency())) {
			throw new OrderRegistryException(order.getCurrency() + " <> " + payment.getCurrency());
		}

		if(order.getTotal().compareTo(order.getPayment().getTotal()) != 0) {
			throw new OrderRegistryException(order.getTotal() + " <> " + order.getPayment().getTotal());
		}
		
	}
	public static Order getActualOrder(Order order, OrderEntityAccess entityAccess) throws OrderNotFoundRegistryException {
		try{
			return entityAccess.findById(order.getId());
		}
		catch(Throwable e){
			throw new OrderNotFoundRegistryException(order.getId());
		}
	}
	
	public static boolean isCompletedOrder(Order order, ProductTypeRegistry productTypeRegistry) throws ProductTypeRegistryException {
		
		if(order.getCompleteInvoice() == null ) {
			return false;
		}
		
		for(ProductRequest pr: order.getItens()) {

			ProductType productType = productTypeRegistry.getProductType(pr.getProduct().getProductType());
			ProductTypeHandler productTypeHandler = productType.getHandler();
			
			if(productTypeHandler.isSupportShipping(pr)) {
				return false;
			}
			
		}
		
		return true;
	}

	public static void updateStatus(Order order, OrderStatus orderStatus, OrderRegistry orderRegistry) throws OrderRegistryException {
		orderRegistry.updateStatus(order, orderStatus);
	}
	
	public static void markAsCompleteOrder(Order order, Invoice invoice, List<Invoice> invoices, List<Shipping> shipping, OrderRegistry orderRegistry, ProductTypeRegistry productTypeRegistry
			) throws ProductTypeRegistryException, InvoiceRegistryException, InvalidUnitsOrderRegistryException {
		
		List<Invoice> allInvoices = new ArrayList<>(invoices);
		
		if(!allInvoices.contains(invoice)) {
			allInvoices.add(invoice);
		}
		
		
		markAsCompleteOrder(order, allInvoices, shipping, orderRegistry, productTypeRegistry); 
			
	}

	public static void checkPayment(Order order) throws InvoiceRegistryException {
		if(order.getPayment().getReceivedFrom() == null) {
			throw new InvoiceRegistryException("payment has not yet been made");
		}
	}
	
	public static void markAsCompleteOrder(Order order, Invoice invoice, Shipping shipping, List<Invoice> invoices, List<Shipping> shippings, OrderRegistry orderRegistry, ProductTypeRegistry productTypeRegistry
			) throws ProductTypeRegistryException, InvoiceRegistryException, InvalidUnitsOrderRegistryException {

		List<Invoice> allInvoices = new ArrayList<>(invoices);
		
		if(invoice != null && !allInvoices.contains(invoice)) {
			allInvoices.add(invoice);
		}
		
		List<Shipping> allShippings = new ArrayList<>(shippings);
		
		if(shipping != null && !allShippings.contains(shipping)) {
			allShippings.add(shipping);
		}
		
		markAsCompleteOrder(order, allInvoices, allShippings, orderRegistry, productTypeRegistry);
	}
			
	public static void markAsCompleteOrder(Order order, List<Invoice> invoices, List<Shipping> shipping, OrderRegistry orderRegistry, ProductTypeRegistry productTypeRegistry
			) throws ProductTypeRegistryException, InvoiceRegistryException, InvalidUnitsOrderRegistryException {

		if(InvoiceRegistryUtil.isCompletedInvoice(order, invoices) && !ShippingRegistryUtil.isCompletedShipping(order, shipping, productTypeRegistry)) {

			if(isCompletedOrder(order, productTypeRegistry)) {

				try {
					orderRegistry.updateStatus(order, OrderStatus.COMPLETE);
				}
				catch(Throwable ex) {
					throw new InvoiceRegistryException(ex);
				}
				
				LocalDateTime now = LocalDateTime.now();
				if(order.getCompleteInvoice() == null) {
					order.setCompleteInvoice(now);
				}
				
				if(order.getCompleteShipping() == null) {
					order.setCompleteShipping(now);
				}
				
				try {
					orderRegistry.registerOrder(order);
				}
				catch(Throwable ex) {
					throw new InvoiceRegistryException(ex);
				}
				
			}
			
		}
			
	}
	
}
