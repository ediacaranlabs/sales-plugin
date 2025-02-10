package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public abstract class AbstractProductTypeHandler 
	implements ProductTypeHandler{

	@Override
	public void addItem(Cart cart, ItensCollection itens, ProductRequest productRequest)
			throws MaxItensException, ProductTypeHandlerException, ProductTypeRegistryException {
		
		ProductRequest cprc = cart.get(productRequest.getSerial());
		
		if(cprc == null){
			itens.add(productRequest);
		}
		else {
			cprc.setUnits(cprc.getUnits() + 1);
		}
		
	}

	@Override
	public void updateQty(Cart cart, ItensCollection itens, ProductRequest productRequest, int qty)
			throws MaxItensException, ProductTypeHandlerException, ProductTypeRegistryException {
		itens.setQty(productRequest, qty);
	}

	@Override
	public void removeItem(Cart cart, ItensCollection itens, ProductRequest productRequest)
			throws ProductTypeHandlerException {
		itens.remove(productRequest);
	}

	@Override
	public boolean isAvailability(SystemUser user, Cart cart, ItensCollection itens, ProductRequest productRequest)
			throws ProductTypeHandlerException {
		return true;
	}
	
	@Override
	public String getSerial(ProductRequest productRequest){
		return "PR-" + productRequest.getProduct().getName().replaceAll("\\s+", " ").replaceAll("\\s+", "-").toUpperCase();
	}

	@Override
	public String getShortDescription(ProductRequest productRequest) throws ProductTypeHandlerException {
		return productRequest.getProduct().getName();
	}

	@Override
	public String getDescription(ProductRequest productRequest) throws ProductTypeHandlerException {
		return productRequest.getProduct().getDescription();
	}

	@Override
	public void preRegisterOrder(SystemUser user, Cart cart, ProductRequest productRequest)
			throws ProductTypeHandlerException {
	}

	@Override
	public void postRegisterOrder(SystemUser user, Cart cart, ProductRequest productRequest)
			throws ProductTypeHandlerException {
	}
	
}
