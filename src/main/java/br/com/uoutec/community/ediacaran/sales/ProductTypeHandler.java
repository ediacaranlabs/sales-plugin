package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public interface ProductTypeHandler {

	/* cart */
	void addItem(Cart cart, ItensCollection itens, ProductRequest productRequest) 
			throws MaxItensException, ProductTypeHandlerException, ProductTypeRegistryException;

	void updateQty(Cart cart, ItensCollection itens, ProductRequest productRequest, int qty) 
			throws MaxItensException, ProductTypeHandlerException, ProductTypeRegistryException;
	
	void removeItem(Cart cart, ItensCollection itens, ProductRequest productRequest)
		throws ProductTypeHandlerException;

	boolean isAvailability(SystemUser user, Cart cart, ItensCollection itens, ProductRequest productRequest) throws
		ProductTypeHandlerException;
	
	String getSerial(ProductRequest productRequest);
	
	String getShortDescription(ProductRequest productRequest) throws ProductTypeHandlerException;

	String getDescription(ProductRequest productRequest) throws ProductTypeHandlerException;
	
	void preRegisterOrder(SystemUser user, Cart cart, ProductRequest productRequest) throws
		ProductTypeHandlerException;
	
	void postRegisterOrder(SystemUser user, Cart cart, ProductRequest productRequest) throws
		ProductTypeHandlerException;

	/* registry */
	
	void registryItem(SystemUser user, Order order, ProductRequest productRequest)
			throws ProductTypeHandlerException;

	void removeItem(SystemUser user, Order order, ProductRequest productRequest)
			throws ProductTypeHandlerException;
	
	
}
