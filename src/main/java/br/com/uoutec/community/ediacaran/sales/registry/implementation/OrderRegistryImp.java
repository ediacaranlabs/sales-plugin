package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.EmptyOrderException;
import br.com.uoutec.community.ediacaran.sales.registry.ExistOrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.IncompleteClientRegistrationException;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderNotFoundRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderStatusNotAllowedRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.UnavailableProductException;
import br.com.uoutec.community.ediacaran.sales.registry.UnmodifiedOrderStatusRegistryException;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.event.EventRegistry;
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
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
@javax.enterprise.inject.Default
public class OrderRegistryImp
	extends AbstractRegistry
	implements OrderRegistry{

	private static final Map<OrderStatus, Set<OrderStatus>> nextState;
	
	private static final String ORDER_EVENT_GROUP = "ORDER";
	
	static{
		nextState = new HashMap<OrderStatus, Set<OrderStatus>>();
		//Fluxo comum
		nextState.put(OrderStatus.ON_HOLD, 
			new HashSet<OrderStatus>(Arrays.asList(
					OrderStatus.CLOSED,
					OrderStatus.CANCELED,
					OrderStatus.PENDING_PAYMENT,
					OrderStatus.PAYMENT_RECEIVED))
		);
		
		nextState.put(OrderStatus.PENDING_PAYMENT, 
				new HashSet<OrderStatus>(Arrays.asList(
						OrderStatus.CLOSED,
						OrderStatus.CANCELED,
						OrderStatus.PAYMENT_RECEIVED))
			);

		nextState.put(OrderStatus.PAYMENT_RECEIVED, 
				new HashSet<OrderStatus>(Arrays.asList(
						OrderStatus.CLOSED,
						OrderStatus.REFOUND,
						OrderStatus.ORDER_SHIPPED))
			); // criar fluxo para refound e order shipped

		//Fluxo comum + order shipped
		nextState.put(OrderStatus.ORDER_SHIPPED, 
				new HashSet<OrderStatus>(Arrays.asList(
						OrderStatus.CLOSED,
						OrderStatus.REFOUND))
			);

		nextState.put(OrderStatus.REFOUND, 
				new HashSet<OrderStatus>(Arrays.asList(
						OrderStatus.CLOSED,
						OrderStatus.CANCELED_REFOUND))
			);
		
		nextState.put(OrderStatus.CANCELED_REFOUND, 
				new HashSet<OrderStatus>(Arrays.asList(
						OrderStatus.CLOSED))
			);

		nextState.put(OrderStatus.CLOSED, 
				new HashSet<OrderStatus>(Arrays.asList(
						OrderStatus.REFOUND))
			);
		
	}

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Inject
	private EventRegistry throwSystemEventRegistry;
	
	@Inject
	private OrderEntityAccess orderEntityAccess;

	@Inject
	private InvoiceRegistry invoiceRegistry;
	
	@Inject
	private SubjectProvider subjectProvider;

	
	@Override
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

	private void registryNewOrder(Order entity) 
					throws PaymentGatewayException, EntityAccessException, ValidationException{
		validateOrder(entity, saveValidations);
		orderEntityAccess.save(entity);
		orderEntityAccess.flush();
	}
	
	private void updateOrder(Order entity) throws EntityAccessException, ValidationException{
		validateOrder(entity, updateValidations);
		orderEntityAccess.update(entity);
		orderEntityAccess.flush();
	}
	
	@Override
	public void removeOrder(Order entity) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRemovePermission());
		
		try{
			if(!entity.isRemoved()){
				entity.setRemoved(true);
				this.updateOrder(entity);
			}
		}
		catch(Throwable e){
			e.printStackTrace();
			throw new OrderRegistryException(e);
		}
		
	}

	@Override
	public Order findById(String id) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, null);
	}

	@Override
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
	public Order findById(String id, SystemUser systemUser) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, systemUser);
	}

	private Order unsafeFindById(String id, SystemUser systemUser) throws OrderRegistryException {
		
		try{
			Order order = orderEntityAccess.findById(id);
			
			if(order != null) {
				if(systemUser != null && order.getOwner() != systemUser.getId()) {
					return null;
				}
			}
			return order;
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	@Override
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
	
	public List<OrderResultSearch> searchOrder(OrderSearch value, Integer first, Integer max) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getSearchPermission());
		
		try{
			return orderEntityAccess.searchOrder(value, first, max);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
	}
	
	public List<Order> getOrders(Integer first, Integer max)
			throws OrderRegistryException, SystemUserRegistryException {
		return getOrders((OrderStatus)null, first, max);
	}
	
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
	public void registerPayment(Order o, String currency, BigDecimal value) throws OrderRegistryException, PaymentGatewayException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRegisterPaymentPermission());
		
		Order order;
		try{
			order = orderEntityAccess.findById(o.getId());
		}
		catch(Throwable e){
			throw new OrderNotFoundRegistryException(o.getId());
		}

		if(order.getPayment().getReceivedFrom() != null) {
			return;
		}
		
		//Verifica se o status é o adequado
		if(!this.isValid(order, OrderStatus.PAYMENT_RECEIVED)){
			throw new OrderStatusNotAllowedRegistryException(
					"invalid status: " + 
					order.getStatus() + " -> " + OrderStatus.PAYMENT_RECEIVED);
		}
		
		if(value == null) {
			
			PaymentGatewayRegistry paymentGatewayRegistry = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
			
			PaymentGateway paymentGateway = paymentGatewayRegistry.getPaymentGateway(order.getPaymentType());
			
			if(paymentGateway == null) {
				throw new OrderRegistryException("payment gateway not found");
			}
			
			SystemUser user;
			
			try {
				user = getSystemUser(order.getOwner());
			}
			catch (SystemUserRegistryException e) {
				throw new OrderRegistryException("owner not found: " + order.getOwner());
			}
			
			paymentGateway.payment(user, order, order.getPayment());
			
		}
		else {
			
			if(!order.getPayment().getCurrency().equals(currency)) {
				throw new OrderRegistryException(currency + " <> " + order.getPayment().getCurrency());
			}

			if(order.getPayment().getTotal().compareTo(value) != 0) {
				throw new OrderRegistryException(order.getPayment().getTotal() + " <> " + value);
			}
			
			
		}

		order.setStatus(OrderStatus.PAYMENT_RECEIVED);
		order.getPayment().setReceivedFrom(LocalDateTime.now());
		this.registerOrder(order);
		
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
	public Order createOrder(Cart cart, Payment payment, 
			String message, PaymentGateway paymentGateway) 
					throws OrderRegistryException, UnavailableProductException, SystemUserRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getCreatePermission());
		
		SystemUserID userID = getSystemUserID();
		SystemUser user = getSystemUser(userID);
		
		return unsafeCreateOrder(cart, user, payment, message, paymentGateway);
	}
	
	public Order createOrder(Cart cart, SystemUserID userID, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException, SystemUserRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getCreatePermission());
		
		SystemUser user = getSystemUser(userID);
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return unsafeCreateOrder(cart, user, payment, message, paymentGateway);
	}
		

	public Order createOrder(Cart cart, SystemUser systemUser, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getCreatePermission());
		
		return unsafeCreateOrder(cart, systemUser, payment, message, paymentGateway);
	}
	
	private Order unsafeCreateOrder(Cart cart, SystemUser systemUser, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException {
		
		try{
			Order order = createOrder0(cart, systemUser, payment, message, paymentGateway);
			cart.clear();
			return order;
		}
		catch(RegistryException e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar o pedido", e);
			throw e;
		}
		catch(Throwable e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar o pedido", e);
			throw new OrderRegistryException(e);
		}
	}
	
	private Order createOrder0(Cart cart, SystemUser systemUser, Payment payment, 
			String message, PaymentGateway paymentGateway) 
					throws OrderRegistryException, UnavailableProductException, 
					ExistOrderRegistryException, EmptyOrderException {

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
		
		if(systemUser == null || systemUser.getId() <= 0) {
			throw new OrderRegistryException("owner not found");
		}
		
		if(!systemUser.isComplete()) {
			throw new IncompleteClientRegistrationException();
		}
		/*
		if(cart.getOwner() == null) {
			throw new OrderRegistryException("owner not found");
		}

		if(!cart.getOwner().equals(systemUser.getSystemID())) {
			throw new OrderRegistryException("invalid owner: " + cart.getOwner() + " <> " + systemUser.getSystemID());
		}
		*/
		
		Order o = this.findByCartID(cart.getId());
		
		if(o != null){
			throw new ExistOrderRegistryException();
		}
		
		try{
			if(!isAvailability(cart, systemUser)){
				throw new UnavailableProductException("Existem produtos que não estão mais disponíveis");
			}
		}
		catch(UnavailableProductException e){
			throw e;
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao verificar a disponibilidades dos produtos", e);
		}
		
		Order order = new Order();
		order.setDate(LocalDateTime.now());
		order.setCartID(cart.getId());
		order.setStatus(OrderStatus.ON_HOLD);
		order.setId(null);
		order.setOwner(systemUser.getId());
		order.setInvoice(null);
		order.setItens(new ArrayList<ProductRequest>(cart.getItens()));
		order.setTaxes(cart.getTaxes());
		order.setPaymentType(paymentGateway.getId());
		order.setCurrency(order.getItens().get(0).getCurrency());
		
		/*
		 Validar moeda
		for(ProductRequest pr: order.getItens()){
			subTotal = subTotal.add(pr.getSubtotal());
			
			if(!currency.equals(pr.getCurrency())){
				throw new OrderRegistryException("moeda divergente: " + 
					currency + " <> " + pr.getCurrency());
			}
		}
		*/
		

		payment.setPaymentType(paymentGateway.getId());
		payment.setTax(cart.getTotalTax());
		payment.setDiscount(cart.getTotalDiscount());
		payment.setCurrency(order.getItens().get(0).getCurrency());
		payment.setValue(cart.getSubtotal());
		payment.setTotal(cart.getTotal());
		order.setPayment(payment);

		
		try{
			for(ProductRequest pr: order.getItens()){
				ProductType productType = productTypeRegistry.getProductType(pr.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.preRegisterOrder(systemUser, cart, pr);
			}
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao processar os produtos", e);
		}

		try{
			this.registerOrder(order);
			this.registryLog(order.getId(), message == null? "Pedido criado #" + order.getId() : message);
			//paymentGateway.payment(systemUser, order, order.getPayment());
			//this.registerOrder(order);
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao registrar o pedido", e);
		}

		if(payment.getTotal().compareTo(BigDecimal.ZERO) <= 0){
			
			order.setStatus(OrderStatus.PAYMENT_RECEIVED);
			this.registerOrder(order);
			
			Map<String, Integer> itens = 
					order.getItens().stream()
					.collect(Collectors.toMap(ProductRequest::getSerial, ProductRequest::getUnits));

			try{
				invoiceRegistry.createInvoice(order, itens, null);
			}
			catch(Throwable e){
				throw new OrderRegistryException("falha ao processar os produtos", e);
			}
		}
		
		try{
			for(ProductRequest pr: order.getItens()){
				ProductType productType = productTypeRegistry.getProductType(pr.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.postRegisterOrder(systemUser, cart, pr);
			}
		}
		catch(Throwable e){
			throw new OrderRegistryException("falha ao processar os produtos", e);
		}
		
		return order;
	}
	
	public boolean isAvailability(Cart cart, SystemUserID userID) 
			throws ProductTypeHandlerException, ProductTypeRegistryException, 
			OrderRegistryException, SystemUserRegistryException{
		
		SystemUser user = getSystemUser(userID);
		
		ItensCollection itens = cart.getItensCollection();
		
		boolean result = true;
		
		for(ProductRequest p: itens.getItens()){
			ProductType productType = productTypeRegistry.getProductType(p.getProduct().getProductType());
			ProductTypeHandler productTypeHandler = productType.getHandler();
			boolean availability = productTypeHandler.isAvailability(user, cart, itens, p);
			
			result = result & availability;
			p.setAvailability(availability);
		}
		
		return result;
	}
	
	/**
	 * Cria o estorno de um pedido.
	 * @param order
	 * @throws OrderRegistryException
	 */
	@Override
	public void createRefound(String orderID, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getRefoundPermission());
		
		try{
			this.unsafeCreateRefound(orderID, message);
		}
		catch(RegistryException e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao fazer o reembolso", e);
			throw e;
		}
		catch(Throwable e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao fazer o reembolso", e);
			throw new OrderRegistryException(e);
		}
	}
	
	private void unsafeCreateRefound(String orderID, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException{
		
		Order order = findById(orderID);
		
		if(order.getStatus() == OrderStatus.REFOUND){
			throw new UnmodifiedOrderStatusRegistryException("Reembolso já efetuado.");
		}
		
		//Verifica se o status é o adequado
		if(!this.isValid(order, OrderStatus.REFOUND)){
			throw new OrderStatusNotAllowedRegistryException(
					"novo status não é permitido: " + 
					order.getStatus() + " -> " + OrderStatus.REFOUND);
		}

		SystemUser user;
		try{
			user = this.systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException(
				"Falha ao recarregar os dados do cliente" + order.getId(), e);
		}
		
		//atualiza o status do pedido
		order.setStatus(OrderStatus.REFOUND);
		
		//Processa os itens do pedido
		for(ProductRequest productRequest: order.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.refoundItem(user, order, productRequest);
			}
			catch(Throwable e){
				throw new OrderRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId(), e);
			}
		}
		
		//Registra as alterações do pedido
		this.registerOrder(order);
		
		//Registra o evento no log
		this.registryLog(order.getId(), message != null? message : "Reembolso feito.");
		
	}
	
	@Override
	public void revertRefound(String orderID, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.REFOUND.getRevertPermission());
		
		try{
			this.unsafeRevertRefound(orderID, message);
		}
		catch(RegistryException e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao reserver o reembolso", e);
			throw e;
		}
		catch(Throwable e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao reserver o reembolso", e);
			throw new OrderRegistryException(e);
		}
	}
	
	private void unsafeRevertRefound(String orderID, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException{
		
		Order order = findById(orderID);
		
		if(order.getStatus() == OrderStatus.CANCELED_REFOUND){
			throw new UnmodifiedOrderStatusRegistryException("Reembolso cancelado.");
		}
		
		//Verifica se o status é o adequado
		if(!this.isValid(order, OrderStatus.CANCELED_REFOUND)){
			throw new OrderStatusNotAllowedRegistryException(
					"novo status não é permitido: " + 
					order.getStatus() + " -> " + OrderStatus.CANCELED_REFOUND);
		}

		SystemUser user;
		
		try{
			user = this.systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException(
				"Falha ao recarregar os dados do cliente" + order.getId(), e);
		}
		
		//atualiza o status do pedido
		order.setStatus(OrderStatus.CANCELED_REFOUND);
		
		//Processa os itens do pedido
		for(ProductRequest productRequest: order.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.revertRefoundItem(user, order, productRequest);
			}
			catch(Throwable e){
				throw new OrderRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId());
			}
		}
		
		//Registra as alterações do pedido
		this.registerOrder(order);
		
		//Registra o evento no log
		this.registryLog(order.getId(), message != null? message : "O processo de Reemboldo foi finalizada.");
	}
	
	@Override
	public Shipping createShipping(String orderID, 
			boolean useAlternativeAdress, String shippingCode) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException {
		throw new OrderRegistryException("not implemented yet");
	}
	
	private void validateOrder(Order order, Class<?> ... groups) throws ValidationException{
		ValidatorBean.validate(order, groups);
	}

	private boolean isValid(Order order, OrderStatus newStatus){
		//verifica se o proximo status é permitido
		OrderStatus currentStatus = order.getStatus();
		Set<OrderStatus> nextStatus = OrderRegistryImp.nextState.get(currentStatus);
		return nextStatus != null && nextStatus.contains(newStatus);
	}
	
	/* log  */
	
	public void registryLog(String orderID, String message) throws OrderRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.LOGS.getRegisterPermission());
		
		try{
			Order order = new Order();
			order.setId(orderID);
			orderEntityAccess.registryLog(order, message);
		}
		catch(Throwable e){
			throw new OrderRegistryException(e);
		}
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
		SystemUser user = systemUserRegistry.getBySystemID(String.valueOf(userID.getSystemID()));
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return user;
	}

	private SystemUser getSystemUser(int id) throws SystemUserRegistryException {
		SystemUser user = systemUserRegistry.findById(id);
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(id));
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
