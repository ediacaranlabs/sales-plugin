package br.com.uoutec.community.ediacaran.sales.actions.cart;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutor;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequest;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorResponse;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class PaymentReceivedAction implements ActionExecutor{

	@Override
	public void execute(ActionExecutorRequest request, ActionExecutorResponse response) throws Throwable {
		
		String orderID = request.getParameter("order");
		
		InvoiceRegistry invoiceRegistry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		OrderRegistry orderRegistry     = EntityContextPlugin.getEntity(OrderRegistry.class);
		
		Order order = orderRegistry.findById(orderID);
		
		if(order.getStatus() == OrderStatus.PAYMENT_RECEIVED) {
			Invoice invoice = invoiceRegistry.toInvoice(order);
			invoiceRegistry.registerInvoice(invoice);
		}
		
	}

}
