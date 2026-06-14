package br.com.uoutec.community.ediacaran.sales.actions.cart;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistry;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutor;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequest;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorResponse;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class RegisterRefundInfoAction implements ActionExecutor, PublicBean{

	@Inject
	private RefundRegistry refundRegistry;
	
	@Override
	@Transactional(rollbackOn = Throwable.class)
	@ActivateRequestContext
	public void execute(ActionExecutorRequest request, ActionExecutorResponse response) throws Throwable {
		ContextSystemSecurityCheck.doPrivileged(()->{
			localExecute(request, response);
			return null;
		});
	}
	
	private void localExecute(ActionExecutorRequest request, ActionExecutorResponse response) throws Throwable {
		
		String id = (String)request.getParameter("refund");
		
		Refund refund = refundRegistry.findRefundById(id);
		
		if(refund.isCompleted()) {
			response.setFinished(true);
			return;
		}
		
		refundRegistry.confirmRefund(refund);
		
		response.setParameter("refund", id);
		
	}

}
