package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoicesResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.entity.registry.RegistryException;
import br.com.uoutec.filter.invoker.annotation.EnableFilters;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
public class InvoiceRegistryImp implements InvoiceRegistry {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};

	@Inject
	private ActionRegistry actionRegistry;
	
	@Inject
	private InvoiceEntityAccess entityAccess;

	@Inject
	private InvoiceIndexEntityAccess indexEntityAccess;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;

	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public void registerInvoice(Invoice entity) throws ValidationException, RegistryException, EntityAccessException, ProductTypeHandlerException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getRegisterPermission());
		
		if(entity.getOrder() == null) {
			throw new InvoiceRegistryException("order is empty");
		}
		
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		
		Order order = new Order();
		order.setId(entity.getOrder());
		order = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		
		if(order == null) {
			throw new InvoiceRegistryException("order not found #" + entity.getOrder());
		}
		
		if(entity.getId() == null){
			registryNewInvoice(entity, order);
		}
		else{
			updateInvoice(entity, order);
		}
	}

	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public void removeInvoice(Invoice entity) throws InvoiceRegistryException, ShippingRegistryException {
		
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
			throw new PersistenceInvoiceRegistryException(ex);
		}
	}

	@Override
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public Invoice findById(String id) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, null);
	}

	private Invoice unsafeFindById(String id, SystemUser systemUser) throws InvoiceRegistryException {
		
		try{
			Invoice e = entityAccess.findById(id);
			
			if(e != null) {
				if(systemUser != null && e.getClient().getId() != systemUser.getId()) {
					return null;
				}
			}
			
			ClientRegistry clientRegistry = EntityContextPlugin.getEntity(ClientRegistry.class);
			
			e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));					
			
			return e;
		}
		catch(Throwable e){
			throw new PersistenceInvoiceRegistryException(e);
		}
	}
	
	@Override
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public InvoicesResultSearch searchInvoice(InvoiceSearch value) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getSearchPermission());
		
		try{
			int page = value.getPage() == null? 1 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<Invoice> list = indexEntityAccess.search(value, firstResult, maxResults);
			List<Invoice> itens = new ArrayList<>();
			ClientRegistry clientRegistry   = EntityContextPlugin.getEntity(ClientRegistry.class);
			
			for(Invoice e: list) {
				e = entityAccess.findById(e.getId());
				e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
				itens.add(e);
			}
			
			return new InvoicesResultSearch(itens.size() > maxItens, -1, page, itens.size() > maxItens? itens.subList(0, maxItens -1) : itens);
		}
		catch(Throwable e){
			throw new PersistenceInvoiceRegistryException(e);
		}
	}

	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public Invoice createInvoice(Order order, Map<String, Integer> itens, String message) 
		throws RegistryException, ProductTypeHandlerException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCreatePermission());
		
		try {
			Order actualOrder = InvoiceRegistryUtil.getActualOrder(order, EntityContextPlugin.getEntity(OrderRegistry.class));
			Invoice Invoice = InvoiceRegistryUtil.toInvoice(order, itens);
			registryNewInvoice(Invoice, actualOrder);
			return Invoice;
		}
		catch(InvoiceRegistryException e){
			throw e;
		}
		catch(ShippingRegistryException e){
			throw e;
		}
		catch(OrderRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(e);
		}
		
	}
	
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public Invoice toInvoice(Order order) throws OrderRegistryException, PersistenceInvoiceRegistryException {
		
		Order actualOrder = InvoiceRegistryUtil.getActualOrder(order, EntityContextPlugin.getEntity(OrderRegistry.class));
		
		if(actualOrder == null) {
			throw new OrderNotFoundRegistryException(order.getId());
		}

		List<Invoice> actualInvoices;
		
		actualInvoices = InvoiceRegistryUtil.getActualInvoices(actualOrder, actualOrder.getClient(), entityAccess);
		
		return InvoiceRegistryUtil.toInvoice(actualOrder, actualInvoices);
	}
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public void cancelInvoice(Invoice invoice, String justification) throws RefundRegistryException, OrderRegistryException, InvoiceRegistryException, ShippingRegistryException, OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCancelPermission());
		
		unsafeCancelInvoices(Arrays.asList(invoice), justification);
		
	}
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public void cancelInvoices(Order order, String justification) throws InvoiceRegistryException, RefundRegistryException, OrderRegistryException, ShippingRegistryException, OrderReportRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCancelPermission());
		
		List<Invoice> invoices;
		
		try {
			invoices = entityAccess.findByOrder(order.getId());
		}
		catch (EntityAccessException e) {
			throw new InvoiceRegistryException(e);
		}

		unsafeCancelInvoices(invoices, justification);
	}

	private void unsafeCancelInvoices(List<Invoice> invoices, String justification) throws RefundRegistryException, OrderRegistryException, InvoiceRegistryException, ShippingRegistryException, OrderReportRegistryException {

		Map<String,List<Invoice>> map = InvoiceRegistryUtil.groupByOrder(invoices);
		
		if(map.isEmpty()) {
			return;
		}
		
		LocalDateTime cancelDate                = LocalDateTime.now();
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		ShippingRegistry shippingRegistry       = EntityContextPlugin.getEntity(ShippingRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		
		for(Entry<String,List<Invoice>> entry: map.entrySet()) {
			
			Order order = new Order();
			order.setId(entry.getKey());
			
			List<Refund> refunds = refundRegistry.findRefundByOrder(entry.getKey());
			List<Shipping> shippings = shippingRegistry.findByOrder(entry.getKey());
			List<OrderReport> reports = orderReportRegistry.findByOrder(entry.getKey());
			
			InvoiceRegistryUtil.cancelInvoices(order, refunds, invoices, shippings, reports, justification, 
					cancelDate, orderRegistry, shippingRegistry, entityAccess);
			
		}
		
	}
	
	@Override
	@ActivateRequestContext
	@EnableFilters(InvoiceRegistry.class)
	public List<Invoice> findByOrder(String id) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		try {
			return entityAccess.findByOrder(id);
		}
		catch(Throwable ex) {
			throw new PersistenceInvoiceRegistryException(ex);
		}
	}

	private void registryNewInvoice(Invoice entity, Order order) throws ValidationException, InvoiceRegistryException, ProductTypeRegistryException, ProductTypeHandlerException, OrderRegistryException, RefundRegistryException, ShippingRegistryException {
		
		InvoiceRegistryUtil.validateInvoice(entity, saveValidations);
		
		ClientRegistry clientRegistry           = EntityContextPlugin.getEntity(ClientRegistry.class);
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		//ShippingRegistry shippingRegistry       = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		Order actualOrder                  = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		Client actualClient                = InvoiceRegistryUtil.getActualUser(actualOrder, clientRegistry);		
		List<Invoice> actualInvoices       = InvoiceRegistryUtil.getActualInvoices(actualOrder, actualClient, entityAccess);
		List<Refund> refunds               = InvoiceRegistryUtil.getActualRefunds(actualOrder, refundRegistry);
		//List<Shipping> shippings           = InvoiceRegistryUtil.getActualShippings(actualOrder, shippingRegistry);
		
		//InvoiceRegistryUtil.checkAllowedCreateInvoice(actualOrder);
		//OrderRegistryUtil.checkNewOrderStatus(actualOrder, OrderStatus.ORDER_INVOICED);
		OrderRegistryUtil.checkPayment(actualOrder);
		InvoiceRegistryUtil.checkCanceledInvoice(entity);
		InvoiceRegistryUtil.checkIsCompletedInvoice(order, refunds, actualInvoices);
		InvoiceRegistryUtil.checkUnits(entity, order, refunds, actualInvoices);		
		InvoiceRegistryUtil.preventChangeInvoiceSaveSensitiveData(entity);
		InvoiceRegistryUtil.registerProducts(entity, actualClient, actualOrder, productTypeRegistry);
		InvoiceRegistryUtil.save(entity, entityAccess);
		InvoiceRegistryUtil.saveOrUpdateIndex(entity, indexEntityAccess);
		InvoiceRegistryUtil.updateStatus(entity, actualOrder, refunds, null, actualInvoices, orderRegistry);
		OrderRegistryUtil.registerEvent("Criada a fatura #" + entity.getId(), actualOrder, orderRegistry);
		InvoiceRegistryUtil.registerNewInvoiceEvent(actionRegistry, entity);
		
	}

	private void updateInvoice(Invoice entity, Order order
			) throws ValidationException, OrderRegistryException, InvoiceRegistryException, EntityAccessException, RefundRegistryException, ShippingRegistryException  {
		
		InvoiceRegistryUtil.validateInvoice(entity, updateValidations);
		
		ClientRegistry clientRegistry      = EntityContextPlugin.getEntity(ClientRegistry.class);
		OrderRegistry orderRegistry        = EntityContextPlugin.getEntity(OrderRegistry.class);
		RefundRegistry refundRegistry      = EntityContextPlugin.getEntity(RefundRegistry.class);
		ShippingRegistry shippingRegistry  = EntityContextPlugin.getEntity(ShippingRegistry.class);
		
		Order actualOrder                  = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		Client actualClient                = InvoiceRegistryUtil.getActualUser(actualOrder, clientRegistry);		
		Invoice actualInvoice              = InvoiceRegistryUtil.getActualInvoice(entity, entityAccess);
		
		List<Invoice> actualInvoices       = InvoiceRegistryUtil.getActualInvoices(order, actualClient, entityAccess);
		List<Refund> refunds               = InvoiceRegistryUtil.getActualRefunds(actualOrder, refundRegistry);
		List<Shipping> shippings           = InvoiceRegistryUtil.getActualShippings(actualOrder, shippingRegistry);
		
		//InvoiceRegistryUtil.checkAllowedUpdateInvoice(actualOrder);
		//OrderRegistryUtil.checkNewOrderStatus(order, OrderStatus.ORDER_INVOICED);
		InvoiceRegistryUtil.checkCanceledInvoiceDate(entity, actualInvoice);
		InvoiceRegistryUtil.checkIsCompletedInvoice(order, refunds, actualInvoices);
		InvoiceRegistryUtil.checkUnits(entity, order, refunds, actualInvoices);		
		InvoiceRegistryUtil.preventChangeInvoiceSensitiveData(entity, actualInvoice);
		InvoiceRegistryUtil.update(entity, entityAccess);
		InvoiceRegistryUtil.updateStatus(entity, actualOrder, refunds, shippings, actualInvoices, orderRegistry);
		InvoiceRegistryUtil.saveOrUpdateIndex(entity, indexEntityAccess);

	}
	
}
