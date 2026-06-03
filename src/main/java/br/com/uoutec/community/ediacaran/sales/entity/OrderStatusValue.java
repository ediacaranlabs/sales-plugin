package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.uoutec.community.ediacaran.sales.registry.ProductRequestUtil;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum OrderStatusValue implements OrderStatus {

	NEW(){
		
		@Override
		public boolean isValidStatus(OrderStatusRequest request) {
			return ((Order)request.getValue(OrderStatus.ORDER)).getId() == null;
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	ON_HOLD(){

		@Override
		public boolean isValidStatus(OrderStatusRequest request) {
			return ((Order)request.getValue(OrderStatus.ORDER)).getId() != null && request.getValue(OrderStatus.PAYMENT) == null;
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
				Payment payment = (Payment)request.getValue(OrderStatus.PAYMENT);
				return payment != null && OrderRegistryUtil.toOrderStatus(payment.getStatus()) == OrderStatus.PENDING_PAYMENT;
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
				Payment payment = (Payment)request.getValue(OrderStatus.PAYMENT);
				return payment != null && OrderRegistryUtil.toOrderStatus(payment.getStatus()) == OrderStatus.PAYMENT_RECEIVED;
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
			
			Collection<Invoice> invoices    = (Collection<Invoice>)request.getValue(OrderStatus.INVOICES);
			Collection<Refund> refunds      = (Collection<Refund>)request.getValue(OrderStatus.REFUNDS);
			Order order                     = (Order)request.getValue(OrderStatus.ORDER);
			
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			invoices.stream()
				.filter((e)->e.getCancelDate() == null)
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getItens());});
			
			for(ProductRequest pr: order.getItens()) {
				
				ProductRequest tpr = map.get(pr.getSerial());

				if(tpr.getUnits() < 0 || tpr.getUnits() > 0) {
					return false;
				}
				
			}
			
			return true;
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
			
			Collection<Shipping> shippings  = (Collection<Shipping>)request.getValue(OrderStatus.SHIPPINGS);
			Collection<Refund> refunds      = (Collection<Refund>)request.getValue(OrderStatus.REFUNDS);
			Order order                     = (Order)request.getValue(OrderStatus.ORDER);
			
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			shippings.stream()
				.filter((e)->!e.isCanceled())
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			for(ProductRequest pr: order.getItens()) {
				
				ProductRequest tpr = map.get(pr.getSerial());

				if(tpr.getUnits() < 0 || tpr.getUnits() > 0) {
					return false;
				}
				
			}
			
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
		
	},

	COMPLETE(){
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			
			Collection<Shipping> shippings  = (Collection<Shipping>)request.getValue(OrderStatus.SHIPPINGS);
			Collection<Refund> refunds      = (Collection<Refund>)request.getValue(OrderStatus.REFUNDS);
			Collection<OrderReport> reports = (Collection<OrderReport>)request.getValue(OrderStatus.REPORT);
			Order order                     = (Order)request.getValue(OrderStatus.ORDER);
			
			
			Collection<OrderReport>  openReports = reports.stream()
				.filter((e)->!e.isClosed())
				.collect(Collectors.toList());
			
			if(!openReports.isEmpty()) {
				return false;
			}
			
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			shippings.stream()
				.filter((e)->e.isCompleted())
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			for(ProductRequest pr: order.getItens()) {
				
				ProductRequest tpr = map.get(pr.getSerial());

				if(tpr.getUnits() < 0 || tpr.getUnits() > 0) {
					return false;
				}
				
			}
			
			return true;
		}
		
	},
	
	CLOSED(){
		public boolean isValidStatus(OrderStatusRequest request) {
			return true;
		}
	},
	
	ARCHIVED(){
		public boolean isValidStatus(OrderStatusRequest request) {
			return true;
		}
	},
	
	CANCELED(){
		public boolean isValidStatus(OrderStatusRequest request) {
			return true;
		}
	},

	REFUND(){
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			Collection<Refund> refunds      = (Collection<Refund>)request.getValue(OrderStatus.REFUNDS);
			Order order                     = (Order)request.getValue(OrderStatus.ORDER);
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			for(ProductRequest pr: order.getItens()) {
				
				ProductRequest tpr = map.get(pr.getSerial());

				if(tpr.getUnits() < 0 || tpr.getUnits() > 0) {
					return false;
				}
				
			}
			
			return true;
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
				new HashSet<OrderStatusValue>(Arrays.asList(OrderStatusValue.values()))
			);
			
			nextState.put(OrderStatusValue.PENDING_PAYMENT, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.CANCELED,
							OrderStatusValue.PAYMENT_RECEIVED))
				);

			nextState.put(OrderStatusValue.PAYMENT_RECEIVED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.REFUND,
							OrderStatusValue.ORDER_INVOICED))
				);

			nextState.put(OrderStatusValue.ORDER_INVOICED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.PAYMENT_RECEIVED,
							OrderStatusValue.ORDER_SHIPPED,
							OrderStatusValue.COMPLETE))
				);

			nextState.put(OrderStatusValue.ORDER_SHIPPED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.ORDER_INVOICED,
							OrderStatusValue.COMPLETE))
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
		return new HashSet<>(set);
	}

	@Override
	public boolean isValidStatus(OrderStatusRequest request) {
		return false;
	}
	
}
