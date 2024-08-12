package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.List;

import javax.ejb.EntityContext;
import javax.enterprise.inject.Default;
import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.entity.registry.AbstractRegistry;
import br.com.uoutec.services.entityaccess.ServicePlanEntityAccess;

@Singleton
@Default
public class ProductRegistryImp 
	extends AbstractRegistry
	implements ProductRegistry{

	public void registryServicePlan(Product entity) throws ProductRegistryException{
		try{
			ServicePlanEntityAccess entityAccess = 
					EntityContext.getEntity(ServicePlanEntityAccess.class);
			
			if(entity.getId() > 0){
				entityAccess.update(entity);	
			}
			else{
				entityAccess.save(entity);				
			}
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}
	
	public void removeServicePlan(Product entity) throws ProductRegistryException{
		try{
			ServicePlanEntityAccess entityAccess = 
					EntityContext.getEntity(ServicePlanEntityAccess.class);
			
			entityAccess.delete(entity);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	public Product findById(int id) throws ProductRegistryException{
		try{
			ServicePlanEntityAccess entityAccess = 
					EntityContext.getEntity(ServicePlanEntityAccess.class);
			return entityAccess.findById(id);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
	}
	
	public List<Product> getProductByType(
			ProductType productType) throws ProductRegistryException{
		try{
			ServicePlanEntityAccess entityAccess = 
					EntityContext.getEntity(ServicePlanEntityAccess.class);
			return entityAccess.getProductByType(productType);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
	}

	@Override
	public void flush() {
		ServicePlanEntityAccess entityAccess = 
				EntityContext.getEntity(ServicePlanEntityAccess.class);
		entityAccess.flush();
	}
	
}
