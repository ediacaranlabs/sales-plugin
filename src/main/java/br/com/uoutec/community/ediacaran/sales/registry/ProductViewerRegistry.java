package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ProductViewerRegistry extends PublicBean{

	void registerProductViewerWidget(ProductWidget widget) throws ProductViewerRegistryException;
	
	void unregisterProductViewerWidget(String id) throws ProductViewerRegistryException;
	
	List<ProductWidget> getProductViewerWidgets();
	
}
