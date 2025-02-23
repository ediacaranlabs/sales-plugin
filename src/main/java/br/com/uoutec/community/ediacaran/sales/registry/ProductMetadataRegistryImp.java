package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataAttributeEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.ProductMetadataAttributeRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.ProductMetadataRegistryUtil;

@Singleton
public class ProductMetadataRegistryImp implements ProductMetadataRegistry{

	@Inject
	private ProductMetadataEntityAccess entityAccess;
	
	@Inject
	private ProductMetadataAttributeEntityAccess metadataentityAccess;
	
	@Override
	public void registerProductMetadata(ProductMetadata entity) throws ProductRegistryException {
		try{
			ProductMetadataRegistryUtil.validate(entity);
			ProductMetadataRegistryUtil.saveOrUpdate(entity, entityAccess);
			ProductMetadataRegistryUtil.sendToRepository(entityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public void removeProductMetadata(ProductMetadata entity) throws ProductRegistryException {
		try{
			ProductMetadata actualEntity = ProductMetadataRegistryUtil.getActual(entity, entityAccess);
			ProductMetadataRegistryUtil.delete(actualEntity, entityAccess);
			ProductMetadataRegistryUtil.sendToRepository(entityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public ProductMetadata findProductMetadataById(int id) throws ProductRegistryException {
		try{
			return ProductMetadataRegistryUtil.get(id, entityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public ProductSearchResult search(ProductSearch value) throws ProductRegistryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductMetadataAttribute findProductMetadataAttributeById(int id) throws ProductRegistryException {
		try{
			return ProductMetadataAttributeRegistryUtil.get(id, metadataentityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public void registerProductMetadataAttributes(List<ProductMetadataAttribute> attributes, ProductMetadata parent)
			throws ProductRegistryException {
		
		try {
			for(ProductMetadataAttribute entity: attributes) {
				ProductMetadataAttributeRegistryUtil.validate(entity);
			}
			
			for(ProductMetadataAttribute entity: attributes) {
				ProductMetadataAttributeRegistryUtil.saveOrUpdate(entity, metadataentityAccess);
			}
			
			ProductMetadataAttributeRegistryUtil.sendToRepository(metadataentityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public void removeProductMetadataAttributes(List<ProductMetadataAttribute> attributes, ProductMetadata parent)
			throws ProductRegistryException {
		try {
			for(ProductMetadataAttribute entity: attributes) {
				ProductMetadataAttributeRegistryUtil.delete(entity, metadataentityAccess);
			}
			
			ProductMetadataAttributeRegistryUtil.sendToRepository(metadataentityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public List<ProductMetadataAttribute> getProductMetadataAttributes(ProductMetadata parent)
			throws ProductRegistryException {
		try {
			return ProductMetadataAttributeRegistryUtil.getByParent(parent, metadataentityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public void flush() {
	}

}
