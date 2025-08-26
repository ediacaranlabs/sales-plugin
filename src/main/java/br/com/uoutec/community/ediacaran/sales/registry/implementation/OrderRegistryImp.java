package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
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
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.OrderStatusNotAllowedRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.PersistenceOrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.UnmodifiedOrderStatusRegistryException;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
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
	implements OrderRegistry{

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
	private SubjectProvider subjectProvider;

	@Inject
	private ClientRegistry clientRegistry;
	
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Override
	@Transactional
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

	private void registryNewOrder(Order entity) throws Throwable {
		
		PaymentGateway paymentGateway = OrderRegistryUtil.getPaymentGateway(entity, paymentGatewayRegistry);
		Payment actualPayment         = OrderRegistryUtil.getPayment(new Payment(), entity, paymentGateway);
		
		OrderRegistryUtil.validateOrder(entity, saveValidations);
		OrderRegistryUtil.checkCurrency(entity, entity.getCurrency());
		OrderRegistryUtil.preOrder(entity, productTypeRegistry);
		OrderRegistryUtil.registerNewOrder(entity, entity.getClient(), actualPayment, "Predido criado", paymentGateway, orderEntityAccess);
		OrderRegistryUtil.postOrder(entity, productTypeRegistry);
		OrderRegistryUtil.registerEvent("Pedido criado #" + entity.getId(), entity, orderEntityAccess);
		OrderRegistryUtil.registerNewOrderEvent(actionRegistry, entity);
		OrderRegistryUtil.saveOrUpdateIndex(entity, indexEntityAccess);
	}
	
	private void updateOrder(Order entity) throws Throwable {
		
		InvoiceRegistry invoiceRegistry         = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		ShippingRegistry shippingRegistry	    = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		Order actualOrder                = OrderRegistryUtil.getActualOrder(entity, orderEntityAccess);
		Client actualClient              = OrderRegistryUtil.getActualClient(entity.getClient(), clientRegistry);
		List<Shipping> actualShippings   = ShippingRegistryUtil.getActualShippings(entity, actualClient, shippingRegistry);
		List<Invoice> actualInvoices     = InvoiceRegistryUtil.getActualInvoices(actualOrder, actualClient, invoiceRegistry);
		List<OrderReport> actualReports  = OrderReportRegistryUtil.findByOrder(actualOrder, orderReportRegistry);
		
		OrderRegistryUtil.validateOrder(entity, updateValidations);
		OrderRegistryUtil.checkCurrency(entity, entity.getCurrency());
		
		if(!actualOrder.isClosed()) {
			OrderRegistryUtil.markAsCompleteOrder(actualOrder, actualInvoices, actualShippings, actualReports, orderEntityAccess, productTypeRegistry);
		}
		
		OrderRegistryUtil.update(entity, orderEntityAccess);
		OrderRegistryUtil.registerEvent("Pedido alterado #" + entity.getId(), entity, orderEntityAccess);
		OrderRegistryUtil.saveOrUpdateIndex(entity, indexEntityAccess);
	}

	private void deleteOrder(Order entity) throws PersistenceOrderRegistryException, ValidationException {
		if(!entity.isRemoved()){
			entity.setRemoved(true);
			OrderRegistryUtil.update(entity, orderEntityAccess);
			OrderRegistryUtil.removeIndex(entity, indexEntityAccess);
		}
	}
	
	@Override
	@Transactional
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
		
		return unsafeFindById(id, null);
	}

	@Override
	@ActivateRequestContext
	public Order findById(String id, SystemUserID userID) throws OrderRegistryException {

		if(!SystemUserRegistry.CURRENT_USER.equals(userID)) {
			ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getFindPermission());
		}

		SystemUser systemUser = null;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			systemUser = getSystemUser(userID);
		}
		catch (SystemUserRegistryException e) {
			throw new OrderRegistryException(e);
		}
		
		return unsafeFindById(id, systemUser);
	}

	@Override
	@ActivateRequestContext
	public Order findById(String id, SystemUser systemUser) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, systemUser);
	}

	private Order unsafeFindById(String id, SystemUser systemUser) throws OrderRegistryException {
		
		try{
			Order order = orderEntityAccess.findById(id);
			
			if(order != null) {
				if(systemUser != null && order.getClient().getId() != systemUser.getId()) {
					return null;
				}
			}
			
			order.setClient(order.getClient() == null? null : clientRegistry.findClientById(order.getClient().getId()));					
			
			return order;
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
	@Transactional
	@ActivateRequestContext
	public void updateStatus(Order o, OrderStatus status) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRegisterPaymentPermission());
		
		Order order = OrderRegistryUtil.getActualOrder(o, orderEntityAccess);
		OrderRegistryUtil.checkNewOrderStatus(order, status);
		order.setStatus(status);
		
		switch (status) {
		case NEW:
			order.setCompleteShipping(null);
			order.setCompleteInvoice(null);
			break;
		case ON_HOLD:
			order.setCompleteShipping(null);
			order.setCompleteInvoice(null);
			break;
		case PENDING_PAYMENT:
			order.setCompleteShipping(null);
			order.setCompleteInvoice(null);
			break;
		case PAYMENT_RECEIVED:
			order.setCompleteShipping(null);
			order.setCompleteInvoice(null);
			break;
		case ORDER_INVOICED:
			order.setCompleteInvoice(LocalDateTime.now());
			order.setCompleteShipping(null);
			break;
		case ORDER_SHIPPED:
			order.setCompleteShipping(LocalDateTime.now());
			break;
		case CANCELED:
			break;
		case REFOUND:
			break;
		case COMPLETE:
			break;
		case CLOSED:
			break;
		case ARCHIVED:
			break;
		default:
			break;
		}
	
		try {
			OrderRegistryUtil.update(order, orderEntityAccess);
			OrderRegistryUtil.saveOrUpdateIndex(order, indexEntityAccess);
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
	@Transactional
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

		Order order = OrderRegistryUtil.createOrder(cart, paymentGateway);
		registerOrder(order);
		return order;
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public void registerPayment(Order o) throws OrderRegistryException, PaymentGatewayException, ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRegisterPaymentPermission());
		
		Order order                   = OrderRegistryUtil.getActualOrder(o, orderEntityAccess);
		PaymentGateway paymentGateway = OrderRegistryUtil.getPaymentGateway(order, paymentGatewayRegistry);
		Client actualUser             = OrderRegistryUtil.getActualClient(order.getClient().getId(), clientRegistry);
		
		OrderRegistryUtil.checkNewOrderStatus(order, OrderStatus.PAYMENT_RECEIVED);
		paymentGateway.payment(new PaymentRequest(actualUser, order.getPayment()));
		OrderRegistryUtil.markPaymentAsReceived(order.getPayment(), order);
		
		try {
			updateOrder(order);
			o.setStatus(order.getStatus());
			o.setPayment(order.getPayment());
		}
		catch (Throwable e) {
			throw new OrderRegistryException(e);
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

	public Shipping createShipping(Order order, Map<String, Integer> itens, String message) throws OrderRegistryException {
		ShippingRegistry shippingRegistry = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		try {
			return shippingRegistry.createShipping(order, itens, message);
		}
		catch(Throwable ex) {
			throw new OrderRegistryException(ex);
		}
	}
	
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
	@Transactional
	@ActivateRequestContext
	@EnableFilters(OrderRegistry.class)
	public void createRefound(String orderID, String message) throws RegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRefoundPermission());
		
		try{
			this.unsafeCreateRefound(orderID, message);
		}
		catch(RegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	private void unsafeCreateRefound(String orderID, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException, InvoiceRegistryException{
		
		Order order = findById(orderID);
		
		if(order.getStatus() == OrderStatus.REFOUND){
			throw new UnmodifiedOrderStatusRegistryException("Reembolso já efetuado.");
		}
		
		//Verifica se o status é o adequado
		if(!order.getStatus().isValidNextStatus(OrderStatus.REFOUND)){
			throw new OrderStatusNotAllowedRegistryException(
					"novo status não é permitido: " + 
					order.getStatus() + " -> " + OrderStatus.REFOUND);
		}

		/*
		SystemUser user;
		try{
			user = this.systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException(
				"Falha ao recarregar os dados do cliente" + order.getId(), e);
		}
		*/
		
		//atualiza o status do pedido
		order.setStatus(OrderStatus.REFOUND);
		
		//invoiceRegistry.cancelInvoices(order, message);
		
		/*
		//Processa os itens do pedido
		for(ProductRequest productRequest: order.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.removeItem(user, order, productRequest);
			}
			catch(Throwable e){
				throw new OrderRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId(), e);
			}
		}
		*/
		
		//Registra as alterações do pedido
		this.registerOrder(order);
		
		//Registra o evento no log
		this.registryLog(order, message != null? message : "Reembolso feito.");
		
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void revertRefound(String orderID, String message) throws RegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.REFOUND.getRevertPermission());
		
		try{
			this.unsafeRevertRefound(orderID, message);
		}
		catch(RegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	private void unsafeRevertRefound(String orderID, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException, InvoiceRegistryException{

		throw new UnsupportedOperationException();
		
		//Order order = findById(orderID);
		/*
		if(order.getStatus() == OrderStatus.CANCELED_REFOUND){
			throw new UnmodifiedOrderStatusRegistryException("Reembolso cancelado.");
		}
		
		//Verifica se o status é o adequado
		if(!this.isValid(order, OrderStatus.CANCELED_REFOUND)){
			throw new OrderStatusNotAllowedRegistryException(
					"novo status não é permitido: " + 
					order.getStatus() + " -> " + OrderStatus.CANCELED_REFOUND);
		}
		 */
		
		/*
		SystemUser user;
		
		try{
			user = this.systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException(
				"Falha ao recarregar os dados do cliente" + order.getId(), e);
		}
		*/
		
		//atualiza o status do pedido
		/*
		order.setStatus(OrderStatus.CANCELED_REFOUND);
		
		Invoice invoice = invoiceRegistry.toInvoice(order);
		invoiceRegistry.registerInvoice(invoice);
		*/
		
		/*
		//Processa os itens do pedido
		for(ProductRequest productRequest: order.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.registryItem(user, order, productRequest);
			}
			catch(Throwable e){
				throw new OrderRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId());
			}
		}
		*/
		
		/*
		//Registra as alterações do pedido
		this.registerOrder(order);
		
		//Registra o evento no log
		this.registryLog(order.getId(), message != null? message : "O processo de Reemboldo foi finalizada.");
		*/
	}
	
	/* log  */
	
	@ActivateRequestContext
	public void registryLog(Order order, String message) throws OrderRegistryException{
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.LOGS.getRegisterPermission());
		OrderRegistryUtil.registerEvent(message, order, orderEntityAccess);
	}

	/*
	public void updateLog(OrderLog log) throws OrderRegistryException{
		try{
			orderEntityAccess.updateLog(log);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	public void deleteLog(OrderLog log) throws OrderRegistryException{
		try{
			orderEntityAccess.deleteLog(log);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	*/
	
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
