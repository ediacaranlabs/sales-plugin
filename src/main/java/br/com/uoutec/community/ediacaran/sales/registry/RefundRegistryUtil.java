package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.com.uoutec.community.ediacaran.sales.ActionsPluginInstaller;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.RefundEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequestBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class RefundRegistryUtil {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};

	@Inject
	private OrderRegistry orderRegistry;
	
	@Inject
	private ClientRegistry systemUserRegistry;
	
	@Inject
	private RefundEntityAccess entityAccess;
	
	@Inject
	private ShippingRegistry shippingRegistry;
	
	@Inject
	private ActionRegistry actionRegistry;
	
	public void save(Refund refund, Order order) throws RefundRegistryException {
		try {
			entityAccess.save(refund);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new RefundRegistryException(
				"refund error: " + order.getId(), e);
		}
	}

	public void update(Refund refund, Order order) throws RefundRegistryException {
		try {
			entityAccess.update(refund);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new RefundRegistryException(
				"refund error: " + order.getId(), e);
		}
	}
	
	public void checkEntityToSave(Refund entity) throws ValidationException {
		ValidatorBean.validate(entity, saveValidations);
	}
	
	public void checkOrder(Refund entity) throws RefundRegistryException {
		
		if(entity.getOrder() == null) {
			throw new RefundRegistryException("order is empty");
		}
		
	}
	
	public Order getActualOrder(Refund entity) throws RefundRegistryException {
		
		Order order;
		try {
			order = orderRegistry.findById(entity.getOrder());
		}
		catch (OrderRegistryException e) {
			throw new RefundRegistryException(e);
		}
		
		if(order == null) {
			throw new RefundRegistryException("order not found #" + entity.getOrder());
		}

		return order;
	}
	
	public Client getActualClient(Order order, Client user) throws RefundRegistryException, ClientRegistryException {
		
		Client actuaClient = systemUserRegistry.findClientById(order.getClient().getId());
		
		if(actuaClient.getId() != user.getId()) {
			throw new RefundRegistryException("invalid user: " + actuaClient.getId()+ " != " + user.getId());
		}
		
		return actuaClient;
	}
	
	public List<Refund> getActualRefunds(Order order, Client client) throws EntityAccessException{
		return entityAccess.findByOrder(order.getId(), client);		
	}

	public List<Shipping> getActualShipping(Order order) throws ShippingRegistryException {
		return shippingRegistry.findByOrder(order.getId());
	}
	
	public void checkAllowedCreateRefund(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedCreateRefund()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}
	
	public void checkAllowedRefundStatus(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isValidNextStatus(OrderStatus.REFUND)){
			throw new OrderStatusNotAllowedRegistryException(
					"invalid status #" + order.getId() + ": " + 
					order.getStatus() + " -> " + OrderStatus.REFUND);
		}
	}
	
	public void checkRefund(Order order, List<Refund> actualResund, Refund refund, Collection<Shipping> shippingList) throws InvalidUnitsOrderRegistryException, ItemNotFoundOrderRegistryException, CompletedShippingRegistryException {

		checkIsCompletedRefund(order, actualResund, shippingList);
		checkUnits(order, actualResund, refund, shippingList);		
		
	}

	public void checkUnits(Order order, List<Refund> actualRefunds, Refund refund, Collection<Shipping> shippingList) throws InvalidUnitsOrderRegistryException, ItemNotFoundOrderRegistryException {

		Map<String, ProductRequest> map = toMap(order.getItens());
		
		removeShippedItens(shippingList, map);
		loadRefundsToCalculateUnits(actualRefunds, refund, map);

		for(ProductRequest pr: refund.getProducts()) {
			
			ProductRequest tpr = map.get(pr.getSerial());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(pr.getSerial());
			}

			if(pr.getUnits() <= 0 || pr.getUnits() > tpr.getUnits()) {
				throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
			}
			
			if(tpr.getUnits() - pr.getUnits() < 0) {
				throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
			}
			
		}
		
	}
	
	public void checkIsCompletedRefund(Order order, Collection<Refund> refundList, Collection<Shipping> shippingList) throws CompletedShippingRegistryException, InvalidUnitsOrderRegistryException {
		
		if(isCompletedRefund(order, refundList, shippingList)) {
			throw new CompletedShippingRegistryException();
		}
	}

	public boolean isCompletedRefund(Order order, Collection<Refund> refundList, Collection<Shipping> shippingList) throws InvalidUnitsOrderRegistryException {
		
		if(refundList.isEmpty()) {
			return false;
		}
		
		boolean isComplete = true;
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 
		 removeShippedItens(shippingList, map);
		 loadRefundsToCalculateUnits(refundList, null, map);
		 
		for(ProductRequest tpr: map.values()) {

			if(tpr.getUnits() > 0) {
				isComplete = false;
				break;
			}
			
		}
		
		return isComplete;
	}
	
	public Map<String, ProductRequest> toMap(Collection<ProductRequest> values) {
		
		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: values) {
			ProductRequest tpr = new ProductRequest(pr);
			transientItens.put(pr.getSerial(), tpr);
		}
		
		return transientItens;
	}
	
	public void removeShippedItens(Collection<Shipping> shippingList, Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(shippingList != null) {
			
			for(Shipping i: shippingList) {
				
				if(i.getCancelDate() != null) {
					continue;
				}
				
				for(ProductRequest pr: i.getProducts()) {
					ProductRequest tpr = productRequests.get(pr.getSerial());
					
					tpr.setUnits(tpr.getUnits() - pr.getUnits());
					
					if(tpr.getUnits() < 0) {
						throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
					}

				}
				
			}
			
		}
		
	}
	
	public void loadRefundsToCalculateUnits(Collection<Refund> refundList, Refund actualRefund, 
			Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(refundList != null) {
			for(Refund i: refundList) {
				
				if(i.getRefundDate() != null) {
					continue;
				}
				
				if(actualRefund != null && i.getId().equals(actualRefund.getId())) {
					continue;
				}

				
				for(ProductRequest pr: i.getProducts()) {
					ProductRequest tpr = productRequests.get(pr.getSerial());
					
					tpr.setUnits(tpr.getUnits() - pr.getUnits());
					
					if(tpr.getUnits() < 0) {
						throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
					}
				}
				
			}
		}
		
	}
	
	public void preventChangeRefundSaveSensitiveData(Refund entity) {
		entity.setDate(LocalDateTime.now());
		entity.setRefundDate(null);
	}

	public void markAsComplete(Order order, Collection<Refund> refundList, Refund refund, Collection<Shipping> shippingList, 
			OrderRegistry orderRegistry) throws InvalidUnitsOrderRegistryException, OrderRegistryException {
		
		List<Refund> allRefund = new ArrayList<>(refundList);
		
		if(!allRefund.contains(refund)) {
			allRefund.add(refund);
		}
		
		markAsComplete(order, refundList, shippingList, orderRegistry); 
	}

	@SuppressWarnings("unchecked")
	public void markAsComplete(Order order, Collection<Refund> refunds, Collection<Shipping> shippingList, 
			OrderRegistry orderRegistry) throws InvalidUnitsOrderRegistryException, OrderRegistryException {
		
		if(isCompletedRefund(order, refunds, Collections.EMPTY_LIST)) {
			OrderRegistryUtil.updateStatus(order, OrderStatus.REFUND, orderRegistry);
		}
		
	}

	public void registerEvent(String message, Order order, OrderRegistry orderRegistry) throws OrderRegistryException {
		orderRegistry.registryLog(order, message);
	}

	public void registerNewRefundEvent(Refund refund) {
		actionRegistry.executeAction(
				ActionsPluginInstaller.NEW_REFUND_REGISTERED, 
				ActionExecutorRequestBuilder.builder()
					.withParameter("refund", refund.getId())
				.build()
		);
	}

	public void updateOrderStatus(Order actualOrder, Collection<Refund> refundList, Refund refund, 
			OrderReportRegistry orderReportRegistry, ShippingRegistry shippingRegistry, 
			OrderRegistry orderRegistry) throws ShippingRegistryException, OrderReportRegistryException, InvalidUnitsOrderRegistryException, OrderRegistryException {
		
		List<Shipping> shippingList			= shippingRegistry.findByOrder(actualOrder.getId());
		List<OrderReport> orderReportList 	= orderReportRegistry.findByOrder(actualOrder);
		List<Refund> allRefund				= new ArrayList<>(refundList);
		
		if(!allRefund.contains(refund)) {
			allRefund.add(refund);
		}
		
		if(isCompletedRefund(actualOrder, refundList, shippingList) &&
			OrderReportRegistryUtil.isCompletedOrderReport(actualOrder, orderReportList)) {
			orderRegistry.updateStatus(actualOrder, OrderStatus.COMPLETE);
		}
		
	}
	
}
