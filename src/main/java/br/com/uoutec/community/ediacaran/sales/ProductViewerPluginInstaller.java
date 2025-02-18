package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.pub.DefaultProductViewerHandler;
import br.com.uoutec.community.ediacaran.sales.registry.ProductViewerRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ProductViewerPluginInstaller {

	public void install() throws Throwable {
		ProductViewerRegistry productViewerRegistry = EntityContextPlugin.getEntity(ProductViewerRegistry.class);
		productViewerRegistry.registerProductViewerHandler(new DefaultProductViewerHandler());
	}

	public void uninstall() throws Throwable {
		ProductViewerRegistry productViewerRegistry = EntityContextPlugin.getEntity(ProductViewerRegistry.class);
		productViewerRegistry.unregisterProductViewerHandler("default");
	}	
}
