package br.com.uoutec.community.ediacaran.sales;

import java.time.temporal.ChronoUnit;

import br.com.uoutec.community.ediacaran.sales.actions.cart.OrderInvoicedAction;
import br.com.uoutec.community.ediacaran.sales.actions.cart.PaymentReceivedAction;
import br.com.uoutec.community.ediacaran.sales.actions.cart.PendingPaymentAction;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.system.actions.ActionFlowBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ActionsPluginInstaller {

	public ActionsPluginInstaller() {
	}
	
	public void install() throws Throwable {
		ActionFlowBuilder actionFlowBuilder = new ActionFlowBuilder();
		
		actionFlowBuilder
			.addNextAction(OrderStatus.PENDING_PAYMENT.name().toLowerCase(), new PendingPaymentAction())
				.setAttemptsBeforeFailure(3)
				.setTimeBeforeTryAgain(10, ChronoUnit.SECONDS)
			.addNextAction(OrderStatus.PAYMENT_RECEIVED.name().toLowerCase(), new PaymentReceivedAction())
				.setAttemptsBeforeFailure(3)
				.setTimeBeforeTryAgain(10, ChronoUnit.SECONDS)
			.addNextAction(OrderStatus.ORDER_INVOICED.name().toLowerCase(), new OrderInvoicedAction())
				.setAttemptsBeforeFailure(3)
				.setTimeBeforeTryAgain(10, ChronoUnit.SECONDS);
				
		ActionRegistry actionRegistry = EntityContextPlugin.getEntity(ActionRegistry.class);
		actionRegistry.registerAction(OrderStatus.PENDING_PAYMENT.name().toLowerCase(), 3, 10, ChronoUnit.SECONDS, new PendingPaymentAction());
	}
	
	public void uninstall() throws Throwable {
	}
	
}
