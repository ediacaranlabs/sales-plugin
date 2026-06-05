package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Collection;

import br.com.uoutec.community.ediacaran.sales.registry.CompletedInvoiceRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;

public class OrderStatusUtil {

	public static boolean isNewOrder(Order order) {
		return order.getId() == null;
	}

	public static boolean isHoldOrder(Order order, Payment payment) {
		return order.getId() != null && payment != null;
	}

	public static boolean isPendingPaymentOrder(Order order, Payment payment) throws OrderRegistryException {
		return payment != null && OrderRegistryUtil.toOrderStatus(payment.getStatus()) == OrderStatus.PENDING_PAYMENT;
	}

	public static boolean isPaymentReceivedOrder(Order order, Payment payment) throws OrderRegistryException {
		OrderStatus paymentStatus = OrderRegistryUtil.toOrderStatus(payment.getStatus());
		return payment != null && (paymentStatus == OrderStatus.PAYMENT_RECEIVED || paymentStatus == OrderStatus.REFUND);
	}
	
	public static boolean isInvoicedOrder(Order order, Collection<Invoice> invoices, 
			Collection<Refund> refunds, Collection<Shipping> shippings) throws OrderRegistryException, CompletedInvoiceRegistryException {
		return InvoiceRegistryUtil.isCompletedInvoice(order, refunds, invoices);
	}

	public static boolean isShippedOrder(Order order, Collection<Refund> refunds, Collection<Shipping> shippings) throws OrderRegistryException {
		return ShippingRegistryUtil.isCompletedShipping(order, refunds, shippings);
	}

	public static boolean isCompletedOrder(Order order, Collection<Refund> refunds, Collection<Shipping> shippings, Collection<OrderReport> reports) throws OrderRegistryException {
		return OrderRegistryUtil.isCompletedOrder(order, shippings, refunds, reports);
	}
	
}
