package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.front.pub.widget.Widget;
import br.com.uoutec.community.ediacaran.sales.pub.ProductViewerHandler;

public interface ProductViewerRegistry {

	void registerProductViewerHandler(ProductViewerHandler handler) throws ProductViewerRegistryException;
	
	void unregisterProductViewerHandler(String id) throws ProductViewerRegistryException;

	void registerProductViewerWidget(Widget widget) throws ProductViewerRegistryException;
	
	void unregisterProductViewerWidget(String id) throws ProductViewerRegistryException;

	List<ProductViewerHandler> getProductViewerHandlers();
	
	List<Widget> getProductViewerWidgets();
	
	ProductViewerHandler getProductViewerHandler();
	
}
