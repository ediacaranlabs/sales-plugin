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
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingsResultSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.event.EventRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
public class ShippingRegistryImp implements ShippingRegistry{

	private static final String ORDER_EVENT_GROUP = "ORDER";

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private ShippingEntityAccess entityAccess;

	@Inject
	private EventRegistry throwSystemEventRegistry;
	
	@Inject
	private ClientRegistry clientRegistry;

	@Inject
	private OrderRegistry orderregistry;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
	@Inject
	private SubjectProvider subjectProvider;
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void registerShipping(Shipping entity) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getRegisterPermission());
		
		if(entity.getOrder() == null) {
			throw new ShippingRegistryException("order is empty");
		}
		
		try{
			OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
			Order order = orderRegistry.findById(entity.getOrder());
			
			if(order == null) {
				throw new InvoiceRegistryException("order not found #" + entity.getOrder());
			}
			
			if(entity.getId() == null){
				validateShipping(entity, saveValidations);
				this.registryNewShipping(entity, order);
			}
			else{
				validateShipping(entity, updateValidations);
				this.updateShipping(entity, order);
			}
		}
		catch(ValidationException e){
			throw new ShippingRegistryException(e.getMessage());
		}
		catch(ShippingRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new ShippingRegistryException(e);
		}
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void removeShipping(Shipping entity) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getRemovePermission());
		
		Shipping actualShipping = ShippingRegistryUtil.getActualShipping(entity, entityAccess);
		
		if(actualShipping == null) {
			return;
		}
		
		try {
			entityAccess.delete(entity);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}

	@Override
	@ActivateRequestContext
	public Shipping findById(String id) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, null);
	}

	@ActivateRequestContext
	public Shipping findById(String id, SystemUserID userID) throws ShippingRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());

		Client client = null;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			client = getSystemUser(userID);
		}
		catch (ClientRegistryException e) {
			throw new ShippingRegistryException(e);
		}
		
		return unsafeFindById(id, client);
		
	}
	
	@ActivateRequestContext
	public Shipping findById(String id, Client systemUser) throws ShippingRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, systemUser);
	}
	
	private Shipping unsafeFindById(String id, Client client) throws ShippingRegistryException {
		
		try{
			Shipping e = entityAccess.findById(id);
			
			if(e != null) {
				
				if(e.getOrder() == null) {
					return null;
				}
				
				
				if(client != null) {
					Order order = orderregistry.findByCartID(e.getOrder());
					if(order == null || order.getClient() != client.getId()) {
						return null;
					}
				}
			}
			
			return e;
		}
		catch(Throwable e){
			throw new ShippingRegistryException(e);
		}
	}
	
	@Override
	@ActivateRequestContext
	public ShippingsResultSearch searchShipping(ShippingSearch value) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getSearchPermission());
		
		try{
			int page = value.getPage() == null? 1 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<ShippingResultSearch> itens = entityAccess.search(value, firstResult, maxResults);
			
			return new ShippingsResultSearch(itens.size() > maxItens, -1, page, itens.size() > maxItens? itens.subList(0, maxItens -1) : itens);
		}
		catch(Throwable e){
			throw new ShippingRegistryException(e);
		}
	}

	@Override
	@ActivateRequestContext
	public Shipping createShipping(Order order, Map<String, Integer> itens, String message) throws ShippingRegistryException, ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getCreatePermission());
		
		try {
			return unsafeCreateShipping(order, itens, message);
		}
		catch(ShippingRegistryException e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw e;
		}
		catch(Throwable e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw new ShippingRegistryException(e);
		}
		
	}

	@Override
	@ActivateRequestContext
	public Shipping toShipping(Order order
			) throws InvalidUnitsOrderRegistryException, CountryRegistryException, OrderNotFoundRegistryException, ProductTypeRegistryException {
		
		Order actualOrder;
		
		try {
			actualOrder = ShippingRegistryUtil.getActualOrder(order, EntityContextPlugin.getEntity(OrderRegistry.class));
		}
		catch(Throwable e) {
			throw new OrderNotFoundRegistryException(e);
		}
		
		if(actualOrder == null) {
			throw new OrderNotFoundRegistryException(order.getId());
		}
		
		List<Shipping> actualShippings;
		Client user;
		
		try {
			user = new Client();
			user.setId(actualOrder.getClient());
			actualShippings = ShippingRegistryUtil.getActualShippings(actualOrder, user, entityAccess);
		}
		catch(Throwable e) {
			throw new OrderNotFoundRegistryException(e);
		}
		
		return createShipping(actualOrder, actualShippings);
	}
	
	@Override
	@ActivateRequestContext
	public List<Shipping> findByOrder(String id) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());
		
		try {
			return entityAccess.findByOrder(id, null);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}

	@Override
	@ActivateRequestContext
	public List<Shipping> findByOrder(String id, SystemUserID userID) throws ShippingRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());

		Client client = null;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			client = getSystemUser(userID);
		}
		catch (ClientRegistryException e) {
			throw new ShippingRegistryException(e);
		}
		
		try {
			Order order = new Order();
			order.setId(id);
			return ShippingRegistryUtil.getActualShippings(order, client, entityAccess);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		
	}
	
	private Shipping unsafeCreateShipping(Order order, Map<String, Integer> itens, String message
			) throws OrderRegistryException, CountryRegistryException, ShippingRegistryException, EntityAccessException, ProductTypeRegistryException, InvoiceRegistryException {

		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		Order actualOrder = orderRegistry.findById(order.getId());
		
		Shipping i = createShipping(actualOrder, itens);
		
		registryNewShipping(i, actualOrder);

		return i;
	}

	private Shipping createShipping(Order order, List<Shipping> shippings) throws InvalidUnitsOrderRegistryException, CountryRegistryException, ProductTypeRegistryException {
		Map<String, ProductRequest> transientItens = ShippingRegistryUtil.toMap(order.getItens(), productTypeRegistry);
		ShippingRegistryUtil.loadShippingsToCalculateUnits(shippings, null, transientItens);
		return ShippingRegistryUtil.toShipping(order, transientItens.values());
	}
	
	private Shipping createShipping(Order order, Map<String, Integer> itens
			) throws ItemNotFoundOrderRegistryException, CountryRegistryException, ProductTypeRegistryException {
		Map<String, ProductRequest> transientItens = ShippingRegistryUtil.toMap(order.getItens(), productTypeRegistry);
		List<ProductRequest> invoiceItens          = ShippingRegistryUtil.setUnitsAndGetCollection(transientItens, itens);
		return ShippingRegistryUtil.toShipping(order, invoiceItens);
	}

	@Transactional
	@Override
	@ActivateRequestContext
	public void cancelShipping(Shipping shipping, String justification
			) throws ShippingRegistryException, CompletedInvoiceRegistryException, OrderRegistryException, ProductTypeRegistryException {
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCancelPermission());
		
		List<Shipping> list;
		
		try {
			Shipping actualInvoice = entityAccess.findById(shipping.getId());
			list = Arrays.asList(actualInvoice);
		}
		catch (EntityAccessException e) {
			throw new ShippingRegistryException(e);
		}

		try {
			unsafeCancelShippings(list, justification);
		}
		catch(EntityAccessException ex) {
			throw new PersistenceShippingRegistryException();
		}
	}
	
	private void unsafeCancelShippings(List<Shipping> shippings, String justification
			) throws EntityAccessException, OrderRegistryException, CompletedInvoiceRegistryException, ShippingRegistryException, ProductTypeRegistryException {

		Map<String,List<Shipping>> map = ShippingRegistryUtil.groupByOrder(shippings);
		
		if(map.isEmpty()) {
			return;
		}
		
		LocalDateTime cancelDate          = LocalDateTime.now();
		OrderRegistry orderRegistry       = EntityContextPlugin.getEntity(OrderRegistry.class);
		ShippingRegistry shippingRegistry = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		for(Entry<String,List<Shipping>> entry: map.entrySet()) {
			
			Order order = new Order();
			order.setId(entry.getKey());
			
			ShippingRegistryUtil.cancelShippings(shippings, order, justification, 
					cancelDate, orderRegistry, shippingRegistry, entityAccess, productTypeRegistry);
			
		}
		
	}
	
	private void registryNewShipping(Shipping shipping, Order order
			) throws OrderRegistryException, ShippingRegistryException, EntityAccessException, ProductTypeRegistryException, InvoiceRegistryException {
		
		Client client = new Client();
		client.setId(order.getClient());

		OrderRegistry orderRegistry   = EntityContextPlugin.getEntity(OrderRegistry.class);
		InvoiceRegistry invoiceRegistry  = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		Order actualOrder             = ShippingRegistryUtil.getActualOrder(order, orderRegistry);
		Client actualClient           = ShippingRegistryUtil.getActualClient(actualOrder, client, clientRegistry);		
		List<Shipping> actualShippings = ShippingRegistryUtil.getActualShippings(actualOrder, actualClient, entityAccess);
		List<Invoice> actualInvoices     = InvoiceRegistryUtil.getActualInvoices(actualOrder, actualClient, invoiceRegistry);
		
		ShippingRegistryUtil.checkShipping(actualOrder, actualShippings, shipping, productTypeRegistry);
		ShippingRegistryUtil.preventChangeShippingSaveSensitiveData(shipping);
		ShippingRegistryUtil.save(shipping, actualOrder, entityAccess);
		ShippingRegistryUtil.markAsComplete(order, shipping, actualShippings, orderRegistry, productTypeRegistry);
		OrderRegistryUtil.markAsCompleteOrder(actualOrder, null, shipping, actualInvoices, actualShippings, orderRegistry, productTypeRegistry);
		OrderRegistryUtil.registerEvent("Criada envio #" + shipping.getId(), actualOrder, orderRegistry);

	}

	private void updateShipping(Shipping shipping, Order order
			) throws ShippingRegistryException, EntityAccessException, OrderRegistryException, ProductTypeRegistryException, InvoiceRegistryException {
		
		Client user = new Client();
		user.setId(order.getClient());
		
		OrderRegistry orderRegistry      = EntityContextPlugin.getEntity(OrderRegistry.class);
		InvoiceRegistry invoiceRegistry  = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		Order actualOrder                = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		Client actualClient              = ShippingRegistryUtil.getActualClient(order, user, clientRegistry);
		List<Shipping> actualShippings   = ShippingRegistryUtil.getActualShippings(order, actualClient, entityAccess);
		List<Invoice> actualInvoices     = InvoiceRegistryUtil.getActualInvoices(actualOrder, actualClient, invoiceRegistry);
		Shipping actualShipping          = ShippingRegistryUtil.getActualShipping(shipping.getId(), entityAccess);
		
		ShippingRegistryUtil.checkShipping(order, actualShippings, shipping, productTypeRegistry);
		ShippingRegistryUtil.preventChangeShippingSensitiveData(shipping, actualShipping);
		ShippingRegistryUtil.update(actualShipping, order, entityAccess);
		ShippingRegistryUtil.markAsComplete(order, shipping, actualShippings, EntityContextPlugin.getEntity(OrderRegistry.class), productTypeRegistry);
		OrderRegistryUtil.markAsCompleteOrder(actualOrder, null, shipping, actualInvoices, actualShippings, orderRegistry, productTypeRegistry);
		
	}
	
	private void validateShipping(Shipping e, Class<?> ... groups) throws ValidationException{
		ValidatorBean.validate(e, groups);
	}

	private Client getSystemUser(SystemUserID userID) throws ClientRegistryException {
		Client client = clientRegistry.getClientBySystemID(String.valueOf(userID.getSystemID()));
		
		if(client == null) {
			throw new ClientRegistryException(String.valueOf(userID));
		}
		
		return client;
	}

	private SystemUserID getSystemUserID() throws ClientRegistryException {
		Subject subject = subjectProvider.getSubject();
		
		if(!subject.isAuthenticated()) {
			throw new ClientRegistryException();
		}
		
		Principal principal = subject.getPrincipal();
		java.security.Principal jaaPrincipal = principal.getUserPrincipal();
		
		return ()->jaaPrincipal.getName();
	}
	
}
