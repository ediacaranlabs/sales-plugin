package br.com.uoutec.community.ediacaran.sales.service.concurrent;

import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.service.ProductProtalService.ProductCategoryUpdate;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PluginType;

public class SelectProductCategoryPortalExecutorService 
	implements Runnable {

	private ProductCategoryUpdate productCategoryUpdate;
	
	private volatile boolean active;
	
	public SelectProductCategoryPortalExecutorService(ProductCategoryUpdate productCategoryUpdate) {
		this.productCategoryUpdate = productCategoryUpdate;
		this.active = false;
	}
	
	@Override
	public void run() {
		
		try {
			active = true;
			
			if(checkActive()) {
				safeAction();
			}
			
		}
		finally {
			active = false;
		}
		
	}

	public void setActive(boolean value) {
		this.active = value;
	}
	
	public boolean isActive() {
		return active;
	}
	
	private boolean checkActive() {
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			return pluginType.getConfiguration().getBoolean("portal_service");
		}
		catch(Throwable ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/*
	private long getSleepTime() {
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			int secs = pluginType.getConfiguration().getInt("portal_service_sleep_time");
			return TimeUnit.SECONDS.toMillis(secs);
		}
		catch(Throwable ex) {
			ex.printStackTrace();
			return TimeUnit.MINUTES.toMillis(3);
		}
	}
	*/
	
	private void safeAction() {
		try {
			action();
		}
		catch(Throwable ex) {
			ex.printStackTrace();
		}
	}
	
	private void action() throws ProductCategoryRegistryException, ProductRegistryException {
		
		ProductCategoryRegistry pcr = EntityContextPlugin.getEntity(ProductCategoryRegistry.class);
		ProductRegistry pr = EntityContextPlugin.getEntity(ProductRegistry.class);
		List<ProductCategory> result = new ArrayList<>();
		List<ProductCategory> list;
		int itens = 10;
		int index = 0;
		
		while(!(list = pcr.getAll(index, itens)).isEmpty()) {

			for(ProductCategory c: list) {

				if(c.getParent1() != null || c.getParent2() != null) {
					continue;
				}
				
				ProductSearch ps = new ProductSearch();
				ps.setCategory(c);
				ps.setPage(0);
				ps.setResultPerPage(1);
				
				ProductSearchResult psr = pr.search(ps);
				
				if(!psr.getItens().isEmpty()) {
					result.add(c);
				}
				
			}
			
			index += list.size();
			
		}
		
		productCategoryUpdate.setProductCategory(result);
		
	}
	
}
