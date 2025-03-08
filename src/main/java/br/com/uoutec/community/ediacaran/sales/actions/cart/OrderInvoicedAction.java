package br.com.uoutec.community.ediacaran.sales.actions.cart;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutor;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequest;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorResponse;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class OrderInvoicedAction implements ActionExecutor, PublicBean{
/*
	@Inject
	private OrderRegistry orderRegistry;
	
	@Inject
	private ShippingRegistry shippingRegistry;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;
*/	
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
		/*
		String orderID = request.getParameter("order");
		Order order = orderRegistry.findById(orderID);
		
		Shipping shipping = shippingRegistry.toShipping(order);
		List<ProductRequest> actualProducts = shipping.getProducts();
		List<ProductRequest> products = new ArrayList<>(shipping.getProducts());
		
		for(ProductRequest pp: products) {
			ProductType productType = productTypeRegistry.getProductType(pp.getProduct().getProductType());
			ProductTypeHandler productTypeHandler = productType.getHandler();
			if(productTypeHandler.isSupportShipping(pp)) {
				actualProducts.remove(pp);
			}
		}
		
		if(!actualProducts.isEmpty()) {
			VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
			String serviceShippingName = varParser.getValue("${plugins.ediacaran.sales.electronic_shipping_method}");
			shipping.setShippingType(serviceShippingName);
			shippingRegistry.registerShipping(shipping);
		}

		response.setParameter("order", orderID);
		*/
	}

}
