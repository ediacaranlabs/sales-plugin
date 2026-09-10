package br.com.uoutec.community.ediacaran.sales.actions.cart;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutor;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequest;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorResponse;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class CreateInvoiceAction implements ActionExecutor, PublicBean {

	@Inject
	private OrderRegistry orderRegistry;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
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
		
		String orderID = (String)request.getParameter("order");
		Order order = orderRegistry.findById(orderID);
		
		Map<String, Integer> invoices = new HashMap<>();
		
		productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		
		for(ProductRequest pp: order.getItens()) {
			
			ProductType productType = productTypeRegistry.getProductType(pp.getProduct().getProductType());
			if(!productType.getHandler().isSupportShipping(pp)) {
				invoices.put(pp.getSerial(), pp.getUnits());
			}
			
		}
		
		if(!invoices.isEmpty()) {
			orderRegistry.createInvoice(order, invoices, "The invoice was created automatically!");
		}
		
		response.setParameter("order", orderID);
		
	}

}
