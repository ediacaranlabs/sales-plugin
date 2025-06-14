package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.pub.ProductViewerHandler;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ProductViewerRegistry extends PublicBean{

	void registerProductViewerHandler(ProductViewerHandler handler) throws ProductViewerRegistryException;
	
	void unregisterProductViewerHandler(String id) throws ProductViewerRegistryException;

	void registerProductViewerWidget(ProductWidget widget) throws ProductViewerRegistryException;
	
	void unregisterProductViewerWidget(String id) throws ProductViewerRegistryException;

	List<ProductViewerHandler> getProductViewerHandlers();
	
	List<ProductWidget> getProductViewerWidgets();
	
	ProductViewerHandler getProductViewerHandler();
	
}
