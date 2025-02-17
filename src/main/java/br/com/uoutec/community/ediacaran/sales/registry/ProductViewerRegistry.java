package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.pub.ProductViewerHandler;

public interface ProductViewerRegistry {

	void registerProductViewerHandler(ProductViewerHandler handler) throws ProductViewerRegistryException;
	
	void unregisterProductViewerHandler(String id) throws ProductViewerRegistryException;
	
	List<ProductViewerHandler> getProductViewerHandlers();
	
	ProductViewerHandler getProductViewerHandler();
	
}
