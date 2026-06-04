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
		@SuppressWarnings("unchecked")
		public boolean isValidStatus(OrderStatusRequest request) {
			try {
				Collection<Invoice> invoices = (Collection<Invoice>)request.getValue(OrderStatus.INVOICES);
				
				if(invoices != null) {
					for(Invoice i: invoices) {
						if(i.getCancelDate() == null) {
							return false;
						}
					}
				}
				
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
			
			Collection<Invoice> invoices   = (Collection<Invoice>)request.getValue(OrderStatus.INVOICES);
			Collection<Refund> refunds     = (Collection<Refund>)request.getValue(OrderStatus.REFUNDS);
			List<Invoice> activeInvoices   = new ArrayList<>();
			Order order                    = (Order)request.getValue(OrderStatus.ORDER);
			
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			invoices.stream()
				.filter((e)->e.getCancelDate() == null)
				.forEach((e)->{
					activeInvoices.add(e);
					ProductRequestUtil.subUnits(map, e.getItens());
				});
			
			ProductRequestUtil.removeEmptyUnits(map);
			
			return map.isEmpty() && !activeInvoices.isEmpty();
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
			List<Shipping> activeShippings  = new ArrayList<>();
			Order order                     = (Order)request.getValue(OrderStatus.ORDER);
			
			Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
			
			refunds.stream()
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
			
			shippings.stream()
				.filter((e)->e.isCompleted())
				.forEach((e)->{
					activeShippings.add(e);
					ProductRequestUtil.subUnits(map, e.getProducts());
				});
			
			ProductRequestUtil.removeEmptyUnits(map);
			
			return map.isEmpty() && !activeShippings.isEmpty();
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
				.filter((e)->e.isCompleted())
				.forEach((e)->{
					ProductRequestUtil.subUnits(map, e.getProducts());
				});
			
			shippings.stream()
				.filter((e)->e.isCompleted())
				.forEach((e)->{
					ProductRequestUtil.subUnits(map, e.getProducts());
				});
			
			ProductRequestUtil.removeEmptyUnits(map);
			
			return map.isEmpty();
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
			Order order = (Order)request.getValue(OrderStatus.ORDER);
			Payment payment = order.getPayment();
			return (payment.getStatus() == PaymentStatus.NEW || payment.getStatus() == PaymentStatus.ON_HOLD || payment.getStatus() == PaymentStatus.REFOUND) &&
					order.getDaysAfterCreated() > 5;
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
