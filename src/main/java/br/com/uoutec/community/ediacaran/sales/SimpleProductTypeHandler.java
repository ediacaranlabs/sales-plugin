package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class SimpleProductTypeHandler 
	extends AbstractProductTypeHandler {

	@Override
	public boolean isSupportShipping(ProductRequest productRequest) {
		return true;
	}

	@Override
	public void registryItem(SystemUser user, Order order, ProductRequest productRequest)
			throws ProductTypeHandlerException {
	}

	@Override
	public void removeItem(SystemUser user, Order order, ProductRequest productRequest)
			throws ProductTypeHandlerException {
	}

}
