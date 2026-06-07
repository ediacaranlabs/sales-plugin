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
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
public class ShippingRegistryImp implements ShippingRegistry {

	//private static final String ORDER_EVENT_GROUP = "ORDER";

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private ActionRegistry actionRegistry;
	
	@Inject
	private ShippingEntityAccess entityAccess;

	@Inject
	private ShippingIndexEntityAccess indexEntityAccess;
	
	@Inject
	private ClientRegistry clientRegistry;

	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void registerShipping(Shipping entity) throws ShippingRegistryException, OrderRegistryException, ValidationException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getRegisterPermission());
		
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		Order order = ShippingRegistryUtil.getActualOrder(entity.getOrder(), orderRegistry);
		
		if(order == null) {
			throw new OrderRegistryException("order not found #" + entity.getOrder());
		}
		
		try{
			if(entity.getId() == null){
				registryNewShipping(entity, order);
			}
			else{
				updateShipping(entity, order);
			}
		}
		catch(OrderRegistryException e){
			throw e;
		}
		catch(ShippingRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new ShippingRegistryException(e);
		}
	}

	@Override
	@Transactional(rollbackOn = Throwable.class)
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
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void confirmShipping(Shipping shipping) throws ShippingRegistryException, OrderRegistryException, RefundRegistryException, OrderReportRegistryException, InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getConfirmPermission());

		Order order = new Order();
		order.setId(shipping.getOrder());
		
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		//OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		InvoiceRegistry invoiceRegistry         = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		
		Order actualOrder                = ShippingRegistryUtil.getActualOrder(order, orderRegistry);
		List<Shipping> actualShippings   = ShippingRegistryUtil.getActualShippings(order, entityAccess);
		List<Invoice> actualInvoices     = ShippingRegistryUtil.getActualInvoices(actualOrder, invoiceRegistry);
		List<Refund> refunds             = ShippingRegistryUtil.getActualRefunds(actualOrder, refundRegistry);
		//List<OrderReport> actualReports  = ShippingRegistryUtil.getActualReports(actualOrder, orderReportRegistry);
		Shipping actualShipping          = ShippingRegistryUtil.getActualShipping(shipping.getId(), entityAccess);
		
		
		if(actualShipping != null && !actualShipping.isClosed()) {
			ShippingRegistryUtil.confirmShipping(actualShipping, entityAccess);
			ShippingRegistryUtil.update(actualShipping, actualOrder, entityAccess);
			ShippingRegistryUtil.saveOrUpdateIndex(shipping, indexEntityAccess);
		}
		
		ShippingRegistryUtil.updateStatus(actualShipping, actualOrder, refunds, actualShippings, actualInvoices, null, orderRegistry);
	}
	
	@Override
	@ActivateRequestContext
	public Shipping findById(String id) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());
		
		return unsafeFindById(id);
	}

	private Shipping unsafeFindById(String id) throws ShippingRegistryException {
		
		try{
			Shipping e = entityAccess.findById(id);
			
			if(e != null) {
				
				if(e.getOrder() == null) {
					return null;
				}
				
				e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));					
				
			}
			
			return e;
		}
		catch(Throwable e){
			throw new ShippingRegistryException(e);
		}
	}
	
	@Override
	@ActivateRequestContext
	public ShippingResultSearch searchShipping(ShippingSearch value) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getSearchPermission());
		
		try{
			int page = value.getPage() == null? 1 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<Shipping> list = indexEntityAccess.search(value, firstResult, maxResults);
			List<Shipping> itens = new ArrayList<>();
			
			for(Shipping e: list) {
				e = entityAccess.findById(e.getId());
				e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
				itens.add(e);
			}
			
			return new ShippingResultSearch(itens.size() > maxItens, -1, page, itens.size() > maxItens? itens.subList(0, maxItens -1) : itens);
		}
		catch(Throwable e){
			throw new ShippingRegistryException(e);
		}
	}

	
	@Override
	@ActivateRequestContext
	public Shipping createShipping(Order order, String shippingType, Map<String, String> data, Map<String, Integer> itens, 
			String message) throws ShippingRegistryException, OrderRegistryException, InvoiceRegistryException, RefundRegistryException, OrderReportRegistryException, ValidationException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getCreatePermission());
		
		try {
			Order actualOrder = ShippingRegistryUtil.getActualOrder(order, EntityContextPlugin.getEntity(OrderRegistry.class));
			Shipping shipping = ShippingRegistryUtil.toShipping(order, shippingType, data, itens);
			registryNewShipping(shipping, actualOrder);
			return shipping;
		}
		catch(ShippingRegistryException ex) {
			throw ex;
		}
		catch(OrderRegistryException ex) {
			throw ex;
		}
		catch(InvoiceRegistryException ex) {
			throw ex;
		}
		catch(RefundRegistryException ex) {
			throw ex;
		}
		catch(OrderReportRegistryException ex) {
			throw ex;
		}
		catch(ValidationException ex) {
			throw ex;
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		
	}
	
	
	@Override
	@ActivateRequestContext
	public Shipping toShipping(Order order, String shippingType) throws InvalidUnitsOrderRegistryException, CountryRegistryException, OrderNotFoundRegistryException, ProductTypeRegistryException, ShippingRegistryException {
		
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
		try {
			actualShippings = ShippingRegistryUtil.getActualShippings(actualOrder, entityAccess);
		}
		catch(Throwable e) {
			throw new OrderNotFoundRegistryException(e);
		}
		
		return ShippingRegistryUtil.toShipping(actualOrder, shippingType, null, actualShippings);
	}
	
	@Override
	@ActivateRequestContext
	public List<Shipping> findByOrder(String id) throws ShippingRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());
		
		try {
			return entityAccess.findByOrder(id);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}

	@Override
	@ActivateRequestContext
	public List<Shipping> findByOrder(String id, SystemUserID userID) throws ShippingRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());

		try {
			Order order = new Order();
			order.setId(id);
			return ShippingRegistryUtil.getActualShippings(order, entityAccess);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		
	}
	
	@Transactional(rollbackOn = Throwable.class)
	@Override
	@ActivateRequestContext
	public void cancelShipping(Shipping shipping, String justification
			) throws ShippingRegistryException, OrderRegistryException, ProductTypeRegistryException, RefundRegistryException, OrderReportRegistryException, InvoiceRegistryException {
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
			) throws EntityAccessException, OrderRegistryException, ShippingRegistryException, ProductTypeRegistryException, RefundRegistryException, OrderReportRegistryException, InvoiceRegistryException {

		Map<String,List<Shipping>> map = ShippingRegistryUtil.groupByOrder(shippings);
		
		if(map.isEmpty()) {
			return;
		}
		
		LocalDateTime cancelDate                = LocalDateTime.now();
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		ShippingRegistry shippingRegistry       = EntityContextPlugin.getEntity(ShippingRegistry.class);
		InvoiceRegistry invoiceRegistry         = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		//OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		
		for(Entry<String,List<Shipping>> entry: map.entrySet()) {
			
			Order order = new Order();
			order.setId(entry.getKey());
			
			List<Refund> refunds = ShippingRegistryUtil.getActualRefunds(order, refundRegistry);
			//List<OrderReport> reports = ShippingRegistryUtil.getActualReports(order, orderReportRegistry);
			List<Invoice> invoices = ShippingRegistryUtil.getActualInvoices(order, invoiceRegistry);
			
			ShippingRegistryUtil.cancelShippings(order, refunds, invoices, shippings, null, 
					justification, cancelDate, orderRegistry, shippingRegistry, entityAccess, indexEntityAccess);
			
		}
		
	}
	
	private void registryNewShipping(Shipping shipping, Order order
			) throws ValidationException, OrderRegistryException, ShippingRegistryException, EntityAccessException, ProductTypeRegistryException, InvoiceRegistryException, RefundRegistryException, OrderReportRegistryException {
		
		validateShipping(shipping, saveValidations);
		
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		InvoiceRegistry invoiceRegistry         = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		//OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		
		Order actualOrder				= ShippingRegistryUtil.getActualOrder(order, orderRegistry);
		List<Shipping> actualShippings	= ShippingRegistryUtil.getActualShippings(actualOrder, entityAccess);
		List<Invoice> actualInvoices	= ShippingRegistryUtil.getActualInvoices(actualOrder, invoiceRegistry);
		List<Refund> refunds            = InvoiceRegistryUtil.getActualRefunds(actualOrder, refundRegistry);
		//List<OrderReport> actualReports = ShippingRegistryUtil.getActualReports(actualOrder, orderReportRegistry);
		
		//ShippingRegistryUtil.checkAllowedCreateShipping(actualOrder);
		//OrderRegistryUtil.checkNewOrderStatus(order, OrderStatus.ORDER_SHIPPED);
		ShippingRegistryUtil.checkShippableProducts(shipping, productTypeRegistry);
		ShippingRegistryUtil.checkIsCompletedShipping(order, refunds, actualShippings);
		ShippingRegistryUtil.checkUnits(shipping, order, actualInvoices, actualShippings);
		ShippingRegistryUtil.preventChangeShippingSaveSensitiveData(shipping);
		ShippingRegistryUtil.save(shipping, actualOrder, entityAccess);
		ShippingRegistryUtil.updateStatus(shipping, actualOrder, refunds, actualShippings, actualInvoices, null, orderRegistry);
		ShippingRegistryUtil.saveOrUpdateIndex(shipping, indexEntityAccess);
		OrderRegistryUtil.registerEvent("Criada envio #" + shipping.getId(), actualOrder, orderRegistry);
		ShippingRegistryUtil.registerNewShippingEvent(actionRegistry, shipping);
		//ShippingRegistryUtil.markOrderAsComplete(shipping, actualOrder, refunds, actualShippings, actualReports, orderRegistry);
		
	}

	private void updateShipping(Shipping shipping, Order order
			) throws ValidationException, OrderRegistryException, ShippingRegistryException, EntityAccessException, ProductTypeRegistryException, InvoiceRegistryException, RefundRegistryException, OrderReportRegistryException {
		
		validateShipping(shipping, updateValidations);
		
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		InvoiceRegistry invoiceRegistry         = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		//OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		RefundRegistry refundRegistry           = EntityContextPlugin.getEntity(RefundRegistry.class);
		
		Order actualOrder                = ShippingRegistryUtil.getActualOrder(order, orderRegistry);
		List<Shipping> actualShippings   = ShippingRegistryUtil.getActualShippings(order, entityAccess);
		List<Invoice> actualInvoices     = ShippingRegistryUtil.getActualInvoices(actualOrder, invoiceRegistry);
		List<Refund> refunds             = InvoiceRegistryUtil.getActualRefunds(actualOrder, refundRegistry);
		//List<OrderReport> actualReports  = ShippingRegistryUtil.getActualReports(actualOrder, orderReportRegistry);
		Shipping actualShipping          = ShippingRegistryUtil.getActualShipping(shipping.getId(), entityAccess);
		
		//ShippingRegistryUtil.checkAllowedUpdateShipping(actualOrder);
		//OrderRegistryUtil.checkNewOrderStatus(order, OrderStatus.ORDER_SHIPPED);
		ShippingRegistryUtil.checkShippableProducts(shipping, productTypeRegistry);
		ShippingRegistryUtil.checkIsCompletedShipping(order, refunds, actualShippings);
		ShippingRegistryUtil.checkUnits(shipping, order, actualInvoices, actualShippings);
		
		ShippingRegistryUtil.preventChangeShippingSensitiveData(shipping, actualShipping);
		ShippingRegistryUtil.update(actualShipping, order, entityAccess);
		ShippingRegistryUtil.updateStatus(actualShipping, actualOrder, refunds, actualShippings, actualInvoices, null, orderRegistry);
		ShippingRegistryUtil.saveOrUpdateIndex(shipping, indexEntityAccess);
		//ShippingRegistryUtil.markOrderAsComplete(shipping, actualOrder, refunds, actualShippings, actualReports, orderRegistry);
		
	}
	
	private void validateShipping(Shipping e, Class<?> ... groups) throws ValidationException{
		ValidatorBean.validate(e, groups);
	}
	
}
