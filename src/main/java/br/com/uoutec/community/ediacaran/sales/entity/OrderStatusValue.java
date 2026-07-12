package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import br.com.uoutec.community.ediacaran.sales.registry.ProductRequestUtil;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum OrderStatusValue implements OrderStatus {

	NEW(){
		
		@Override
		public boolean isValidStatus(OrderStatusRequest request) {
			return  OrderStatusUtil.isNewOrder((Order)request.getValue(OrderStatus.ORDER));
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	ON_HOLD(){

		@Override
		public boolean isValidStatus(OrderStatusRequest request) {
			return OrderStatusUtil.isHoldOrder((Order)request.getValue(OrderStatus.ORDER), (Payment) request.getValue(OrderStatus.PAYMENT));
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	PENDING_PAYMENT(){

		@Override
		public boolean isValidStatus(OrderStatusRequest request) {
			try {
				return OrderStatusUtil.isPendingPaymentOrder((Order)request.getValue(OrderStatus.ORDER), (Payment)request.getValue(OrderStatus.PAYMENT));
			}
			catch(Throwable ex) {
				return false;
			}
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	PAYMENT_RECEIVED(){

		@Override
		public boolean isValidStatus(OrderStatusRequest request) {
			try {
				return OrderStatusUtil.isPaymentReceivedOrder((Order)request.getValue(OrderStatus.ORDER), (Payment)request.getValue(OrderStatus.PAYMENT));
			}
			catch(Throwable ex) {
				return false;
			}
		}
		
		public boolean isAllowedCreateInvoice() {
			return true;
		}
		
		public boolean isAllowedChangeInvoice() {
			return true;
		}

		@Override
		public boolean isClosed() {
			return false;
		}
		
		@Override
		public boolean isAllowedCreateRefund() {
			return true;
		}

		@Override
		public boolean isAllowedChangeRefund() {
			return true;
		}
		
	},
	
	ORDER_INVOICED(){

		@Override
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			try {
				return OrderStatusUtil.isInvoicedOrder(
						(Order)request.getValue(OrderStatus.ORDER), 
						(Collection<Invoice>)request.getValue(OrderStatus.INVOICES), 
						(Collection<Refund>)request.getValue(OrderStatus.REFUNDS), 
						(Collection<Shipping>)request.getValue(OrderStatus.SHIPPINGS)
				);
			}
			catch(Throwable ex) {
				return false;
			}
		}
		
		public boolean isAllowedCreateShipping() {
			return true;
		}
		
		public boolean isAllowedChangeShipping() {
			return true;
		}

		public boolean isAllowedCreateOrderReport() {
			return true;
		}
		
		public boolean isAllowedChangeOrderReport() {
			return true;
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
		@Override
		public boolean isAllowedCreateRefund() {
			return true;
		}

		@Override
		public boolean isAllowedChangeRefund() {
			return true;
		}
		
	},
	
	ORDER_SHIPPED(){
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			try {
				return OrderStatusUtil.isShippedOrder(
						(Order)request.getValue(OrderStatus.ORDER), 
						(Collection<Refund>)request.getValue(OrderStatus.REFUNDS), 
						(Collection<Shipping>)request.getValue(OrderStatus.SHIPPINGS)
				);
			}
			catch(Throwable ex) {
				return false;
			}
		}
		
		public boolean isAllowedCreateOrderReport() {
			return true;
		}
		
		public boolean isAllowedChangeOrderReport() {
			return true;
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},

	COMPLETE(){
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			try {
				return OrderStatusUtil.isCompletedOrder(
						(Order)request.getValue(OrderStatus.ORDER), 
						(Collection<Refund>)request.getValue(OrderStatus.REFUNDS), 
						(Collection<Shipping>)request.getValue(OrderStatus.SHIPPINGS), 
						(Collection<OrderReport>)request.getValue(OrderStatus.REPORT)
				);
			}
			catch(Throwable ex) {
				return false;
			}
		}
		
	},
	
	CLOSED(){
		public boolean isValidStatus(OrderStatusRequest request) {
			Order order = (Order)request.getValue(OrderStatus.ORDER);
			return order.getDaysAfterCreated() > 30;
		}
	},
	
	ARCHIVED(){
		public boolean isValidStatus(OrderStatusRequest request) {
			Order order = (Order)request.getValue(OrderStatus.ORDER);
			return order.getDaysAfterCreated() > 120;
		}
	},
	
	CANCELED(){
		public boolean isValidStatus(OrderStatusRequest request) {
			Order order = (Order)request.getValue(OrderStatus.ORDER);
			Payment payment = order.getPayment();
			return (payment.getStatus() == PaymentStatus.NEW || payment.getStatus() == PaymentStatus.ON_HOLD) &&
					order.getDaysAfterCreated() > 5;
		}
	},

	PARTIAL_REFUND(){
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			Collection<Refund> refunds      = (Collection<Refund>)request.getValue(OrderStatus.REFUNDS);
			List<Refund> activeRefunds      = new ArrayList<>();
			Order order                     = (Order)request.getValue(OrderStatus.ORDER);
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.filter((e)->e.isCompleted())
				.forEach((e)->{
					activeRefunds.add(e);
					ProductRequestUtil.subUnits(map, e.getProducts());
				});
			
			ProductRequestUtil.removeEmptyUnits(map);
			
			return !map.isEmpty() && !activeRefunds.isEmpty();
		}
		
		public boolean isAllowedCreateInvoice() {
			return true;
		}
		
		public boolean isAllowedChangeInvoice() {
			return true;
		}

		@Override
		public boolean isClosed() {
			return false;
		}
		
		@Override
		public boolean isAllowedCreateRefund() {
			return true;
		}

		@Override
		public boolean isAllowedChangeRefund() {
			return true;
		}
		
	},
	
	REFUND(){
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			Collection<Refund> refunds      = (Collection<Refund>)request.getValue(OrderStatus.REFUNDS);
			List<Refund> activeRefunds      = new ArrayList<>();
			Order order                     = (Order)request.getValue(OrderStatus.ORDER);
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.filter((e)->e.isCompleted())
				.forEach((e)->{
					activeRefunds.add(e);
					ProductRequestUtil.subUnits(map, e.getProducts());
				});
			
			ProductRequestUtil.removeEmptyUnits(map);
			
			return map.isEmpty() && !activeRefunds.isEmpty();
		}
		
	};
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(OrderStatusValue.class);
	
	public String getName(Locale locale) {
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		return i18nRegistry
					.getString(
							RESOURCE_BUNDLE,
							name().toLowerCase(), 
							locale
					);		
	}

	public boolean isValidNextStatus(OrderStatusValue newStatus) {
		return StatusCheck.isValid(this, newStatus);
	}
	
	private static class StatusCheck {
		
		private static final Map<OrderStatusValue, Set<OrderStatusValue>> nextState;
		
		static{
			nextState = new HashMap<OrderStatusValue, Set<OrderStatusValue>>();
			
			nextState.put(OrderStatusValue.NEW, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.PENDING_PAYMENT,
							OrderStatusValue.CANCELED,
							OrderStatusValue.ON_HOLD))
				);
			
			nextState.put(OrderStatusValue.ON_HOLD, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.PENDING_PAYMENT,
							OrderStatusValue.PAYMENT_RECEIVED))
				);
			
			nextState.put(OrderStatusValue.PENDING_PAYMENT, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.CANCELED,
							OrderStatusValue.PAYMENT_RECEIVED))
				);

			nextState.put(OrderStatusValue.PAYMENT_RECEIVED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.REFUND,
							OrderStatusValue.PARTIAL_REFUND,
							OrderStatusValue.ORDER_INVOICED,
							OrderStatusValue.ORDER_SHIPPED))
				);

			nextState.put(OrderStatusValue.ORDER_INVOICED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							//OrderStatusValue.PAYMENT_RECEIVED,
							OrderStatusValue.ORDER_SHIPPED,
							OrderStatusValue.COMPLETE))
				);

			nextState.put(OrderStatusValue.ORDER_SHIPPED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							//OrderStatusValue.ORDER_INVOICED,
							OrderStatusValue.COMPLETE))
				);

			nextState.put(OrderStatusValue.PARTIAL_REFUND, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.REFUND,
							OrderStatusValue.ORDER_INVOICED,
							OrderStatusValue.ORDER_SHIPPED))
				);
			
			nextState.put(OrderStatusValue.REFUND, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.CLOSED))
				);
			
			nextState.put(OrderStatusValue.CLOSED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.ARCHIVED))
				);

			nextState.put(OrderStatusValue.COMPLETE, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.ARCHIVED))
				);
			
			nextState.put(OrderStatusValue.CANCELED, 
					new HashSet<OrderStatusValue>(Arrays.asList())
				);
			
		}
		
		public static boolean isValid(OrderStatusValue currentStatus, OrderStatusValue newStatus){
			if(currentStatus.equals(newStatus)) {
				return true;
			}
			Set<OrderStatusValue> nextStatus = nextState.get(currentStatus);
			return nextStatus != null && nextStatus.contains(newStatus);
		}

		public static Set<OrderStatusValue> getNexStatus(OrderStatusValue currentStatus){
			return nextState.get(currentStatus);
		}
		
	}

	@Override
	public boolean isValidNextStatus(OrderStatus newStatus) {
		return isValidNextStatus((OrderStatusValue)newStatus);
	}

	@Override
	public boolean isClosed() {
		return true;
	}
	
	public boolean isAllowedCreateInvoice() {
		return false;
	}
	
	public boolean isAllowedChangeInvoice() {
		return false;
	}

	public boolean isAllowedCreateShipping() {
		return false;
	}
	
	public boolean isAllowedChangeShipping() {
		return false;
	}

	public boolean isAllowedCreateOrderReport() {
		return false;
	}
	
	public boolean isAllowedChangeOrderReport() {
		return false;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public boolean isAllowedCreateRefund() {
		return false;
	}

	@Override
	public boolean isAllowedChangeRefund() {
		return false;
	}

	@Override
	public Set<OrderStatus> getNexStatus() {
		Set<OrderStatusValue> set = StatusCheck.getNexStatus(this);
		return set == null? null : new HashSet<>(set);
	}

	@Override
	public boolean isValidStatus(OrderStatusRequest request) {
		return false;
	}

	@Override
	public OrderStatus getNextStatus(OrderStatusRequest request) {
		
		OrderStatus status = null;
		OrderStatus current = NEW;
		
		while(current != null) {
			status = current;
			current = OrderStatus.getNextStatus(current, request);
		}
		
		return status;
		//return OrderStatus.getNextStatus(this, request);
	}
	
}
