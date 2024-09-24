package br.com.uoutec.community.ediacaran.junit.sales;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Cart;
import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class ProductTypeHandlerMock implements ProductTypeHandler {

	private AtomicInteger index;
	
	private ConcurrentMap<String, ProductRequest> requests;
	
	public ProductTypeHandlerMock() {
		this.requests = new ConcurrentHashMap<>();
		this.index = new AtomicInteger();
	}
	
	@Override
	public void addItem(Cart cart, ItensCollection itens, ProductRequest productRequest)
			throws MaxItensException, ProductTypeHandlerException {
		
		productRequest.setSerial("PR" + index.incrementAndGet() + "-" + productRequest.getProduct().getName().toUpperCase());
		itens.add(productRequest);
		
		ProductRequest cprc = cart.get(productRequest.getSerial());
		
		if(cprc == null){
			itens.add(productRequest);
		}
		
	}

	@Override
	public void updateQty(Cart cart, ItensCollection itens, ProductRequest productRequest, int qty)
			throws MaxItensException, ProductTypeHandlerException {
		
		if(qty > 1 && (productRequest.getCost() == null || productRequest.getCost().doubleValue() <= 0) ){
			itens.setQty(productRequest, 1);
		}
		else {
			itens.setQty(productRequest, qty);
		}
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

	@Override
	public void registryItem(SystemUser user, Order order, ProductRequest productRequest)
			throws ProductTypeHandlerException {
		requests.put(productRequest.getSerial(), productRequest);
	}

	@Override
	public void revertRefoundItem(SystemUser user, Order order, ProductRequest productRequest)
			throws ProductTypeHandlerException {

		requests.put(productRequest.getSerial(), productRequest);
	}

	@Override
	public void refoundItem(SystemUser user, Order order, ProductRequest productRequest)
			throws ProductTypeHandlerException {
		
		requests.remove(productRequest.getSerial());
		
	}

	public ConcurrentMap<String, ProductRequest> getRequests() {
		return requests;
	}
	

}
