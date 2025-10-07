package br.com.uoutec.community.ediacaran.sales.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.service.concurrent.SelectProductCategoryPortalExecutorService;
import br.com.uoutec.community.ediacaran.system.concurrent.ThreadGroupManager;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class ProductProtalService implements PublicBean {

	private volatile List<Product> offers;
	
	private volatile List<Product> products;
	
	private SelectProductCategoryPortalExecutorService selectProductCategoryPortalExecutorService;

	@Inject
	private ThreadGroupManager threadGroupManager;
	
	public ProductProtalService() {
		this.categories = new ArrayList<>();
		this.offers = new ArrayList<>();
		this.products = new ArrayList<>();
		
		this.selectProductCategoryPortalExecutorService = new SelectProductCategoryPortalExecutorService((e)->{
			this.categories = e;
		});
		
	}
	
	
	private Object categoryLock = new Object();
	
	private volatile List<ProductCategory> categories;
	
	private volatile long nextLoad;
	
	public List<ProductCategory> getCategories() {
		
		long current = SystemProperties.currentTimeMillis();
		
		if(nextLoad < current) {
			
			synchronized (categoryLock) {
				if(nextLoad < current) {
					nextLoad = SystemProperties.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3);		
				}
				else {
					return categories;
				}
			}

			synchronized (categoryLock) {
				if(!selectProductCategoryPortalExecutorService.isActive()) {
					try {
						threadGroupManager.getThreadGroup("default")
							.execute(selectProductCategoryPortalExecutorService);
					}
					catch(Throwable ex) {
						ex.printStackTrace();
					}
				}
			}
			
		}
		
		return categories;
	}

	
	public static interface ProductCategoryUpdate {
		
		void setProductCategory(List<ProductCategory> value);
		
	}

	public static interface ProductOfferUpdate {
		
		void setProductCategory(List<Product> value);
		
	}
	
}
