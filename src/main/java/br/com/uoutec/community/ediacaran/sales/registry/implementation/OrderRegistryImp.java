package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.security.DoPrivilegedException;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.PersistenceOrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.AbstractRegistry;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.entity.registry.RegistryException;
import br.com.uoutec.filter.invoker.annotation.EnableFilters;
import br.com.uoutec.i18n.ValidationException;

@Singleton
public class OrderRegistryImp
	extends AbstractRegistry
	implements OrderRegistry {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private ActionRegistry actionRegistry;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Inject
	private OrderEntityAccess orderEntityAccess;

	@Inject
	private OrderIndexEntityAccess indexEntityAccess;
	
	@Inject
	private ClientRegistry clientRegistry;
	
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public void registerOrder(Order entity)	throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRegisterPermission());
		
		try{
			if(entity.getId() == null){
				this.registryNewOrder(entity);
			}
			else{
				this.updateOrder(entity);
			}
		}
		catch(ValidationException e){
			throw new OrderRegistryException(e.getMessage());
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	private void registryNewOrder(Order entity) throws Throwable {
		
		PaymentGateway paymentGateway = OrderRegistryUtil.getPaymentGateway(entity, paymentGatewayRegistry);
		
		OrderRegistryUtil.validateOrder(entity, saveValidations);
		OrderRegistryUtil.checkCurrency(entity, entity.getCurrency());
		OrderRegistryUtil.preOrder(entity, productTypeRegistry);
		OrderRegistryUtil.updateStatus(entity, OrderStatus.NEW);
		OrderRegistryUtil.save(entity, orderEntityAccess);
		OrderRegistryUtil.saveOrUpdateIndex(entity, indexEntityAccess);
		OrderRegistryUtil.registerPayment(entity, entity.getClient(), entity.getPayment(), "Predido criado", paymentGateway, orderEntityAccess);
		OrderRegistryUtil.updateStatusByPaymentStatus(entity);
		OrderRegistryUtil.checkAcceptNewOrderStatus(entity, entity.getStatus(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		OrderRegistryUtil.update(entity, orderEntityAccess);
		OrderRegistryUtil.postOrder(entity, productTypeRegistry);
		OrderRegistryUtil.registerEvent("Pedido criado #" + entity.getId(), entity, orderEntityAccess);
		OrderRegistryUtil.registerNewOrderEvent(actionRegistry, entity);
	}
	
	private void updateOrder(Order entity) throws Throwable {
		
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		ShippingRegistry shippingRegistry	    = EntityContextPlugin.getEntity(ShippingRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		InvoiceRegistry invoiceRegistry         = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		
		Order actualOrder                = OrderRegistryUtil.getActualOrder(entity, orderEntityAccess);
		List<Shipping> actualShippings   = ShippingRegistryUtil.getActualShippings(entity, shippingRegistry);
		List<OrderReport> actualReports  = OrderReportRegistryUtil.findByOrder(actualOrder, orderReportRegistry);
		List<Refund> refunds             = InvoiceRegistryUtil.getActualRefunds(actualOrder, refundRegistry);
		List<Invoice> actualInvoices     = InvoiceRegistryUtil.getActualInvoices(actualOrder, null, invoiceRegistry);
		
		OrderRegistryUtil.validateOrder(entity, updateValidations);
		OrderRegistryUtil.checkCurrency(entity, entity.getCurrency());
		OrderRegistryUtil.markAsCompleteOrder(actualOrder, refunds, actualShippings, actualReports, orderEntityAccess, productTypeRegistry);
		OrderRegistryUtil.checkAcceptNewOrderStatus(actualOrder, actualOrder.getStatus(), refunds, actualShippings, actualInvoices, actualReports);
		
		if(!actualOrder.getStatus().isClosed()) {
			OrderRegistryUtil.update(entity, orderEntityAccess);
			OrderRegistryUtil.registerEvent("Pedido alterado #" + entity.getId(), entity, orderEntityAccess);
			OrderRegistryUtil.saveOrUpdateIndex(entity, indexEntityAccess);
		}
	}

	private void deleteOrder(Order entity) throws PersistenceOrderRegistryException, ValidationException {
		if(!entity.isRemoved()){
			entity.setRemoved(true);
			OrderRegistryUtil.update(entity, orderEntityAccess);
			OrderRegistryUtil.removeIndex(entity, indexEntityAccess);
		}
	}
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public void removeOrder(Order entity) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRemovePermission());

		try{
			if(entity.getId() != null){
				deleteOrder(entity);
			}
		}
		catch(ValidationException e){
			throw new OrderRegistryException(e.getMessage());
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
		
	}

	@Override
	@ActivateRequestContext
	public Order findById(String id) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getFindPermission());
		
		try{
			return orderEntityAccess.findById(id);			
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	@Override
	@ActivateRequestContext
	public Order findByCartID(String id)
			throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getFindPermission());
		
		try{
			return orderEntityAccess.findByCartID(id);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	@Override
	@ActivateRequestContext
	public OrderResultSearch searchOrder(OrderSearch value) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getSearchPermission());
		
		try{
			int page = value.getPage() == null? 1 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<Order> itens = indexEntityAccess.searchOrder(value, firstResult, maxResults);
			List<Order> result = new ArrayList<>();
			ClientRegistry clientRegistry = EntityContextPlugin.getEntity(ClientRegistry.class);
			
			for(Order e: itens) {
				e = OrderRegistryUtil.getActualOrder(e, orderEntityAccess);
				e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
				result.add(e);
			}
			
			return new OrderResultSearch(result.size() > maxItens, -1, page, result.size() > maxItens? result.subList(0, maxItens -1) : result);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	@ActivateRequestContext
	public List<Order> getOrders(Integer first, Integer max)
			throws OrderRegistryException, SystemUserRegistryException {
		return getOrders((OrderStatus)null, first, max);
	}
	
	@ActivateRequestContext
	public List<Order> getOrders(OrderStatus status, Integer first, Integer max)
			throws OrderRegistryException, SystemUserRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getListPermission());
		
		try{
			return orderEntityAccess.getOrders(null, status, first, max);
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
		
	}
	
	@ActivateRequestContext
	public List<Order> getOrders(SystemUserID userID, Integer first, Integer max)
			throws OrderRegistryException, SystemUserRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getListPermission());
		
		SystemUser systemUser = getSystemUser(userID);
		
		try{
			return orderEntityAccess.getOrders(systemUser.getId(), first, max);
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
	}

	@ActivateRequestContext
	public List<Order> getOrders(SystemUserID userID, OrderStatus status,
			Integer first, Integer max) throws OrderRegistryException, SystemUserRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getListPermission());
		
		SystemUser systemUser = getSystemUser(userID);
		
		try{
			return orderEntityAccess.getOrders(systemUser.getId(), status, first, max);
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
	}

	public ProductRequest getProductRequestBySerial(String orderID, String serial) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.GET.getProductRequestPermission());
		
		try{
			return orderEntityAccess.getProductRequestBySerial(orderID, serial);
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
	}
	
	@ActivateRequestContext
	public ProductRequest getProductRequest(String orderID,
			String id) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.GET.getProductRequestPermission());
		
		try{
			return orderEntityAccess.getProductRequest(orderID, id);
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
	}

	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void updateStatus(Order o, OrderStatus status) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRegisterPaymentPermission());
		
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		InvoiceRegistry invoiceRegistry         = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		ShippingRegistry shippingRegistry         = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		Order order                  = OrderRegistryUtil.getActualOrder(o, orderEntityAccess);
		List<Refund> refunds         = OrderRegistryUtil.getActualRefunds(order, refundRegistry);
		List<Shipping> shipping      = OrderRegistryUtil.getActualShippings(order, shippingRegistry);
		List<Invoice> invoices       = OrderRegistryUtil.getActualInvoices(order, invoiceRegistry);
		List<OrderReport> reportList = OrderRegistryUtil.getActualReports(order, orderReportRegistry);
		
		//OrderRegistryUtil.checkNewOrderStatus(order, status);
		OrderRegistryUtil.checkAcceptNewOrderStatus(order, status, refunds, shipping, invoices, reportList);
		order.setStatus(status);
	
		try {
			OrderRegistryUtil.update(order, orderEntityAccess);
			OrderRegistryUtil.saveOrUpdateIndex(order, indexEntityAccess);
			o.setStatus(status);
		}
		catch(Throwable ex) {
			throw new OrderRegistryException(ex);
		}
		
	}
	
	/**
	 * Cria um novo pedido.
	 * 
	 * @param cart Carrinho de compras.
	 * @param payment Dados do Pagamento.
	 * @param paymentGateway Processador de pagamentos.
	 * @param message Mensagem registrada no log.
	 * @param user Cliente.
	 * @return Pedido.
	 */
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public Order createOrder(Cart cart, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getCreatePermission());
		
		return unsafeCreateOrder(cart, payment, message, paymentGateway);
	}
	
	private Order unsafeCreateOrder(Cart cart, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException {
		
		try{
			Order order = createOrder0(cart, payment, message, paymentGateway);
			cart.clear();
			return order;
		}
		catch(OrderRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	private Order createOrder0(Cart cart, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException,
			SystemUserRegistryException, ClientRegistryException, ProductTypeRegistryException, ProductTypeHandlerException, PaymentGatewayException, ValidationException {

		OrderRegistryUtil.updateClient(cart.getClient(), clientRegistry);
		OrderRegistryUtil.checkCartToRegistry(cart, payment, paymentGateway, productTypeRegistry, orderEntityAccess);
		OrderRegistryUtil.saveAddressIfNecessary(cart, clientRegistry);

		Order order = OrderRegistryUtil.createOrder(cart, cart.getClient(), payment, paymentGateway);
		registerOrder(order);
		return order;
	}
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public void registerPayment(Order o) throws OrderRegistryException, PaymentGatewayException, ClientRegistryException {
		Order order = OrderRegistryUtil.getActualOrder(o, orderEntityAccess);
		updatePaymentStatus0(order.getPayment(), order, null);
	}

	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public void updatePaymentStatus(Payment payment, Order o, PaymentStatus paymentStatus) throws OrderRegistryException, PaymentGatewayException, ClientRegistryException {
		updatePaymentStatus0(payment, o, paymentStatus);
	}

	private void updatePaymentStatus0(Payment payment, Order o, PaymentStatus paymentStatusParam) throws OrderRegistryException, PaymentGatewayException, ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getUpdatePaymentPermission(payment.getPaymentType().toLowerCase()));
		
		try {
			ContextSystemSecurityCheck.doPrivileged(()->{
				
				PaymentStatus paymentStatus = paymentStatusParam;
				Order order                 = OrderRegistryUtil.getActualOrder(o, orderEntityAccess);
				Payment actualPayment       = OrderRegistryUtil.getActualPayment(payment, order);

				if(paymentStatus == null) {
					
					PaymentGateway paymentGateway = OrderRegistryUtil.getPaymentGateway(order, paymentGatewayRegistry);
					paymentGateway.payment(new PaymentRequest(order));
					paymentStatus = order.getPayment().getStatus();
					
				}
				
				
				OrderRegistryUtil.updatePaymentStatus(actualPayment, order, paymentStatus);
				
				updateOrder(order);
	
				o.setStatus(order.getStatus());
				o.setPayment(order.getPayment());
				
				return null;
			});
			
		}
		catch(DoPrivilegedException ex) {
			Throwable e = ex.getCause();
			
			if(e instanceof OrderRegistryException) {
				throw (OrderRegistryException)e;
			}
			else
			if(e instanceof PaymentGatewayException) {
				throw (PaymentGatewayException)e;
			}
			else
			if(e instanceof ClientRegistryException) {
				throw (ClientRegistryException)e;
			}
			else {
				throw new OrderRegistryException(e);
			}
			
		}
		
	}
	
	public Invoice createInvoice(Order order, Map<String, Integer> itens, String message) throws OrderRegistryException{
		InvoiceRegistry invoiceRegistry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		try {
			return invoiceRegistry.createInvoice(order, itens, message);
		}
		catch(Throwable ex) {
			throw new OrderRegistryException(ex);
		}
	}
/*
	public Shipping createShipping(Order order, Map<String, Integer> itens, String message) throws OrderRegistryException {
		ShippingRegistry shippingRegistry = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		try {
			return shippingRegistry.createShipping(order, itens, message);
		}
		catch(Throwable ex) {
			throw new OrderRegistryException(ex);
		}
	}
*/	
	@ActivateRequestContext
	public boolean isAvailability(Cart cart) 
			throws ProductTypeHandlerException, ProductTypeRegistryException, 
			OrderRegistryException, SystemUserRegistryException{
		return OrderRegistryUtil.isAvailability(cart, productTypeRegistry);
	}
	
	/**
	 * Cria o estorno de um pedido.
	 * @param order
	 * @throws RegistryException 
	 */
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public Refund createRefund(Order order, Map<String, Integer> itens, String message) throws OrderRegistryException {
		
		RefundRegistry refundRegistry  = EntityContextPlugin.getEntity(RefundRegistry.class);
		
		try {
			return refundRegistry.createRefund(order, itens);
		}
		catch(Throwable ex) {
			throw new OrderRegistryException(ex);
		}
	}

	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public Refund createRefund(Order order, String message) throws OrderRegistryException {
		
		RefundRegistry refundRegistry  = EntityContextPlugin.getEntity(RefundRegistry.class);
		
		try {
			return refundRegistry.toRefund(order);
		}
		catch(Throwable ex) {
			throw new OrderRegistryException(ex);
		}
		
	}
	
	/* log  */
	
	@ActivateRequestContext
	public void registryLog(Order order, String message) throws OrderRegistryException{
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.LOGS.getRegisterPermission());
		OrderRegistryUtil.registerEvent(message, order, orderEntityAccess);
	}

	@ActivateRequestContext
	public List<OrderLog> getLogs(String orderID, Integer first, Integer max) throws OrderRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.LOGS.getListPermission());
		
		try{
			Order order = new Order();
			order.setId(orderID);
			return orderEntityAccess.getLogs(order, first, max);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}


	@Override
	public void flush() {
		orderEntityAccess.flush();
	}

	private SystemUser getSystemUser(SystemUserID userID) throws SystemUserRegistryException {
		SystemUser user = systemUserRegistry.getBySystemID(userID);
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return user;
	}

}
