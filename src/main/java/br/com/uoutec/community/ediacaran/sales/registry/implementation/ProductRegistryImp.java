package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.SalesPluginConstants;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.entity.registry.AbstractRegistry;

@Singleton
@Default
public class ProductRegistryImp 
	extends AbstractRegistry
	implements ProductRegistry{

	@Inject
	private ProductEntityAccess entityAccess;

	@Inject
	private ObjectsTemplateManager objectsManager;
	
	@ActivateRequestContext
	@Transactional
	public void registerProduct(Product entity) throws ProductRegistryException {
		try{
			
			if(entity.getId() > 0){
				entityAccess.update(entity);	
			}
			else{
				entityAccess.save(entity);				
			}
			
			if(entityAccess.ifIndexExist(entity)) {
				entityAccess.updateIndex(entity);
			}
			else {
				entityAccess.saveIndex(entity);
			}
			entityAccess.flush();
			
			if(entity.getThumb() != null) {
				String path = getThumbPath(entity);
				objectsManager.registerObject(path, null, entity.getThumb());
				entity.setThumb((Path)objectsManager.getObject(path));
			}
			
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}
	
	public void removeProduct(Product entity) throws ProductRegistryException{
		try{
			if(entity.getThumb() != null) {
				objectsManager.unregisterObject(getThumbPath(entity), null);
			}
			
			entityAccess.delete(entity);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	public Product findProductById(int id) throws ProductRegistryException{
		try{
			Product e = entityAccess.findById(id);
			e.setThumb((Path)objectsManager.getObject(getThumbPath(e), null));
			return e;
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
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<Product> list = entityAccess.searchProduct(value, firstResult, maxResults);
			List<Product> products = new ArrayList<>();
			
			for(Product e: list) {
				e = entityAccess.findById(e.getId());
				e.setThumb((Path)objectsManager.getObject(getThumbPath(e), null));
				products.add(e);
			}
			
			return new ProductSearchResult(products.size() > maxItens, -1, page, products.size() > maxItens? products.subList(0, maxItens -1) : products);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}
	
	private String getThumbPath(Product e) {
		
		if(e == null || e.getId() <= 0) {
			return null;
		}
		
		List<String> partsList = new ArrayList<>();
		String id = Integer.toString(e.getId(), Character.MAX_RADIX);
		
		int maxlen = id.length() / 3;
		maxlen = maxlen == 0? id.length() : maxlen;
		
		char[] chars = id.toCharArray();
		int i = 0;
		
		while(i<chars.length) {
			
			int from = i;
			int to = i + maxlen;
			
			if(to > chars.length) {
				to = chars.length - 1;
			}
			
			char[] tmp = Arrays.copyOfRange(chars, from, to);
			partsList.add(new String(tmp));
			i += maxlen;
			
		}
		
		String path = String.join("/", partsList);
		
		return SalesPluginConstants.PRODUCTS_DRIVER_NAME + SalesPluginConstants.IMAGES_DRIVER_PATH + "/" + path;
	}
	
	@Override
	public void flush() {
		entityAccess.flush();
	}
	
}
