package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessageResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportMessageEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductRequestReportEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;

@Singleton
public class OrderReportRegistryImp implements OrderReportRegistry {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private OrderReportMessageEntityAccess orderReportMessageEntityAccess;
	
	@Inject
	private OrderReportEntityAccess entityAccess;
	
	@Inject
	private OrderReportIndexEntityAccess indexEntityAccess;
	
	
	@Inject
	private ProductRequestReportEntityAccess productRequestReportEntityAccess;
	
	//@Inject
	//private OrderReportMessageEntityAccess orderReportMessageEntityAccess;
	
	@Inject
	private ActionRegistry actionRegistry;
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void registerOrderReport(OrderReport entity) throws OrderReportRegistryException, ValidationException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getRegisterPermission());
		
		boolean newEntity = entity.getId() == null;
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		ClientRegistry clientRegistry           = EntityContextPlugin.getEntity(ClientRegistry.class);
		InvoiceRegistry invoiceRegistry	        = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		ShippingRegistry shippingRegistry	    = EntityContextPlugin.getEntity(ShippingRegistry.class);
		ProductTypeRegistry productTypeRegistry	= EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		
		try {
			if(newEntity) {
				save(entity, entityAccess, orderRegistry, clientRegistry, shippingRegistry, invoiceRegistry, productTypeRegistry);
			}
			else {
				update(entity, entityAccess, orderRegistry, clientRegistry, shippingRegistry, invoiceRegistry, productTypeRegistry);
			}
		}
		catch(OrderReportRegistryException ex) {
			throw ex;
		}
		catch(Throwable ex) {
			throw new OrderReportRegistryException(ex);
		}
		
		confirmRegistration(entity, newEntity, entityAccess, actionRegistry);
		
	}

	private void save(OrderReport entity, OrderReportEntityAccess entityAccess, OrderRegistry orderRegistry, ClientRegistry clientRegistry, ShippingRegistry shippingRegistry, 
			InvoiceRegistry invoiceRegistry, ProductTypeRegistry productTypeRegistry) throws Throwable {
		
		entity.setStatus(OrderReportStatus.NEW_REQUEST);
		entity.setDate(LocalDateTime.now());
		
		OrderReportRegistryUtil.validate(entity, saveValidations);
		OrderReportRegistryUtil.checkOrderReportStatus(entity, orderRegistry, entityAccess);
		
		Order order = new Order();
		order.setId(entity.getOrder().getId());
		
		Order actualOrder				= ShippingRegistryUtil.getActualOrder(order, orderRegistry);
		//Client actualClient				= ShippingRegistryUtil.getActualClient(actualOrder, order.getClient(), clientRegistry);		
		//List<Shipping> actualShippings	= ShippingRegistryUtil.getActualShippings(actualOrder, actualClient, shippingRegistry);
		//List<Invoice> actualInvoices	= InvoiceRegistryUtil.getActualInvoices(actualOrder, actualClient, invoiceRegistry);
		//List<OrderReport> actualReports = OrderReportRegistryUtil.findByOrder(actualOrder, entityAccess);
		
		OrderReportRegistryUtil.save(entity, entityAccess);
		OrderReportRegistryUtil.sendToRepository(entityAccess);
		//OrderRegistryUtil.markAsCompleteOrder(actualOrder, null, null, actualInvoices, actualShippings, actualReports, orderRegistry, productTypeRegistry);
		OrderRegistryUtil.updateOrder(actualOrder, orderRegistry);
	}

	private void update(OrderReport entity, OrderReportEntityAccess entityAccess, OrderRegistry orderRegistry, 
			ClientRegistry clientRegistry, ShippingRegistry shippingRegistry, 
			InvoiceRegistry invoiceRegistry, ProductTypeRegistry productTypeRegistry) throws ValidationException, OrderReportRegistryException, OrderRegistryException, 
			ShippingRegistryException, InvoiceRegistryException, ProductTypeRegistryException {

		OrderReportRegistryUtil.validate(entity, updateValidations);
		OrderReportRegistryUtil.checkUpdateOrderReportStatus(entity, orderRegistry, entityAccess, clientRegistry);
		
		Order order = new Order();
		order.setId(entity.getOrder().getId());
		
		Order actualOrder				= ShippingRegistryUtil.getActualOrder(order, orderRegistry);
		//Client actualClient				= ShippingRegistryUtil.getActualClient(actualOrder, order.getClient(), clientRegistry);		
		//List<Shipping> actualShippings	= ShippingRegistryUtil.getActualShippings(actualOrder, actualClient, shippingRegistry);
		//List<Invoice> actualInvoices	= InvoiceRegistryUtil.getActualInvoices(actualOrder, actualClient, invoiceRegistry);
		//List<OrderReport> actualReports = OrderReportRegistryUtil.findByOrder(actualOrder, entityAccess);
		
		OrderReportRegistryUtil.update(entity, entityAccess);
		OrderReportRegistryUtil.sendToRepository(entityAccess);
		OrderRegistryUtil.updateOrder(actualOrder, orderRegistry);
		
		//OrderRegistryUtil.markAsCompleteOrder(actualOrder, null, null, actualInvoices, actualShippings, actualReports, orderRegistry, productTypeRegistry);
	}
	
	private void confirmRegistration(OrderReport entity, boolean newEntity, OrderReportEntityAccess entityAccess, ActionRegistry actionRegistry) throws OrderReportRegistryException {
		OrderReportRegistryUtil.sendToRepository(entityAccess);
		OrderReportRegistryUtil.registerOrderReportRegisterEvent(actionRegistry, entity, newEntity);
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void removeOrderReport(OrderReport entity) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getRemovePermission());
		
		OrderReportRegistryUtil.delete(entity, entityAccess);
		OrderReportRegistryUtil.sendToRepository(entityAccess);
		
	}

	@Override
	@ActivateRequestContext
	public OrderReport findById(String id) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getFindPermission());
		
		ClientRegistry clientRegistry = EntityContextPlugin.getEntity(ClientRegistry.class);
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		
		return OrderReportRegistryUtil.getOrderReportById(id, entityAccess, clientRegistry, orderRegistry);
	}
	
	@Override
	@ActivateRequestContext
	public OrderReportResultSearch searchOrderReport(OrderReportSearch value) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getSearchPermission());
		
		ClientRegistry clientRegistry = EntityContextPlugin.getEntity(ClientRegistry.class);
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		
		return OrderReportRegistryUtil.searchOrderReport(value, entityAccess, indexEntityAccess, clientRegistry, orderRegistry);
	}

	@Override
	@ActivateRequestContext
	public List<OrderReport> findByOrder(Order order) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getFindPermission());
		
		ClientRegistry clientRegistry = EntityContextPlugin.getEntity(ClientRegistry.class);
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		List<OrderReport> list = OrderReportRegistryUtil.findByOrder(order, entityAccess);
		List<OrderReport> r = new ArrayList<>();
		
		for(OrderReport e: list) {
			r.add(OrderReportRegistryUtil.reload(e, entityAccess, clientRegistry, orderRegistry));
		}
		
		return r;
	}

	@Override
	public ProductRequestReport getProductRequestReport(String id, OrderReport orderReport)
			throws OrderReportRegistryException {

		return OrderReportRegistryUtil.getProductRequestReport(id, orderReport, productRequestReportEntityAccess);
	}

	@Override
	@ActivateRequestContext
	public OrderReport toOrderReport(Order order) throws OrderReportRegistryException {
		
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		
		return OrderReportRegistryUtil.toOrderReport(order, orderRegistry);
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void sendMessage(OrderReport orderReport, String message, SystemUser user) throws OrderReportRegistryException {
		OrderReportMessage e = OrderReportRegistryUtil.toOrderReportMessage(orderReport, message, LocalDateTime.now(), user);
		OrderReportRegistryUtil.registerMessage(e, orderReportMessageEntityAccess);
		OrderReportRegistryUtil.sendToRepository(orderReportMessageEntityAccess);
	}

	@Override
	@ActivateRequestContext
	public OrderReportMessageResultSearch getMessages(OrderReport orderReport, Integer page, Integer quantityPerPage) throws OrderReportRegistryException {
		
		SystemUserRegistry systemUserRegistry = EntityContextPlugin.getEntity(SystemUserRegistry.class);
		
		return OrderReportRegistryUtil.getOrderReportMessageByOrderReport(orderReport, page, quantityPerPage, orderReportMessageEntityAccess, systemUserRegistry);
	}

	@Override
	public OrderReportMessage getMessageById(String id) throws OrderReportRegistryException {
		return OrderReportRegistryUtil.findById(id, orderReportMessageEntityAccess);
	}
	
}
