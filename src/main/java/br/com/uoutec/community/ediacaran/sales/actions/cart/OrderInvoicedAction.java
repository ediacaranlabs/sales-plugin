package br.com.uoutec.community.ediacaran.sales.actions.cart;

import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutor;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequest;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorResponse;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class OrderInvoicedAction implements ActionExecutor{

	@Override
	public void execute(ActionExecutorRequest request, ActionExecutorResponse response) throws Throwable {
		
		String orderID = request.getParameter("order");
		
		OrderRegistry orderRegistry             = EntityContextPlugin.getEntity(OrderRegistry.class);
		ShippingRegistry shippingRegistry       = EntityContextPlugin.getEntity(ShippingRegistry.class);
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		
		Order order = orderRegistry.findById(orderID);
		
		if(order.getStatus() == OrderStatus.ORDER_INVOICED) {
			Shipping shipping = shippingRegistry.toShipping(order);
			List<ProductRequest> actualProducts = shipping.getProducts();
			List<ProductRequest> products = new ArrayList<>(shipping.getProducts());
			
			for(ProductRequest pp: products) {
				ProductType productType = productTypeRegistry.getProductType(pp.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				if(!productTypeHandler.isService(pp)) {
					actualProducts.remove(pp);
				}
			}
			
			if(!actualProducts.isEmpty()) {
				VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
				String serviceShippingName = varParser.getValue("${plugins.ediacaran.sales.electronic_shipping_method}");
				shipping.setShippingType(serviceShippingName);
				shippingRegistry.registerShipping(shipping);
			}
			
		}
		
	}

}
