package br.com.uoutec.community.ediacaran.sales.actions.cart;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutor;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequest;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorResponse;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class CreateInvoiceAction implements ActionExecutor, PublicBean{

	@Inject
	private OrderRegistry orderRegistry;
	
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
		
		Order order = orderRegistry.findById(orderID);
		orderRegistry.createInvoice(order, null, "The invoice was created automatically!");
		
		response.setParameter("order", orderID);
		
	}

}
