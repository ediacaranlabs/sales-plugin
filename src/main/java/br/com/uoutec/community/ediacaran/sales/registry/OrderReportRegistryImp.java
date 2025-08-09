package br.com.uoutec.community.ediacaran.sales.registry;

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
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportMessageEntityAccess;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
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
	private OrderReportEntityAccess entityAccess;
	
	@Inject
	private OrderReportIndexEntityAccess indexEntityAccess;
	
	@Inject
	private ClientRegistry clientRegistry;
	
	@Inject
	private OrderReportMessageEntityAccess orderReportMessageEntityAccess;
	
	@Inject
	private ActionRegistry actionRegistry;
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void registerOrderReport(OrderReport entity) throws OrderReportRegistryException, ValidationException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getRegisterPermission());
		
		boolean newEntity = entity.getId() == null;
		
		if(newEntity) {
			OrderReportRegistryUtil.validate(entity, saveValidations);
			OrderReportRegistryUtil.save(entity, entityAccess);
		}
		else {
			OrderReportRegistryUtil.validate(entity, updateValidations);
			OrderReportRegistryUtil.update(entity, entityAccess);
		}
		
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
		
		return OrderReportRegistryUtil.getOrderReportById(id, entityAccess, clientRegistry);
	}
	
	@Override
	@ActivateRequestContext
	public OrderReportResultSearch searchShipping(OrderReportSearch value) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getSearchPermission());
		
		return OrderReportRegistryUtil.searchOrderReport(value, indexEntityAccess, clientRegistry);
	}

	@Override
	@ActivateRequestContext
	public List<OrderReport> findByOrder(Order order) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDERREPORT_REGISTRY.getFindPermission());
		
		List<OrderReport> list = OrderReportRegistryUtil.findByOrder(order, entityAccess);
		List<OrderReport> r = new ArrayList<>();
		
		for(OrderReport e: list) {
			r.add(OrderReportRegistryUtil.reload(e, entityAccess, clientRegistry));
		}
		
		return r;
	}
	
}
