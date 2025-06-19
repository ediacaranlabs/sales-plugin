package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProductViewerRegistryImp implements ProductViewerRegistry {

	private final ProductViewerRegistryRepository repository;
	
	public ProductViewerRegistryImp() {
		this.repository = null;
	}
	
	@Inject
	public ProductViewerRegistryImp(ProductViewerRegistryRepository repository) {
		this.repository = repository;
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
