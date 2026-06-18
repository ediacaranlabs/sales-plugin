package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.i18n.ValidationException;

@Singleton
public class RefundRegistryImp implements RefundRegistry {

	@Inject
	private RefundRegistryUtil refundRegistryUtil;
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void registerRefund(Refund entity) throws RefundRegistryException, ClientRegistryException, 
		ShippingRegistryException, OrderRegistryException, OrderReportRegistryException, ValidationException, PaymentGatewayException, InvoiceRegistryException {
		
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
		ClientRegistryException, ShippingRegistryException, OrderRegistryException, OrderReportRegistryException, PaymentGatewayException, InvoiceRegistryException {
		
		refundRegistryUtil.checkEntityToSave(entity);

		Order actualOrder 				= refundRegistryUtil.getActualOrder(entity);
		List<Refund> actualRefunds		= refundRegistryUtil.getActualRefunds(actualOrder);
		List<Shipping> actualShiping	= refundRegistryUtil.getActualShipping(actualOrder);
		List<Invoice> actualInvoice		= refundRegistryUtil.getActualInvoice(actualOrder);
		//boolean partialRefund			= refundRegistryUtil.isPartialRefund(entity, actualOrder, actualRefunds);
		
		refundRegistryUtil.checkOrder(entity);
		refundRegistryUtil.checkCanBeRefund(entity, actualOrder, actualRefunds, actualInvoice);
		refundRegistryUtil.checkAllowedCreateRefund(actualOrder);
		refundRegistryUtil.checkAllowedRefundStatus(actualOrder);
		refundRegistryUtil.checkRefund(actualOrder, actualRefunds, entity, actualShiping);
		refundRegistryUtil.preventChangeRefundSaveSensitiveData(entity, actualOrder);
		
		//PaymentGateway paymentGateway = refundRegistryUtil.getPaymentGateway(entity);
		//refundRegistryUtil.refundProducts(actualOrder, entity, partialRefund, entity.getProducts(), paymentGateway);
		refundRegistryUtil.save(entity, actualOrder);
		refundRegistryUtil.updateIndex(entity, actualOrder);
		
		refundRegistryUtil.updateStatus(entity, actualOrder, actualRefunds, actualShiping, actualInvoice);
		refundRegistryUtil.registerEvent("Refund #" + entity.getId(), actualOrder);
		refundRegistryUtil.scheduleRefund(entity);
		refundRegistryUtil.registerNewRefundEvent(entity);
		
	}

	private void update(Refund entity) throws ValidationException, RefundRegistryException, ClientRegistryException, 
		ShippingRegistryException, OrderRegistryException, OrderReportRegistryException, InvoiceRegistryException {
		
		refundRegistryUtil.checkEntityToUpdate(entity);
		
		Order actualOrder 				= refundRegistryUtil.getActualOrder(entity);
		Refund actualRefund 			= refundRegistryUtil.getActualRefund(entity);
		List<Refund> actualRefunds		= refundRegistryUtil.getActualRefunds(actualOrder);
		List<Invoice> actualInvoice		= refundRegistryUtil.getActualInvoice(actualOrder);
		List<Shipping> actualShiping	= refundRegistryUtil.getActualShipping(actualOrder);
		
		refundRegistryUtil.preventChangeRefundSensitiveData(entity, actualRefund);
		refundRegistryUtil.checkCanBeRefund(entity, actualOrder, actualRefunds, actualInvoice);
		refundRegistryUtil.update(actualRefund, actualOrder);
		refundRegistryUtil.updateIndex(actualRefund, actualOrder);
		refundRegistryUtil.updateStatus(entity, actualOrder, actualRefunds, actualShiping, actualInvoice);
		
	}
	
	@Override
	@ActivateRequestContext
	@Transactional(rollbackOn = Throwable.class)
	public void scheduleRefund(Refund entity) throws RefundRegistryException {
		
		if(entity == null || entity.getId() == null) {
			throw new NullPointerException();
		}
		
		Refund actualRefund = refundRegistryUtil.getActualRefund(entity);	
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getRegisterPermission());
		
		if(!actualRefund.isCompleted()) {
			refundRegistryUtil.scheduleRefund(entity);
		}
		
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
	public void confirmRefund(Refund entity) throws RefundRegistryException, ShippingRegistryException, InvoiceRegistryException, PaymentGatewayException, OrderRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getConfirmPermission());

		Order order = new Order();
		order.setId(entity.getOrder());
		
		Refund actualRefund = refundRegistryUtil.getActualRefund(entity);
		
		if(actualRefund.isCompleted()) {
			entity.setRefundDate(actualRefund.getRefundDate());
			return;
		}
		
		Order actualOrder 				= refundRegistryUtil.getActualOrder(entity);
		List<Refund> actualRefunds		= refundRegistryUtil.getActualRefunds(actualOrder);
		List<Shipping> actualShiping	= refundRegistryUtil.getActualShipping(actualOrder);
		List<Invoice> actualInvoice		= refundRegistryUtil.getActualInvoice(actualOrder);
		PaymentGateway paymentGateway	= refundRegistryUtil.getPaymentGateway(entity);
		boolean partialRefund			= refundRegistryUtil.isPartialRefund(entity, actualOrder, actualRefunds);
		
		refundRegistryUtil.refundProducts(actualOrder, actualRefund, partialRefund, actualRefund.getProducts(), paymentGateway);
		
		if(actualRefund != null && actualRefund.getRefundDate() != null) {
			refundRegistryUtil.confirmRefund(actualRefund);
			refundRegistryUtil.update(actualRefund, actualOrder);
			
			entity.setRefundDate(actualRefund.getRefundDate());
		}
		
		refundRegistryUtil.updateStatus(actualRefund, actualOrder, actualRefunds, actualShiping, actualInvoice);
		
	}
	
	@Override
	@ActivateRequestContext
	public Refund findRefundById(String id) throws RefundRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getFindPermission());
		
		return refundRegistryUtil.getActualRefund(id);
	}

	@Override
	@ActivateRequestContext
	public Refund createRefund(Order order, Map<String, Integer> itens) throws RefundRegistryException, ClientRegistryException, ShippingRegistryException, OrderRegistryException, OrderReportRegistryException, InvoiceRegistryException, ValidationException, PaymentGatewayException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.REFUND_REGISTRY.getCreatePermission());
		
		Refund refund = toRefund(order, itens);
		save(refund);
		return refund;
	}
	
	@Override
	@ActivateRequestContext
	public Refund toRefund(Order order) throws RefundRegistryException, InvalidUnitsOrderRegistryException, ItemNotFoundOrderRegistryException, InvoiceRegistryException {
		
		Order actualOrder           = refundRegistryUtil.getActualOrder(order);
		List<Refund> actualRefunds  = refundRegistryUtil.getActualRefunds(actualOrder);
		List<Invoice> actualInvoice	= refundRegistryUtil.getActualInvoice(actualOrder);
		
		return refundRegistryUtil.toRefund(actualOrder, actualInvoice, actualRefunds);
	}

	private Refund toRefund(Order order, Map<String, Integer> itens) throws RefundRegistryException, InvalidUnitsOrderRegistryException, ItemNotFoundOrderRegistryException {
		
		Order actualOrder = refundRegistryUtil.getActualOrder(order);
		return refundRegistryUtil.toRefund(actualOrder, itens);
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
