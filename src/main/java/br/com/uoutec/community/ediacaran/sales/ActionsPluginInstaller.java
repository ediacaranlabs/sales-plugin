package br.com.uoutec.community.ediacaran.sales;

import java.time.temporal.ChronoUnit;

import br.com.uoutec.community.ediacaran.sales.actions.cart.AsyncConfirmRefundAction;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ActionsPluginInstaller {

	public static final String NEW_ORDER_REGISTERED 		= "new_order_registered";

	public static final String UPDATE_ORDER_REGISTERED 		= "update_order_registered";
	
	public static final String NEW_REFUND_REGISTERED 		= "new_refund_registered";

	public static final String UPDATE_REFUND_REGISTERED 	= "update_refund_registered";
	
	public static final String NEW_INVOICE_REGISTERED 		= "new_invoice_registered";

	public static final String UPDATE_INVOICE_REGISTERED 	= "update_invoice_registered";
	
	public static final String NEW_SHIPPING_REGISTERED 		= "new_shipping_registered";

	public static final String UPDATE_SHIPPING_REGISTERED 	= "update_shipping_registered";
	
	public static final String NEW_ORDER_REPORT_REGISTERED	= "new_order_report_registered";

	public static final String UPDATE_ORDER_REPORT_REGISTERED = "update_order_report_registered";
	
	//public static final String REGISTER_PAYMENT_INFO 		= "register_payment_info";

	public static final String ASYNC_CONFIRM_REFUND_ACTION	= "async_confirm_refund_action";
	
	//public static final String CREATE_INVOICE 				= "create_invoice";
	
	public ActionsPluginInstaller() {
	}
	
	public void install() throws Throwable {
				
		ActionRegistry actionRegistry = EntityContextPlugin.getEntity(ActionRegistry.class);
		
		actionRegistry.registerAction(NEW_ORDER_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String orderID = (String)request.getParameter("order");
			response.setParameter("order", orderID);
		});

		actionRegistry.registerAction(UPDATE_ORDER_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String orderID = (String)request.getParameter("order");
			response.setParameter("order", orderID);
		});
		
		actionRegistry.registerAction(NEW_INVOICE_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String invoice = (String)request.getParameter("invoice");
			response.setParameter("invoice", invoice);
		});

		actionRegistry.registerAction(UPDATE_INVOICE_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String invoice = (String)request.getParameter("invoice");
			response.setParameter("invoice", invoice);
		});
		
		actionRegistry.registerAction(NEW_SHIPPING_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String shipping = (String)request.getParameter("shipping");
			response.setParameter("shipping", shipping);
		});

		actionRegistry.registerAction(UPDATE_SHIPPING_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String shipping = (String)request.getParameter("shipping");
			response.setParameter("shipping", shipping);
		});

		actionRegistry.registerAction(NEW_REFUND_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String refund = (String)request.getParameter("refund");
			response.setParameter("refund", refund);
		});

		actionRegistry.registerAction(UPDATE_REFUND_REGISTERED, 	3, 10, ChronoUnit.SECONDS, (request,response)->{
			String refund = (String)request.getParameter("refund");
			response.setParameter("refund", refund);
		});
		
		actionRegistry.registerAction(NEW_ORDER_REPORT_REGISTERED, 3, 10, ChronoUnit.SECONDS, (request,response)->{
			String orderReport = (String)request.getParameter("orderReport");
			response.setParameter("orderReport", orderReport);
		});
		
		actionRegistry.registerAction(UPDATE_ORDER_REPORT_REGISTERED, 3, 10, ChronoUnit.SECONDS, (request,response)->{
			String orderReport = (String)request.getParameter("orderReport");
			response.setParameter("orderReport", orderReport);
		});
		
		//actionRegistry.registerAction(REGISTER_PAYMENT_INFO, 		3, 10, ChronoUnit.SECONDS, EntityContextPlugin.getEntity(RegisterPaymntInfoAction.class));
		//actionRegistry.registerAction(CREATE_INVOICE,				3, 10, ChronoUnit.SECONDS, EntityContextPlugin.getEntity(CreateInvoiceAction.class));
		actionRegistry.registerAction(ASYNC_CONFIRM_REFUND_ACTION,	3, 10, ChronoUnit.SECONDS, EntityContextPlugin.getEntity(AsyncConfirmRefundAction.class));
		
		//actionRegistry.executeAfter(NEW_ORDER_REGISTERED,	REGISTER_PAYMENT_INFO);
		//actionRegistry.executeAfter(REGISTER_PAYMENT_INFO,	CREATE_INVOICE);
		//actionRegistry.addExceptionAction(CREATE_INVOICE,	EmptyInvoiceException.class, null);
		
	}
	
	public void uninstall() throws Throwable {
		ActionRegistry actionRegistry = EntityContextPlugin.getEntity(ActionRegistry.class);
		actionRegistry.removeAction(NEW_ORDER_REGISTERED);
		actionRegistry.removeAction(NEW_INVOICE_REGISTERED);
		actionRegistry.removeAction(NEW_SHIPPING_REGISTERED);
		actionRegistry.removeAction(NEW_ORDER_REPORT_REGISTERED);
		actionRegistry.removeAction(ASYNC_CONFIRM_REFUND_ACTION);
		//actionRegistry.removeAction(CREATE_INVOICE);
		//actionRegistry.removeAction(REGISTER_PAYMENT_INFO);
	}
	
}
