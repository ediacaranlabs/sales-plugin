package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import br.com.uoutec.community.ediacaran.sales.ActionsPluginInstaller;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.payment.RefundRequest;
import br.com.uoutec.community.ediacaran.sales.persistence.RefundEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.RefundIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequestBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;

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
	private RefundIndexEntityAccess indexEntityAccess;
	
	@Inject
	private ShippingRegistry shippingRegistry;

	@Inject
	private InvoiceRegistry invoiceRegistry;
	
	@Inject
	private ActionRegistry actionRegistry;

	@Inject
	private ClientRegistry clientRegistry;
	
	@Inject
	private OrderReportRegistry orderReportRegistry;
	
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
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

	public void delete(Refund refund, Order order) throws RefundRegistryException {
		try {
			entityAccess.delete(refund);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new RefundRegistryException(
				"refund error: " + order.getId(), e);
		}
	}
	
	
	public void updateIndex(Refund refund, Order order) throws RefundRegistryException {
		try {
			Refund index = indexEntityAccess.findById(refund.getId());
			if(index == null) {
				indexEntityAccess.save(refund);
			}
			else {
				indexEntityAccess.update(refund);
			}

			entityAccess.flush();
		}
		catch(Throwable e){
			throw new RefundRegistryException(
				"refund index error: " + order.getId(), e);
		}
	}
	
	public RefundResultSearch search(RefundSearch value) throws RefundRegistryException {
		try{
			int page = value.getPage() == null? 1 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<Refund> list = indexEntityAccess.search(value, firstResult, maxResults);
			List<Refund> itens = new ArrayList<>();
			
			for(Refund e: list) {
				e = entityAccess.findById(e.getId());
				e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
				itens.add(e);
			}
			
			return new RefundResultSearch(itens.size() > maxItens, -1, page, itens.size() > maxItens? itens.subList(0, maxItens -1) : itens);
		}
		catch(Throwable e){
			throw new RefundRegistryException(e);
		}
		
	}
	
	public void setSaveProperties(Refund entity) throws ValidationException {
		entity.setDate(LocalDateTime.now());
		entity.setRefundDate(null);
	}
	
	public void checkEntityToSave(Refund entity) throws ValidationException {
		ValidatorBean.validate(entity, saveValidations);
	}

	public void checkEntityToUpdate(Refund entity) throws ValidationException {
		ValidatorBean.validate(entity, updateValidations);
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

	public Order getActualOrder(Order entity) throws RefundRegistryException {
		
		Order order;
		try {
			order = orderRegistry.findById(entity.getId());
		}
		catch (OrderRegistryException e) {
			throw new RefundRegistryException(e);
		}
		
		if(order == null) {
			throw new RefundRegistryException("order not found #" + entity.getId());
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

	public Refund getActualRefund(Refund refund) throws RefundRegistryException {
		try {
			return entityAccess.findById(refund.getId());
		}
		catch(Throwable ex) {
			throw new RefundRegistryException(ex);
		}
	}

	public Refund getActualRefund(String id) throws RefundRegistryException {
		try {
			return entityAccess.findById(id);
		}
		catch(Throwable ex) {
			throw new RefundRegistryException(ex);
		}
	}
	
	public List<Refund> getActualRefunds(Order order) throws RefundRegistryException {
		try {
			return entityAccess.findByOrder(order.getId());
		}
		catch(Throwable ex) {
			throw new RefundRegistryException(ex);
		}
	}

	public PaymentGateway getPaymentGateway(Refund refund) throws RefundRegistryException {
		PaymentGateway paymentGateway = paymentGatewayRegistry.getPaymentGateway(refund.getRefundType());
		
		if(paymentGateway == null) {
			throw new RefundRegistryException("payment gateway not found: " + refund.getRefundType());
		}
		
		return paymentGateway;
	}
	
	public List<Shipping> getActualShipping(Order order) throws ShippingRegistryException {
		return shippingRegistry.findByOrder(order.getId());
	}

	public List<Invoice> getActualInvoice(Order order) throws InvoiceRegistryException {
		return invoiceRegistry.findByOrder(order.getId());
	}
	
	public void checkAllowedCreateRefund(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedCreateRefund()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}

	public void checkHasShippedProducts(Refund entity, List<Shipping> actualShiping) throws RefundRegistryException {
		
		Map<String, ProductRequest> map = toMap(entity.getProducts());

		for(Shipping e: actualShiping) {
			
			if(e.isCanceled()) {
				continue;
			}
			
			for(ProductRequest pr: e.getProducts()) {
				if(map.containsKey(pr.getSerial())) {
					throw new RefundRegistryException("product shipped: " + pr.getSerial());
				}
			}
			
		}
	}

	public void checkHasInvoicedProduct(Refund entity, List<Invoice> actualInvoice) throws RefundRegistryException {
		
		Map<String, ProductRequest> map = toMap(entity.getProducts());

		for(Invoice e: actualInvoice) {
			
			if(e.getCancelDate() != null) {
				continue;
			}
			
			for(ProductRequest pr: e.getItens()) {
				if(map.containsKey(pr.getSerial())) {
					throw new RefundRegistryException("product shipped: " + pr.getSerial());
				}
			}
			
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

		checkIsCompletedRefund(order, actualResund);
		checkUnits(order, actualResund, refund, shippingList);		
		
	}

	public void refoundProducts(Order order, Refund refund, boolean partialRefund,  List<ProductRequest> itens, PaymentGateway paymentGateway) throws PaymentGatewayException {
		paymentGateway.refund(new RefundRequest(order, order.getClient(), order.getPayment(), refund, partialRefund, itens));
	}
	
	public void checkUnits(Order order, List<Refund> actualRefunds, Refund refund, Collection<Shipping> shippingList) throws InvalidUnitsOrderRegistryException, ItemNotFoundOrderRegistryException {

		Map<String, ProductRequest> map = toMap(order.getItens());
		
		removeAllShippedItens(shippingList, map);
		removeAllRefundItens(actualRefunds, refund, map);

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
	
	public void checkIsCompletedRefund(Order order, Collection<Refund> refundList) throws CompletedShippingRegistryException, InvalidUnitsOrderRegistryException {
		
		if(isCompletedRefund(order, refundList)) {
			throw new CompletedShippingRegistryException();
		}
		
	}

	public boolean isCompletedRefund(Order order, Collection<Refund> refundList) throws InvalidUnitsOrderRegistryException {
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 
		 removeConfirmedRefundItens(refundList, null, map);
		
		 return !isExistsItens(map);
	}

	/*
	public boolean isExistsItens(Order order, Collection<Refund> refundList, Collection<Shipping> shippingList) throws InvalidUnitsOrderRegistryException {
		
		if(refundList.isEmpty()) {
			return false;
		}
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 
		 removePendingShippedItens(shippingList, map);
		 removeRefundItens(refundList, null, map);
		 
		for(ProductRequest tpr: map.values()) {

			if(tpr.getUnits() > 0) {
				return true;
			}
			
		}

		return false;
	}
	*/

	public boolean isExistsItens(Map<String, ProductRequest> map) throws InvalidUnitsOrderRegistryException {
		
		for(ProductRequest tpr: map.values()) {

			if(tpr.getUnits() > 0) {
				return true;
			}
			
		}

		return false;
	}

	public boolean isPartialRefund(Refund refund, Order order, Collection<Refund> refundList) throws InvalidUnitsOrderRegistryException {
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 
		 removeConfirmedRefundItens(refundList, refund, map);
		 
		for(ProductRequest tpr: map.values()) {

			if(tpr.getUnits() > 0) {
				return false;
			}
			
		}

		return true;
	}
	
	public boolean isCompletedOrder(Order order, Collection<Refund> refundList, Collection<Shipping> shippingList, Collection<OrderReport> orderReportList) throws InvalidUnitsOrderRegistryException {
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 
		 removeCompletedShippingItens(shippingList, map);
		 removeConfirmedRefundItens(refundList, null, map);
		 
		for(ProductRequest tpr: map.values()) {

			if(tpr.getUnits() > 0) {
				return false;
			}
			
		}

		return OrderReportRegistryUtil.isCompletedOrderReport(order, orderReportList);
	}
	
	public Map<String, ProductRequest> toMap(Collection<ProductRequest> values) {
		
		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: values) {
			ProductRequest tpr = new ProductRequest(pr);
			transientItens.put(pr.getSerial(), tpr);
		}
		
		return transientItens;
	}

	public void removeNotSelectedItens(Map<String, ProductRequest> productRequests, Map<String, Integer> selected) throws InvalidUnitsOrderRegistryException {

		Set<String> toRemove = new HashSet<>();
		
		productRequests.keySet().forEach((e)->{
			if(!selected.containsKey(e)) {
				toRemove.add(e);
			}
		});
		
		toRemove.forEach((e)->{
			productRequests.remove(e);	
		});
		
	}
	
	public void removeAllShippedItens(Collection<Shipping> shippingList, Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(shippingList == null || shippingList.isEmpty()) {
			return;
		}
		
		for(Shipping i: shippingList) {
			
			if(i.isCanceled()) {
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

	public void removeCompletedShippingItens(Collection<Shipping> shippingList, Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(shippingList != null) {
			
			for(Shipping i: shippingList) {
				
				if(!i.isCompleted()) {
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
	
	public void removeAllRefundItens(Collection<Refund> refundList, Refund actualRefund, 
			Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(refundList != null) {
			for(Refund i: refundList) {
				
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

	public void removeConfirmedRefundItens(Collection<Refund> refundList, Refund actualRefund, 
			Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(refundList == null || refundList.isEmpty()) {
			return;
		}
		
		for(Refund i: refundList) {
			
			if(!i.isCompleted()) {
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
	
	public void preventChangeRefundSaveSensitiveData(Refund entity, Order order) {
		entity.setDate(LocalDateTime.now());
		entity.setRefundDate(null);
		entity.setId(null);
		entity.setRefundType(order.getPaymentType());
	}

	public void markAsComplete(Order order, Collection<Refund> refundList, Refund refund) throws InvalidUnitsOrderRegistryException, OrderRegistryException {
		
		List<Refund> allRefund = new ArrayList<>(refundList);
		
		if(!allRefund.contains(refund)) {
			allRefund.add(refund);
		}
		
		markAsComplete(order, refundList, orderRegistry); 
	}

	public void markAsComplete(Order order, Collection<Refund> refunds, 
			OrderRegistry orderRegistry) throws InvalidUnitsOrderRegistryException, OrderRegistryException {
		
		if(isCompletedRefund(order, refunds) ) {
			order.getPayment().setStatus(PaymentStatus.REFOUND);
			OrderRegistryUtil.updateStatus(order, OrderStatus.REFUND, orderRegistry);
		}
		
	}

	public void registerEvent(String message, Order order) throws OrderRegistryException {
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

	public void updateOrderStatus(Order actualOrder, Collection<Refund> refundList, Refund refund) throws ShippingRegistryException, OrderReportRegistryException, InvalidUnitsOrderRegistryException, OrderRegistryException {
		
		List<Shipping> shippingList			= shippingRegistry.findByOrder(actualOrder.getId());
		List<OrderReport> orderReportList 	= orderReportRegistry.findByOrder(actualOrder);
		List<Refund> allRefund				= new ArrayList<>(refundList);
		
		if(!allRefund.contains(refund)) {
			allRefund.add(refund);
		}
		
		if(isCompletedOrder(actualOrder, refundList, shippingList, orderReportList)) {
			orderRegistry.updateStatus(actualOrder, OrderStatus.COMPLETE);
		}
		
	}
	
	public void checkAllowedUpdateRefund(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedChangeRefund()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}

	public void preventChangeRefundSensitiveData(Refund refund, Refund actualRefund) {
		
		refund.setClient(actualRefund.getClient());
		refund.setDate(actualRefund.getDate());
		refund.setOrder(actualRefund.getOrder());
		refund.setRefundType(actualRefund.getRefundType());
		refund.setProducts(actualRefund.getProducts());
		
	}

	public void confirmRefund(Refund refund) throws ShippingRegistryException {
		refund.setRefundDate(LocalDateTime.now());
	}

	public Refund toRefund(Order order, Collection<ProductRequest> itens) {
		Refund i = new Refund();
		
		i.setRefundDate(null);
		i.setAddData(new HashMap<>());
		i.setDate(LocalDateTime.now());
		i.setClient(order.getClient());
		i.setProducts(new ArrayList<ProductRequest>(itens));
		i.setRefundType(null);
		i.setOrder(order.getId());
		i.setRefundType(order.getPaymentType());
		
		return i;
	}
	
}
