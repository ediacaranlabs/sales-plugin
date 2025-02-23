package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResult;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataAttributeEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataAttributeOptionEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.ProductMetadataAttributeRegistryUtil;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.ProductMetadataRegistryUtil;

@Singleton
public class ProductMetadataRegistryImp implements ProductMetadataRegistry {

	@Inject
	private ProductMetadataEntityAccess entityAccess;
	
	@Inject
	private ProductMetadataAttributeEntityAccess metadataentityAccess;
	
	@Inject
	private ProductMetadataAttributeOptionEntityAccess productMetadataAttributeOptionEntityAccess;
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void registerProductMetadata(ProductMetadata entity) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.getRegisterPermission());
		
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
	@Transactional
	@ActivateRequestContext
	public void removeProductMetadata(ProductMetadata entity) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.getRemovePermission());
		
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
	@ActivateRequestContext
	public ProductMetadata findProductMetadataById(int id) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.getGetPermission());
		
		try{
			return ProductMetadataRegistryUtil.get(id, entityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	@ActivateRequestContext
	public ProductMetadataSearchResult search(ProductMetadataSearch value) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.getListPermission());
		
		try{
			return ProductMetadataRegistryUtil.search(value, entityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	/* Attributes */
	
	@Override
	@ActivateRequestContext
	public ProductMetadataAttribute findProductMetadataAttributeById(int id) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getGetPermission());
		
		try{
			return ProductMetadataAttributeRegistryUtil.get(id, metadataentityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void registerProductMetadataAttributes(List<ProductMetadataAttribute> attributes, ProductMetadata parent)
			throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getRegisterPermission());
		
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
	@Transactional
	@ActivateRequestContext
	public void removeProductMetadataAttributes(List<ProductMetadataAttribute> attributes, ProductMetadata parent)
			throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getRemovePermission());
		
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
	@ActivateRequestContext
	public List<ProductMetadataAttribute> getProductMetadataAttributes(ProductMetadata parent)
			throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getListPermission());
		
		try {
			return ProductMetadataAttributeRegistryUtil.getByParent(parent, metadataentityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	/* options */
	
	@Override
	@ActivateRequestContext
	public ProductMetadataAttributeOption findProductMetadataAttributeOptionById(int id) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.OPTIONS.getGetPermission());
		
		try{
			return ProductMetadataAttributeOptionRegistryUtil.get(id, productMetadataAttributeOptionEntityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void registerProductMetadataAttributeOptions(List<ProductMetadataAttributeOption> options, ProductMetadataAttribute parent)
			throws ProductRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.OPTIONS.getRegisterPermission());
		
		try {
			for(ProductMetadataAttributeOption entity: options) {
				ProductMetadataAttributeOptionRegistryUtil.validate(entity);
			}
			
			for(ProductMetadataAttributeOption entity: options) {
				ProductMetadataAttributeOptionRegistryUtil.saveOrUpdate(entity, productMetadataAttributeOptionEntityAccess);
			}
			
			ProductMetadataAttributeOptionRegistryUtil.sendToRepository(productMetadataAttributeOptionEntityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void removeProductMetadataAttributeOptions(List<ProductMetadataAttributeOption> options, ProductMetadataAttribute parent)
			throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.OPTIONS.getRemovePermission());
		
		try {
			for(ProductMetadataAttributeOption entity: options) {
				ProductMetadataAttributeOptionRegistryUtil.delete(entity, productMetadataAttributeOptionEntityAccess);
			}
			
			ProductMetadataAttributeOptionRegistryUtil.sendToRepository(productMetadataAttributeOptionEntityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	@ActivateRequestContext
	public List<ProductMetadataAttributeOption> getProductMetadataAttributeOptions(ProductMetadataAttribute parent)
			throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.OPTIONS.getListPermission());
		
		try {
			return ProductMetadataAttributeOptionRegistryUtil.getByParent(parent, productMetadataAttributeOptionEntityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}
	
	@Override
	public void flush() {
	}

}
