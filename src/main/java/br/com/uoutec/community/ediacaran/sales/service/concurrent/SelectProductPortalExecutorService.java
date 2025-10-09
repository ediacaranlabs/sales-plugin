package br.com.uoutec.community.ediacaran.sales.service.concurrent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

@Singleton
public class SelectProductPortalExecutorService 
	implements Runnable {

	private volatile List<Product> products;
	
	private volatile boolean active;
	
	public SelectProductPortalExecutorService() {
		this.products = Collections.unmodifiableList(new ArrayList<>());
		this.active = false;
	}
	
	@Override
	public void run() {
		
		try {
			if(isActive()) {
				safeAction();
			}
		}
		finally {
			active = false;
		}
		
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setActive(boolean value) {
		this.active = value;
	}
	
	public boolean isActive() {
		return active;
	}
	
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
		
		this.products = Collections.unmodifiableList(result);
		
	}
	
}
