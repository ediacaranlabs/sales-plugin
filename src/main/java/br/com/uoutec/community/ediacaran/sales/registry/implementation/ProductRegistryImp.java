package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.entity.registry.AbstractRegistry;

@Singleton
@Default
public class ProductRegistryImp 
	extends AbstractRegistry
	implements ProductRegistry{

	@Inject
	private ProductEntityAccess entityAccess;
	
	public void registerProduct(Product entity) throws ProductRegistryException{
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
	
	public void removeProduct(Product entity) throws ProductRegistryException{
		try{
			entityAccess.delete(entity);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	public Product findProductById(int id) throws ProductRegistryException{
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
	public ProductSearchResult search(ProductSearch value) throws ProductRegistryException {
		try{
			int page = value.getPage() == null? 0 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = page*maxItens;
			int maxResults = maxItens + 1;
			List<Product> products = entityAccess.searchProduct(value, firstResult, maxResults);
			
			return new ProductSearchResult(products.size() > maxItens, -1, page, products.size() > maxItens? products.subList(0, maxItens -1) : products);
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
