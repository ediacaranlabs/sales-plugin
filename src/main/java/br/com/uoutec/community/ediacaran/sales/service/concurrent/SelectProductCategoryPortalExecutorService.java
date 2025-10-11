package br.com.uoutec.community.ediacaran.sales.service.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

@Singleton
public class SelectProductCategoryPortalExecutorService 
	implements Runnable {

	private volatile List<ProductCategory> categories;
	
	private volatile boolean active;
	
	public SelectProductCategoryPortalExecutorService() {
		this.active = false;
		this.categories = Collections.unmodifiableList(new ArrayList<>());
	}
	
	@Override
	@ActivateRequestContext
	public void run() {

		try {
			if(isActive()) {
				ContextSystemSecurityCheck.doPrivileged(()->{
					safeAction();
					return null;
				});
			}
		}
		finally {
			active = false;
		}
		
	}

	public List<ProductCategory> getCategories() {
		return categories;
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
		
		ProductCategoryRegistry pcr = EntityContextPlugin.getEntity(ProductCategoryRegistry.class);
		ProductRegistry pr = EntityContextPlugin.getEntity(ProductRegistry.class);
		List<ProductCategory> result = new ArrayList<>();
		List<ProductCategory> list;
		int itens = 10;
		int index = 0;
		
		//list: while(!(list = pcr.getAll(index, itens)).isEmpty()) {
		while(!(list = pcr.getAll(index, itens)).isEmpty()) {
			for(ProductCategory c: list) {

				if(c.getParent1() != null || c.getParent2() != null) {
					continue;
				}
				
				result.add(c);
				
				/*
				ProductSearch ps = new ProductSearch();
				ps.setCategory(c);
				ps.setPage(0);
				ps.setResultPerPage(1);
				
				ProductSearchResult psr = pr.search(ps);
				
				if(!psr.getItens().isEmpty()) {
					result.add(c);
				}
				
				if(result.size() >= 12) {
					break list;
				}
				*/
			}
			
			index += list.size();
			
		}
		
		this.categories = Collections.unmodifiableList(result);
		
	}
	
}
