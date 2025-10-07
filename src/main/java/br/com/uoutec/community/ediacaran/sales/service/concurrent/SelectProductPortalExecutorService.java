package br.com.uoutec.community.ediacaran.sales.service.concurrent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.service.ProductProtalService.ProductOfferUpdate;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PluginType;

public class SelectProductPortalExecutorService 
	implements Runnable {

	private ProductOfferUpdate productOfferUpdate;
	
	private volatile boolean active;
	
	public SelectProductPortalExecutorService(ProductOfferUpdate productOfferUpdate) {
		this.productOfferUpdate = productOfferUpdate;
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
		
		ProductRegistry pr = EntityContextPlugin.getEntity(ProductRegistry.class);
		List<Product> result = new ArrayList<>();
		List<Product> list;
		int itens = 50;
		int index = 0;
		
		list: while(!(list = pr.getAll(index, itens)).isEmpty()) {

			for(Product p: list) {

				if(p.getOfferDiscount() == null || p.getOfferDiscount().doubleValue() == 0) {
					continue;
				}

				if(p.getOfferDate() == null || p.getOfferDate().isAfter(LocalDate.now())) {
					continue;
				}
				
				result.add(p);
				
				if(result.size() >= 12) {
					break list;
				}
				
			}
			
			index += list.size();
			
		}
		
		productOfferUpdate.setProductCategory(result);
		
	}
	
}
