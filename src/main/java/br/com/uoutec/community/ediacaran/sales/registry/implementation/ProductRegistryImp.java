package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductImageEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductUtil;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.entity.registry.AbstractRegistry;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
@Default
public class ProductRegistryImp 
	extends AbstractRegistry
	implements ProductRegistry{

	@Inject
	private ProductMetadataEntityAccess productMetadataEntityAccess;
	
	@Inject
	private ProductEntityAccess entityAccess;

	@Inject
	private ProductImageEntityAccess imageEntityAccess;
	
	@Inject
	private ObjectsTemplateManager objectsManager;
	
	@ActivateRequestContext
	@Transactional
	public void registerProduct(Product entity) throws ProductRegistryException {
		
		try {
			ProductRegistryUtil.validate(entity, productMetadataEntityAccess);
		}
		catch(Throwable ex) {
			throw new ProductRegistryException(ex);
		}
		
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
				String path = ProductUtil.getThumbPath(entity);
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
				objectsManager.unregisterObject(ProductUtil.getThumbPath(entity), null);
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
			e.setThumb((Path)objectsManager.getObject(ProductUtil.getThumbPath(e), null));
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
				e.setThumb((Path)objectsManager.getObject(ProductUtil.getThumbPath(e), null));
				products.add(e);
			}
			
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

	@Override
	public void registerProductImages(List<ProductImage> images, Product entity) throws ProductRegistryException {
		try {
			for(ProductImage pi: images) {
				pi.setProduct(entity.getId());
				
				if(pi.getId() == null) {
					imageEntityAccess.save(pi);
				}
				else {
					imageEntityAccess.update(pi);;
				}
			}
			
			imageEntityAccess.flush();

			for(ProductImage pi: images) {
				if(pi.getImage() != null) {
					String path = ProductUtil.getThumbPath(pi);
					objectsManager.registerObject(path, null, pi.getImage());
					pi.setImage((Path)objectsManager.getObject(path));
				}			
			}
			
		}
		catch(EntityAccessException ex) {
			throw new ProductRegistryException(ex);
		}
	}

	@Override
	public void removeProductImages(List<ProductImage> images, Product entity) throws ProductRegistryException {

		try {
			for(ProductImage pi: images) {
				if(pi.getImage() != null) {
					String path = ProductUtil.getThumbPath(pi);
					objectsManager.unregisterObject(path, null);
				}			
			}
			
			for(ProductImage pi: images) {
				imageEntityAccess.delete(pi);
			}
			
			imageEntityAccess.flush();
		}
		catch(EntityAccessException ex) {
			throw new ProductRegistryException(ex);
		}
		
	}

	@Override
	public ProductImage getImagesByID(String id) throws ProductRegistryException {
		try {
			ProductImage e = imageEntityAccess.findById(id);
			
			if(e != null) {
				String path = ProductUtil.getThumbPath(e);
				e.setImage((Path)objectsManager.getObject(path));
			}			

			return e;
		}
		catch(EntityAccessException ex) {
			throw new ProductRegistryException(ex);
		}
	}
	
	@Override
	public List<ProductImage> getImagesByProduct(Product product) throws ProductRegistryException {
		
		List<ProductImage> images;
		
		try {
			images = imageEntityAccess.getImagesByProduct(product);
			
			for(ProductImage pi: images) {
				String path = ProductUtil.getThumbPath(pi);
				pi.setImage((Path)objectsManager.getObject(path));
			}
			
			return images;
		}
		catch(EntityAccessException ex) {
			throw new ProductRegistryException(ex);
		}
	}
	
}
