package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductPlanEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.entity.registry.AbstractRegistry;

@Singleton
@Default
public class ProductRegistryImp 
	extends AbstractRegistry
	implements ProductRegistry{

	@Inject
	private ProductPlanEntityAccess entityAccess;
	
	public void registryServicePlan(Product entity) throws ProductRegistryException{
		try{
			
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
			entityAccess.delete(entity);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	public Product findById(int id) throws ProductRegistryException{
		try{
			return entityAccess.findById(id);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
	}
	
	public List<Product> getProductByType(
			ProductType productType) throws ProductRegistryException{
		try{
			return entityAccess.getProductByType(productType);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
	}

	@Override
	public void flush() {
		entityAccess.flush();
	}
	
}
