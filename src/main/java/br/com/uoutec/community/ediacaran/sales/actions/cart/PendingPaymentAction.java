package br.com.uoutec.community.ediacaran.sales.actions.cart;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutor;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequest;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorResponse;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class PendingPaymentAction implements ActionExecutor, PublicBean{

	@Override
	@Transactional
	@ActivateRequestContext
	public void execute(ActionExecutorRequest request, ActionExecutorResponse response) throws Throwable {
		ContextSystemSecurityCheck.doPrivileged(()->{
			localExecute(request, response);
			return null;
		});
	}
	
	private void localExecute(ActionExecutorRequest request, ActionExecutorResponse response) throws Throwable {
		
		String orderID = request.getParameter("order");
		
		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		
		Order order = orderRegistry.findById(orderID);
		
		if(order.getPayment().getStatus() == PaymentStatus.PENDING_PAYMENT) {
			orderRegistry.registerPayment(order, null, null);
		}
		
	}

}
