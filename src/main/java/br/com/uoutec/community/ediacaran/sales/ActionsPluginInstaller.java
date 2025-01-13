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
		actionRegistry.registerAction(OrderStatus.PENDING_PAYMENT.name().toLowerCase(), 3, 10, ChronoUnit.SECONDS, new PendingPaymentAction());
	}
	
	public void uninstall() throws Throwable {
	}
	
}
