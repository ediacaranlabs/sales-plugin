package br.com.uoutec.community.ediacaran.sales.service.concurrent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

@Singleton
public class SelectProductOfferPortalExecutorService 
	implements Runnable {

	private volatile List<Product> offers;
	
	private volatile boolean active;
	
	public SelectProductOfferPortalExecutorService() {
		this.offers = Collections.unmodifiableList(new ArrayList<>());
		this.active = false;
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

	public List<Product> getOffers() {
		return offers;
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

				double offerDiscount = p.getOfferDiscount() == null? 0 : p.getOfferDiscount().doubleValue();
				LocalDate offerDate = p.getOfferDate();
				
				if(offerDate == null || offerDiscount <= 0) {
					continue;
				}
				
				if(LocalDate.now().isAfter(offerDate)) {
					continue;
				}
				
				result.add(p);
				
				if(result.size() >= 12) {
					break list;
				}
				
			}
			
			index += list.size();
			
		}
		
		this.offers = Collections.unmodifiableList(result);
		
	}
	
}
