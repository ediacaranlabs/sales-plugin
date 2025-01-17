package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.event.EventRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.entity.registry.RegistryException;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
public class InvoiceRegistryImp implements InvoiceRegistry{

	private static final String ORDER_EVENT_GROUP = "ORDER";

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private InvoiceEntityAccess entityAccess;

	@Inject
	private EventRegistry throwSystemEventRegistry;
	
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Inject
	private ClientRegistry clientRegistry;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;

	@Inject
	private SubjectProvider subjectProvider;
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void registerInvoice(Invoice entity) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getRegisterPermission());
		
		if(entity.getOrder() == null) {
			throw new InvoiceRegistryException("order is empty");
		}
		
		try{
			OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
			
			Order order = new Order();
			order.setId(entity.getOrder());
			order = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
			
			if(order == null) {
				throw new InvoiceRegistryException("order not found #" + entity.getOrder());
			}
			
			if(entity.getId() == null){
				validateInvoice(entity, saveValidations);
				this.registryNewInvoice(entity, order);
			}
			else{
				validateInvoice(entity, updateValidations);
				this.updateInvoice(entity, order);
			}
		}
		catch(ValidationException e){
			throw new InvoiceRegistryException(e.getMessage());
		}
		catch(InvoiceRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(e);
		}
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void removeInvoice(Invoice entity) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getRemovePermission());
		
		Invoice actualInvoice = InvoiceRegistryUtil.getActualInvoice(entity, entityAccess);
		
		if(actualInvoice == null) {
			return;
		}
		
		ShippingRegistry shippingRegistry = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		Order order = new Order();
		order.setId(actualInvoice.getOrder());
		
		InvoiceRegistryUtil.checkIfExistsShipping(order, shippingRegistry);
		
		try {
			entityAccess.delete(entity);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}

	@Override
	@ActivateRequestContext
	public Invoice findById(String id) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, null);
	}

	@ActivateRequestContext
	public Invoice findById(String id, SystemUserID userID) throws InvoiceRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());

		SystemUser systemUser = null;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			systemUser = getSystemUser(userID);
		}
		catch (SystemUserRegistryException e) {
			throw new InvoiceRegistryException(e);
		}
		
		return unsafeFindById(id, systemUser);
		
	}
	
	@ActivateRequestContext
	public Invoice findById(String id, SystemUser systemUser) throws InvoiceRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, systemUser);
	}
	
	private Invoice unsafeFindById(String id, SystemUser systemUser) throws InvoiceRegistryException {
		
		try{
			Invoice e = entityAccess.findById(id);
			
			if(e != null) {
				if(systemUser != null && e.getOwner() != systemUser.getId()) {
					return null;
				}
			}
			return e;
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(e);
		}
	}
	
	@Override
	@ActivateRequestContext
	public List<InvoiceResultSearch> searchInvoice(InvoiceSearch value, Integer first, Integer max) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getSearchPermission());
		
		try{
			return entityAccess.search(value, first, max);
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(e);
		}
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public Invoice createInvoice(Order order, Map<String, Integer> itens, String message) 
		throws RegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCreatePermission());
		
		SystemUserID userID = getSystemUserID();
		SystemUser user = getSystemUser(userID);
		
		try {
			return unsafeCreateInvoice(order, user, itens, message);
		}
		catch(RegistryException e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw e;
		}
		catch(Throwable e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw new OrderRegistryException(e);
		}
		
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public Invoice createInvoice(Order order, SystemUserID userID, Map<String, Integer> itens, String message) 
			throws RegistryException{
		
		SystemUser user = getSystemUser(userID);
		return createInvoice(order, user, itens, message);
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public Invoice createInvoice(Order order, SystemUser systemUser, Map<String, Integer> itens, String message) 
			throws RegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCreatePermission());
		
		if(systemUser == null) {
			throw new NullPointerException("systemUser");
		}
		
		try {
			return unsafeCreateInvoice(order, systemUser, itens, message);
		}
		catch(RegistryException e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw e;
		}
		catch(Throwable e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw new OrderRegistryException(e);
		}
		
	}
	
	@ActivateRequestContext
	public Invoice toInvoice(Order order
			) throws OrderNotFoundRegistryException, ItemNotFoundOrderRegistryException, 
				InvalidUnitsOrderRegistryException {
		
		Order actualOrder;
		
		try {
			actualOrder = InvoiceRegistryUtil.getActualOrder(order, EntityContextPlugin.getEntity(OrderRegistry.class));
		}
		catch(Throwable e) {
			throw new OrderNotFoundRegistryException(e);
		}
		
		if(actualOrder == null) {
			throw new OrderNotFoundRegistryException(order.getId());
		}

		List<Invoice> actualInvoices;
		
		try {
			Client client = new Client();
			client.setId(actualOrder.getOwner());
			actualInvoices = InvoiceRegistryUtil.getActualInvoices(actualOrder, client, entityAccess);
		}
		catch(Throwable e) {
			throw new OrderNotFoundRegistryException(e);
		}
		
		return createInvoice(actualOrder, actualInvoices);
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void cancelInvoice(Invoice invoice, String justification) throws InvoiceRegistryException {
		cancelInvoice(invoice, null, justification);
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void cancelInvoice(Invoice invoice, SystemUserID userID, String justification) throws InvoiceRegistryException{
		
		SystemUser systemUser;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			systemUser = getSystemUser(userID);
		}
		catch (SystemUserRegistryException e) {
			throw new InvoiceRegistryException(e);
		}
		
		cancelInvoice(invoice, systemUser, justification);
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void cancelInvoice(Invoice invoice, SystemUser systemUser, String justification) throws InvoiceRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCancelPermission());
		
		List<Invoice> invoices;
		
		try {
			Invoice actualInvoice = entityAccess.findById(invoice.getId());
			invoices = Arrays.asList(actualInvoice);
		}
		catch (EntityAccessException e) {
			throw new InvoiceRegistryException(e);
		}

		try {
			unsafeCancelInvoices(invoices, justification, systemUser);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
		
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void cancelInvoices(Order order, String justification) throws InvoiceRegistryException {
		cancelInvoices(order, null, justification);
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void cancelInvoices(Order invoice, SystemUserID userID, String justification) throws InvoiceRegistryException{
		
		SystemUser systemUser;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			systemUser = getSystemUser(userID);
		}
		catch (SystemUserRegistryException e) {
			throw new InvoiceRegistryException(e);
		}
		
		cancelInvoices(invoice, systemUser, justification);
	}

	
	@Override
	@Transactional
	@ActivateRequestContext
	public void cancelInvoices(Order order, SystemUser systemUser, String justification) throws InvoiceRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCancelPermission());
		
		List<Invoice> invoices;
		
		try {
			SystemUser user = new SystemUser();
			user.setId(order.getOwner());
			invoices = entityAccess.findByOrder(order.getId(), user);
		}
		catch (EntityAccessException e) {
			throw new InvoiceRegistryException(e);
		}

		try {
			unsafeCancelInvoices(invoices, justification, null);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}

	private void unsafeCancelInvoices(List<Invoice> invoices, String justification, 
			SystemUser user) throws EntityAccessException, OrderRegistryException, InvoiceRegistryException {

		Map<String,List<Invoice>> map = InvoiceRegistryUtil.groupByOrder(invoices);
		
		if(map.isEmpty()) {
			return;
		}
		
		LocalDateTime cancelDate          = LocalDateTime.now();
		OrderRegistry orderRegistry       = EntityContextPlugin.getEntity(OrderRegistry.class);
		ShippingRegistry shippingRegistry = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		for(Entry<String,List<Invoice>> entry: map.entrySet()) {
			
			Order order = new Order();
			order.setId(entry.getKey());
			
			InvoiceRegistryUtil.cancelInvoices(invoices, order, justification, 
					cancelDate, orderRegistry, shippingRegistry, entityAccess);
			
		}
		
	}
	
	@Override
	@ActivateRequestContext
	public List<Invoice> findByOrder(String id) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		try {
			return entityAccess.findByOrder(id, null);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}

	@Override
	@ActivateRequestContext
	public List<Invoice> findByOrder(String id, SystemUserID userID) throws InvoiceRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());

		SystemUser systemUser = null;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			systemUser = getSystemUser(userID);
		}
		catch (SystemUserRegistryException e) {
			throw new InvoiceRegistryException(e);
		}
		
		try {
			return entityAccess.findByOrder(id, systemUser);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
		
	}
	
	private Invoice unsafeCreateInvoice(Order order, SystemUser systemUser, Map<String, Integer> itens, String message
			) throws OrderRegistryException, InvoiceRegistryException, EntityAccessException, ShippingRegistryException, ProductTypeRegistryException{

		
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		Order actualOrder = orderRegistry.findById(order.getId());

		Invoice i = createInvoice(actualOrder, itens);
		
		registryNewInvoice(i, actualOrder);

		return i;
	}

	private Invoice createInvoice(Order order, List<Invoice> invoices) throws ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {
		Map<String, ProductRequest> transientItens = InvoiceRegistryUtil.toMap(order.getItens());
		InvoiceRegistryUtil.loadInvoicesToCalculateUnits(invoices, null, transientItens);
		return InvoiceRegistryUtil.toInvoice(order, transientItens.values());
	}
	
	private Invoice createInvoice(Order order, Map<String, Integer> itens
			) throws ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {
		Map<String, ProductRequest> transientItens = InvoiceRegistryUtil.toMap(order.getItens());
		List<ProductRequest> invoiceItens          = InvoiceRegistryUtil.setUnitsAndGetCollection(transientItens, itens);
		return InvoiceRegistryUtil.toInvoice(order, invoiceItens);
	}

	private void registryNewInvoice(Invoice entity, Order order
			) throws OrderStatusNotAllowedRegistryException, UnmodifiedOrderStatusRegistryException, 
			OrderRegistryException, InvoiceRegistryException, EntityAccessException, ShippingRegistryException, ProductTypeRegistryException {
		
		Client client = new Client();
		client.setId(entity.getOwner());
		
		OrderRegistry orderRegistry        = EntityContextPlugin.getEntity(OrderRegistry.class);
		ShippingRegistry shippingRegistry  = EntityContextPlugin.getEntity(ShippingRegistry.class);
		Order actualOrder                  = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		Client actualClient                = InvoiceRegistryUtil.getActualUser(actualOrder, client, clientRegistry);		
		List<Invoice> actualInvoices       = InvoiceRegistryUtil.getActualInvoices(actualOrder, actualClient, entityAccess);
		List<Shipping> actualShippings     = ShippingRegistryUtil.getActualShippings(actualOrder, actualClient, shippingRegistry);
		
		OrderRegistryUtil.checkNewOrderStatus(actualOrder, OrderStatus.ORDER_INVOICED);
		OrderRegistryUtil.checkPayment(actualOrder);
		InvoiceRegistryUtil.checkInvoice(actualOrder, actualInvoices, entity, null);
		InvoiceRegistryUtil.registerProducts(entity, actualClient, actualOrder, productTypeRegistry);
		InvoiceRegistryUtil.preventChangeInvoiceSaveSensitiveData(entity);
		InvoiceRegistryUtil.save(entity, actualOrder, entityAccess);
		InvoiceRegistryUtil.markAsComplete(actualOrder, entity, actualInvoices, EntityContextPlugin.getEntity(OrderRegistry.class));
		OrderRegistryUtil.markAsCompleteOrder(actualOrder, entity, actualInvoices, actualShippings, orderRegistry, productTypeRegistry);
		InvoiceRegistryUtil.registerEvent(entity, actualOrder, null, orderRegistry);
	}

	private void updateInvoice(Invoice entity, Order order
			) throws OrderRegistryException, InvoiceRegistryException, EntityAccessException, ShippingRegistryException, ProductTypeRegistryException {
		
		Client client = new Client();
		client.setId(entity.getOwner());
		
		OrderRegistry orderRegistry        = EntityContextPlugin.getEntity(OrderRegistry.class);
		ShippingRegistry shippingRegistry  = EntityContextPlugin.getEntity(ShippingRegistry.class);
		Order actualOrder                  = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		Client actualClient                = InvoiceRegistryUtil.getActualUser(actualOrder, client, clientRegistry);		
		Invoice actualInvoice              = InvoiceRegistryUtil.getActualInvoice(entity, entityAccess);
		List<Invoice> actualInvoices       = InvoiceRegistryUtil.getActualInvoices(order, actualClient, entityAccess);
		List<Shipping> actualShippings     = ShippingRegistryUtil.getActualShippings(actualOrder, actualClient, shippingRegistry);
		
		InvoiceRegistryUtil.checkInvoice(order, actualInvoices, entity, entityAccess.findById(entity.getId()));
		InvoiceRegistryUtil.preventChangeInvoiceSensitiveData(entity, actualInvoice);
		InvoiceRegistryUtil.update(entity, order, entityAccess);
		InvoiceRegistryUtil.markAsComplete(actualOrder, entity, actualInvoices, EntityContextPlugin.getEntity(OrderRegistry.class));
		OrderRegistryUtil.markAsCompleteOrder(actualOrder, entity, actualInvoices, actualShippings, orderRegistry, productTypeRegistry);
	}
	
	private void validateInvoice(Invoice e, Class<?> ... groups) throws ValidationException{
		ValidatorBean.validate(e, groups);
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
