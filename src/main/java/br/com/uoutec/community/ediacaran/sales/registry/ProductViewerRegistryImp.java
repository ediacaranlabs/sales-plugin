package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.sales.pub.ProductViewerHandler;
import br.com.uoutec.ediacaran.core.plugins.PluginType;

@Singleton
public class ProductViewerRegistryImp implements ProductViewerRegistry {

	private final ProductViewerRegistryRepository repository;
	
	private final PluginType pluginType;
	
	private volatile ProductViewerHandler productViewerHandler;
	
	private long time;
	
	@Inject
	public ProductViewerRegistryImp(ProductViewerRegistryRepository repository, PluginType pluginType) {
		this.repository = repository;
		this.pluginType = pluginType;
		this.productViewerHandler = null;
		this.time = SystemProperties.currentTimeMillis() + 10000;
	}
	
	@Override
	public void registerProductViewerHandler(ProductViewerHandler handler) throws ProductViewerRegistryException {
		repository.registerProductViewerHandler(handler);
	}

	@Override
	public void unregisterProductViewerHandler(String id) throws ProductViewerRegistryException {
		repository.unregisterProductViewerHandler(id);
	}

	@Override
	public List<ProductViewerHandler> getProductViewerHandlers() {
		return repository.getProductViewerHandlers();
	}

	@Override
	public ProductViewerHandler getProductViewerHandler() {

		long localTime = SystemProperties.currentTimeMillis();
		if(localTime > time || productViewerHandler == null) {
			productViewerHandler = getSystemProductViewerHandler();
			time = SystemProperties.currentTimeMillis() + 10000;
		}
		
		return productViewerHandler;
	}

	public synchronized ProductViewerHandler getSystemProductViewerHandler() {
		
		String viewerId = pluginType.getConfiguration().getString("product_viewer");
		
		if(viewerId == null) {
			return null;
		}
		
		return repository.getProductViewerHandler(viewerId);
	}

	@Override
	public void registerProductViewerWidget(ProductWidget widget) throws ProductViewerRegistryException {
		repository.registerProductViewerWidget(widget);
	}

	@Override
	public void unregisterProductViewerWidget(String id) throws ProductViewerRegistryException {
		repository.unregisterProductViewerWidget(id);
	}

	@Override
	public List<ProductWidget> getProductViewerWidgets() {
		return repository.getProductViewerWidgets();
	}
	
}
