package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Locale;

public interface OrderStatus {

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

	public static final OrderStatus REFOUND = OrderStatusValue.REFOUND;
	
	String getName(Locale locale);

	boolean isValidNextStatus(OrderStatus newStatus);

	boolean isAllowedCreateInvoice();
	
	boolean isAllowedChangeInvoice();

	boolean isAllowedCreateShipping();
	
	boolean isAllowedChangeShipping();

	boolean isAllowedCreateOrderReport();
	
	boolean isAllowedChangeOrderReport();

	boolean isClosed();
	
	public static OrderStatus[] getValues() {
		return  OrderStatusValue.values();
	};
	
}
