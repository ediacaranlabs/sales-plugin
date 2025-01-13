package br.com.uoutec.community.ediacaran.sales;

import java.time.temporal.ChronoUnit;

import br.com.uoutec.community.ediacaran.sales.actions.cart.PendingPaymentAction;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ActionsPluginInstaller {

	public ActionsPluginInstaller() {
	}
	
	public void install() throws Throwable {
				
		ActionRegistry actionRegistry = EntityContextPlugin.getEntity(ActionRegistry.class);
		
		actionRegistry.registerAction(
				OrderStatus.PENDING_PAYMENT.name(), 
				3, 
				10, 
				ChronoUnit.SECONDS, 
				EntityContextPlugin.getEntity(PendingPaymentAction.class)
		);
		
		actionRegistry.addNextAction(OrderStatus.PENDING_PAYMENT.name(), OrderStatus.PAYMENT_RECEIVED.name());
		
		actionRegistry.registerAction(
				OrderStatus.PAYMENT_RECEIVED.name(), 
				3, 
				10, 
				ChronoUnit.SECONDS, 
				EntityContextPlugin.getEntity(PendingPaymentAction.class)
		);
		
		actionRegistry.addNextAction(OrderStatus.PAYMENT_RECEIVED.name(), OrderStatus.ORDER_INVOICED.name());
		
		actionRegistry.registerAction(
				OrderStatus.ORDER_INVOICED.name(), 
				3, 
				10, 
				ChronoUnit.SECONDS, 
				EntityContextPlugin.getEntity(PendingPaymentAction.class)
		);
	}
	
	public void uninstall() throws Throwable {
		ActionRegistry actionRegistry = EntityContextPlugin.getEntity(ActionRegistry.class);
		actionRegistry.removeAction(OrderStatus.PENDING_PAYMENT.name());
		actionRegistry.removeAction(OrderStatus.PAYMENT_RECEIVED.name());
		actionRegistry.removeAction(OrderStatus.ORDER_INVOICED.name());
	}
	
}