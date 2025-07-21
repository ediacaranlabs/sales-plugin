package br.com.uoutec.community.ediacaran.sales;

import java.time.temporal.ChronoUnit;

import br.com.uoutec.community.ediacaran.sales.actions.cart.CreateInvoiceAction;
import br.com.uoutec.community.ediacaran.sales.actions.cart.RegisterPaymntInfoAction;
import br.com.uoutec.community.ediacaran.sales.registry.EmptyInvoiceException;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ActionsPluginInstaller {

	public static final String NEW_ORDER_REGISTERED 	= "new_order_registered";

	public static final String NEW_INVOICE_REGISTERED 	= "new_invoice_registered";

	public static final String NEW_SHIPPING_REGISTERED 	= "new_shipping_registered";
	
	public static final String REGISTER_PAYMENT_INFO 	= "register_payment_info";

	public static final String CREATE_INVOICE 			= "create_invoice";
	
	public ActionsPluginInstaller() {
	}
	
	public void install() throws Throwable {
				
		ActionRegistry actionRegistry = EntityContextPlugin.getEntity(ActionRegistry.class);
		
		actionRegistry.registerAction(NEW_ORDER_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String orderID = request.getParameter("order");
			response.setParameter("order", orderID);
		});

		actionRegistry.registerAction(NEW_INVOICE_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String invoice = request.getParameter("invoice");
			response.setParameter("invoice", invoice);
		});

		actionRegistry.registerAction(NEW_SHIPPING_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String shipping = request.getParameter("shipping");
			response.setParameter("shipping", shipping);
		});
		
		actionRegistry.registerAction(REGISTER_PAYMENT_INFO, 	3, 10, ChronoUnit.SECONDS, EntityContextPlugin.getEntity(RegisterPaymntInfoAction.class));
		actionRegistry.registerAction(CREATE_INVOICE,			3, 10, ChronoUnit.SECONDS, EntityContextPlugin.getEntity(CreateInvoiceAction.class));
		
		actionRegistry.addNextAction(NEW_ORDER_REGISTERED,	REGISTER_PAYMENT_INFO);
		actionRegistry.addNextAction(REGISTER_PAYMENT_INFO,	CREATE_INVOICE);
		actionRegistry.addExceptionAction(CREATE_INVOICE,	EmptyInvoiceException.class, null);
		
	}
	
	public void uninstall() throws Throwable {
		ActionRegistry actionRegistry = EntityContextPlugin.getEntity(ActionRegistry.class);
		actionRegistry.removeAction(NEW_ORDER_REGISTERED);
		actionRegistry.removeAction(NEW_INVOICE_REGISTERED);
		actionRegistry.removeAction(NEW_SHIPPING_REGISTERED);
		actionRegistry.removeAction(REGISTER_PAYMENT_INFO);
		actionRegistry.removeAction(CREATE_INVOICE);
	}
	
}
