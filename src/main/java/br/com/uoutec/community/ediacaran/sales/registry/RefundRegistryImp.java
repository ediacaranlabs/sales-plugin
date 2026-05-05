package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.i18n.ValidationException;

@Singleton
public class RefundRegistryImp implements RefundRegistry {

	@Inject
	private RefundRegistryUtil refundRegistryUtil;
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void registerRefund(Refund entity) throws RefundRegistryException, ClientRegistryException, 
		ShippingRegistryException, OrderRegistryException, OrderReportRegistryException, ValidationException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getRegisterPermission());
		
		if(entity.getId() == null) {
			save(entity);
		}
		else {
			update(entity);
		}
		
	}

	public RefundResultSearch searchRefund(RefundSearch value) throws RefundRegistryException {
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getSearchPermission());
		return refundRegistryUtil.search(value);
	}
	
	private void save(Refund entity) throws ValidationException, RefundRegistryException, 
		ClientRegistryException, ShippingRegistryException, OrderRegistryException, OrderReportRegistryException {
		
		refundRegistryUtil.checkEntityToSave(entity);

		Order actualOrder 				= refundRegistryUtil.getActualOrder(entity);
		List<Refund> actualRefunds		= refundRegistryUtil.getActualRefunds(actualOrder);
		List<Shipping> actualShiping	= refundRegistryUtil.getActualShipping(actualOrder);
		
		refundRegistryUtil.checkOrder(entity);
		refundRegistryUtil.checkAllowedCreateRefund(actualOrder);
		refundRegistryUtil.checkAllowedRefundStatus(actualOrder);
		refundRegistryUtil.checkRefund(actualOrder, actualRefunds, entity, actualShiping);
		refundRegistryUtil.preventChangeRefundSaveSensitiveData(entity, actualOrder);
		refundRegistryUtil.save(entity, actualOrder);
		refundRegistryUtil.markAsComplete(actualOrder, actualRefunds, entity);
		refundRegistryUtil.registerEvent("Refund #" + entity.getId(), actualOrder);
		refundRegistryUtil.registerNewRefundEvent(entity);
		refundRegistryUtil.updateOrderStatus(actualOrder, actualRefunds, entity);
		
	}

	private void update(Refund entity) throws ValidationException, RefundRegistryException, ClientRegistryException, 
		ShippingRegistryException, OrderRegistryException, OrderReportRegistryException {
		
		refundRegistryUtil.checkEntityToUpdate(entity);
		
		Order actualOrder 				= refundRegistryUtil.getActualOrder(entity);
		Refund actualRefund 			= refundRegistryUtil.getActualRefund(entity);
		List<Refund> actualRefunds		= refundRegistryUtil.getActualRefunds(actualOrder);
		
		refundRegistryUtil.preventChangeRefundSensitiveData(entity, actualRefund);
		refundRegistryUtil.update(actualRefund, actualOrder);
		refundRegistryUtil.markAsComplete(actualOrder, actualRefunds, entity);
		refundRegistryUtil.updateOrderStatus(actualOrder, actualRefunds, entity);
		
	}
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void removeRefund(Refund entity) throws RefundRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getRemovePermission());
		
		Refund actualRefund = refundRegistryUtil.getActualRefund(entity);
		
		if(actualRefund == null) {
			return;
		}
		
		Order order = new Order();
		order.setId(entity.getOrder());
		
		refundRegistryUtil.delete(actualRefund, order);
	}

	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void confirmRefund(Refund entity) throws RefundRegistryException, ClientRegistryException, ShippingRegistryException, InvalidUnitsOrderRegistryException, OrderReportRegistryException, OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getConfirmPermission());

		Order order = new Order();
		order.setId(entity.getOrder());
		
		Refund actualRefund 			= refundRegistryUtil.getActualRefund(entity);
		Order actualOrder 				= refundRegistryUtil.getActualOrder(entity);
		List<Refund> actualRefunds		= refundRegistryUtil.getActualRefunds(actualOrder);
		
		if(entity != null && entity.getRefundDate() == null) {
			refundRegistryUtil.confirmRefund(actualRefund);
			refundRegistryUtil.update(actualRefund, actualOrder);
		}
		
		refundRegistryUtil.updateOrderStatus(actualOrder, actualRefunds, null);
	}
	
	@Override
	@ActivateRequestContext
	public Refund findRefundById(String id) throws RefundRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getFindPermission());
		
		return refundRegistryUtil.getActualRefund(id);
	}

	@Override
	@ActivateRequestContext
	public Refund createRefund(Order order, Map<String, Integer> itens) throws RefundRegistryException, InvalidUnitsOrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getCreatePermission());
		
		Order actualOrder							= refundRegistryUtil.getActualOrder(order);
		List<Refund> actualRefunds					= refundRegistryUtil.getActualRefunds(actualOrder);
		Map<String, ProductRequest> transientItens	= refundRegistryUtil.toMap(actualOrder.getItens());
		
		
		refundRegistryUtil.removeAllRefundItens(actualRefunds, null, transientItens);
		
		return refundRegistryUtil.toRefund(actualOrder, transientItens.values());
		
	}

	@Override
	@ActivateRequestContext
	public Refund createRefund(Order order) throws RefundRegistryException, InvalidUnitsOrderRegistryException {
		return createRefund(order, new HashMap<>());
	}
	
	@Override
	@ActivateRequestContext
	public List<Refund> findRefundByOrder(String id) throws RefundRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getFindPermission());
		
		Order order = new Order();
		order.setId(id);
		
		return refundRegistryUtil.getActualRefunds(order);
		
	}
	
}
