package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductEntitySearchResult;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductImageEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
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
	private ProductIndexEntityAccess productIndexEntityAccess;
	
	@Inject
	private ObjectsTemplateManager objectsManager;
	
	@Transactional
	@ActivateRequestContext
	public void registerProduct(Product entity) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.getRegisterPermission());
		
		try{
			ProductRegistryUtil.validate(entity, productMetadataEntityAccess);
			
			ProductRegistryUtil.saveOrUpdate(entity, entityAccess);
			ProductRegistryUtil.sendToRepository(entityAccess);
			
			ProductRegistryUtil.updateIndex(entity, productIndexEntityAccess);
			ProductRegistryUtil.sendToRepository(entityAccess);
			
			ProductRegistryUtil.persistProductImage(entity, objectsManager);
			
			if(entity.getImages() != null) {
			
				for(ProductImage i: entity.getImages()) {
					i.setProduct(entity.getId());
					if(i.getId() != null && i.isDeleted()) {
						//ProductImage e = ProductImageRegistryUtil.getActual(i, imageEntityAccess);
						ProductImageRegistryUtil.deleteImage(i, objectsManager);
						ProductImageRegistryUtil.delete(i, imageEntityAccess);
					}
					else
					if(!i.isDeleted()) {
						ProductImageRegistryUtil.validate(i);
						ProductImageRegistryUtil.saveOrUpdate(i, imageEntityAccess);
					}
				}
				
				ProductImageRegistryUtil.sendToRepository(imageEntityAccess);
				
			}
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void removeProduct(Product entity) throws ProductRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.getRemovePermission());
		
		try{
			Product actualEntity = ProductRegistryUtil.getActualProduct(entity, entityAccess, objectsManager);
			ProductRegistryUtil.deleteProductImage(actualEntity, objectsManager);
			ProductRegistryUtil.delete(actualEntity, entityAccess);
			ProductRegistryUtil.deleteIndex(actualEntity, productIndexEntityAccess);
			ProductRegistryUtil.sendToRepository(entityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	@ActivateRequestContext
	public Product findProductById(int id) throws ProductRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.getGetPermission());
		
		try{
			return ProductRegistryUtil.getProduct(id, entityAccess, objectsManager);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}
	
	@Override
	@ActivateRequestContext
	public List<Product> getProductByType(
			ProductType productType) throws ProductRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.getListPermission());
		
		try{
			return entityAccess.getProductByType(productType);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
	}

	@Override
	@ActivateRequestContext
	public ProductSearchResult search(ProductSearch value) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.getListPermission());
		
		try{
			int page = value.getPage() == null? 0 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			ProductEntitySearchResult searchResult = entityAccess.searchProduct(value, firstResult, maxResults);
			
			List<Product> products = new ArrayList<>();
			
			for(Product e: searchResult.getItens()) {
				e = entityAccess.findById(e.getId());
				products.add(e);
			}
			
			searchResult.setItens(products);
			return searchResult.toEntity(maxItens, -1, page);
			//return new ProductSearchResult(products.size() > maxItens, -1, page, products.size() > maxItens? products.subList(0, maxItens -1) : products);
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
	@Transactional
	@ActivateRequestContext
	public void registerProductImages(List<ProductImage> list, Product parent) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.IMAGE.getRegisterPermission());
		
		try{
			for(ProductImage entity: list) {
				if(entity.getProduct() != 0 && entity.getProduct() != parent.getId()) {
					throw new ProductRegistryException("invalid product: " + entity.getProduct() + " != " + parent.getId());
				}
				entity.setProduct(parent.getId());
				ProductImageRegistryUtil.validate(entity);
				ProductImageRegistryUtil.saveOrUpdate(entity, imageEntityAccess);
			}
			
			ProductImageRegistryUtil.sendToRepository(imageEntityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
		try{
			for(ProductImage entity: list) {
				ProductImageRegistryUtil.persistImage(entity, objectsManager);
			}
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void removeProductImages(List<ProductImage> list, Product parent) throws ProductRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.IMAGE.getRemovePermission());
		
		List<ProductImage> images = new ArrayList<>();
		try{
			for(ProductImage entity: list) {
				ProductImage e = ProductImageRegistryUtil.getActual(entity, imageEntityAccess);
				if(e != null && e.getProduct() == parent.getId()) {
					ProductImageRegistryUtil.deleteImage(entity, objectsManager);
					images.add(e);
				}
			}
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
		try {
			for(ProductImage entity: images) {
				ProductImageRegistryUtil.delete(entity, imageEntityAccess);
			}
			
			ProductImageRegistryUtil.sendToRepository(imageEntityAccess);
		}
		catch(EntityAccessException ex) {
			throw new ProductRegistryException(ex);
		}
		
	}

	@Override
	@ActivateRequestContext
	public ProductImage getImagesByID(String id) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.IMAGE.getGetPermission());
		
		try {
			return ProductImageRegistryUtil.get(id, imageEntityAccess);
		}
		catch(EntityAccessException ex) {
			throw new ProductRegistryException(ex);
		}
	}
	
	@Override
	@ActivateRequestContext
	public List<ProductImage> getImagesByProduct(Product product) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.IMAGE.getListPermission());
		
		try {
			return ProductImageRegistryUtil.getImages(product, imageEntityAccess);
		}
		catch(EntityAccessException ex) {
			throw new ProductRegistryException(ex);
		}
	}
	
}
