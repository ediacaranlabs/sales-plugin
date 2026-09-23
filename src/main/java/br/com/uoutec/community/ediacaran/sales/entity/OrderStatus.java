package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Locale;
import java.util.Set;

import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistryException;

public interface OrderStatus {

	public static final String ORDER = "ORDER";
	
	public static final String PAYMENT = "PAYMENT";
	
	public static final String INVOICES = "INVOICES";

	public static final String SHIPPINGS = "SHIPPINGS";

	public static final String REFUNDS = "REFUNDS";

	public static final String REPORT = "REPORT";
	
	public static final OrderStatus NEW = OrderStatusValue.NEW;
	
	public static final OrderStatus ON_HOLD = OrderStatusValue.ON_HOLD;
	
	public static final OrderStatus PENDING_PAYMENT = OrderStatusValue.PENDING_PAYMENT;
	
	public static final OrderStatus PAYMENT_RECEIVED = OrderStatusValue.PAYMENT_RECEIVED;
	
	public static final OrderStatus ORDER_INVOICED = OrderStatusValue.ORDER_INVOICED;
	
	public static final OrderStatus ORDER_SHIPPED = OrderStatusValue.ORDER_SHIPPED;

	public static final OrderStatus COMPLETE = OrderStatusValue.COMPLETE;
	
	public static final OrderStatus CLOSED = OrderStatusValue.CLOSED;
	
	public static final OrderStatus ARCHIVED = OrderStatusValue.ARCHIVED;
	
	public static final OrderStatus CANCELED = OrderStatusValue.CANCELED;

	public static final OrderStatus PARTIAL_REFUND = OrderStatusValue.PARTIAL_REFUND;
	
	public static final OrderStatus REFUND = OrderStatusValue.REFUND;
	
	String getCode();
	
	String getName(Locale locale);

	boolean isValidNextStatus(OrderStatus newStatus);

	boolean isAllowedCreateInvoice();
	
	boolean isAllowedChangeInvoice();

	boolean isAllowedCreateShipping();

	boolean isAllowedCreateRefund();

	boolean isAllowedChangeRefund();
	
	boolean isAllowedChangeShipping();

	boolean isAllowedCreateOrderReport();
	
	boolean isAllowedChangeOrderReport();

	boolean isClosed();
	
	Set<OrderStatus> getNexStatus();
	
	boolean isValidStatus(OrderStatusRequest request);

	OrderStatus getNextStatus(OrderStatusRequest request);
	
	public static OrderStatus getNextStatus(OrderStatus actualStatus, OrderStatusRequest request) {
		
		Set<OrderStatus> set = actualStatus.getNexStatus();
		
		if(set == null) {
			return null;
		}
		
		for(OrderStatus status: set) {
			if(status.isValidStatus(request)) {
				return status;
			}
		}
		
		return null;
	}
	
	public static OrderStatus toOrderStatus(String value) {
		return  value == null? null : OrderStatusValue.valueOf(value);
	};
	
	public static OrderStatus[] getValues() {
		return  OrderStatusValue.values();
	}
	
	public static interface OrderStatusRequest {
		
		Object getValue(String name);
		
	}

	public class OrderStatusRequestOrder implements OrderStatusRequest{

		private Order order;
		
		public OrderStatusRequestOrder(Order order) {
			this.order = order;
		}
		
		@Override
		public Object getValue(String name) {
			switch (name) {
			case OrderStatus.PAYMENT:
				return order.getPayment();
			case OrderStatus.INVOICES:
				try {
					return order.getInvoices();
				}
				catch (InvoiceRegistryException e) {
					throw new RuntimeException(e);
				}
			case OrderStatus.SHIPPINGS:
				try {
					return order.getShippings();
				}
				catch (ShippingRegistryException e) {
					throw new RuntimeException(e);
				}
			case OrderStatus.REFUNDS:
				try {
					return order.getRefunds();
				}
				catch (RefundRegistryException e) {
					throw new RuntimeException(e);
				}
			case OrderStatus.REPORT:
				try {
					return order.getOrderReport();
				}
				catch (OrderReportRegistryException e) {
					throw new RuntimeException(e);
				}
			case OrderStatus.ORDER:
				return order;
			}
			return null;

		}
		
	}
	
	
}
