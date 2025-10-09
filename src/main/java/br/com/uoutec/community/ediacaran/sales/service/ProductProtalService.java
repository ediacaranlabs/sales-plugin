package br.com.uoutec.community.ediacaran.sales.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.service.concurrent.SelectProductCategoryPortalExecutorService;
import br.com.uoutec.community.ediacaran.sales.service.concurrent.SelectProductOfferPortalExecutorService;
import br.com.uoutec.community.ediacaran.sales.service.concurrent.SelectProductPortalExecutorService;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class ProductProtalService implements PublicBean {

	@Inject
	private SelectProductCategoryPortalExecutorService selectProductCategoryPortalExecutorService;
	
	@Inject
	private SelectProductOfferPortalExecutorService selectProductOfferPortalExecutorService;
	
	@Inject
	private SelectProductPortalExecutorService selectProductPortalExecutorService;
	
	public ProductProtalService() {
	}
	
	public List<Product> getOffers() {
		return selectProductOfferPortalExecutorService.getOffers();
	}

	public List<Product> getProducts() {
		return selectProductPortalExecutorService.getProducts();
	}

	public List<ProductCategory> getCategories() {
		return selectProductCategoryPortalExecutorService.getCategories();
	}
	
}
