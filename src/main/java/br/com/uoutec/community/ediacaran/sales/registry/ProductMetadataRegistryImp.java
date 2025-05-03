package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.SystemProperties;
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
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PluginType;

@Singleton
public class ProductMetadataRegistryImp implements ProductMetadataRegistry {

	private volatile ProductMetadata defaultProductMetadata;
	
	private volatile boolean loadedDefaultProductMetadata;
	
	private volatile long nextLoadedDefaultProductMetadata = -1;
	
	@Inject
	private ProductMetadataEntityAccess entityAccess;
	
	@Inject
	private ProductMetadataAttributeEntityAccess metadataentityAccess;
	
	@Inject
	private ProductMetadataAttributeOptionEntityAccess productMetadataAttributeOptionEntityAccess;
	
	public ProductMetadataRegistryImp() {
		this.defaultProductMetadata = null;
		this.loadedDefaultProductMetadata = false;
	}
	
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
		
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			Integer id = pluginType.getConfiguration().getInt("default_product_metadata");
			if(id != null && id == entity.getId()) {
				this.nextLoadedDefaultProductMetadata = -1;	
			}
		}
		catch(Throwable ex) {
			//supress
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

	@Override
	@ActivateRequestContext
	public List<ProductMetadata> getAllProductMetadata() throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.getListPermission());
		
		try{
			return ProductMetadataRegistryUtil.getAllProductMetadata(entityAccess);
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
	public void registerProductMetadataAttributes(List<? extends ProductMetadataAttribute> attributes, ProductMetadata parent)
			throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getRegisterPermission());
		
		ProductMetadata defaultProductMetadata = getDefaultProductMetadata();
		
		try {
			for(ProductMetadataAttribute entity: attributes) {
				if(entity.getProductMetadata() <= 0) {
					entity.setProductMetadata(parent.getId());
				}
			}
			
			for(ProductMetadataAttribute entity: attributes) {
				ProductMetadataAttributeRegistryUtil.validate(entity, parent, metadataentityAccess);
			}
			
			if(defaultProductMetadata != null) {
				ProductMetadataAttributeRegistryUtil.checkDuplicationCode(defaultProductMetadata.getAttributeList(), parent, metadataentityAccess);
			}
			
			for(ProductMetadataAttribute entity: attributes) {
				ProductMetadataAttributeRegistryUtil.saveOrUpdate(entity, metadataentityAccess);
			}
			
			ProductMetadataAttributeRegistryUtil.sendToRepository(metadataentityAccess);
		}
		catch(ProductRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public ProductMetadata getDefaultProductMetadata() throws ProductRegistryException{
		
		long currentTime = SystemProperties.currentTimeMillis();
		if(currentTime > nextLoadedDefaultProductMetadata || !loadedDefaultProductMetadata) {
			loadDefaultProductMetadata();
		}
		
		return defaultProductMetadata;
	}

	public synchronized void loadDefaultProductMetadata() throws ProductRegistryException{
		
		long currentTime = SystemProperties.currentTimeMillis();
		
		if(currentTime > nextLoadedDefaultProductMetadata && loadedDefaultProductMetadata) {
			return;
		}
		
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			Integer id = pluginType.getConfiguration().getInt("default_product_metadata");
			if(id != null) {
				defaultProductMetadata = entityAccess.findById(id);
			}
			this.nextLoadedDefaultProductMetadata = currentTime + 10000; // TODO: update
			this.loadedDefaultProductMetadata = true;
		}
		catch(Throwable ex) {
			throw new ProductRegistryException(ex);
		}
		
	}
	
	@Override
	public void registerDefaultProductMetadataAttribute(ProductMetadataAttribute entity) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getRegisterPermission());
		
		ProductMetadata defaultMetadata = getDefaultProductMetadata();
		
		if(defaultMetadata != null) {
			registerProductMetadataAttributes(Arrays.asList(entity), defaultMetadata);
		}
		
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void removeProductMetadataAttributes(List<? extends ProductMetadataAttribute> attributes, ProductMetadata parent)
			throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getRemovePermission());
		
		try {
			for(ProductMetadataAttribute entity: attributes) {
				entity = ProductMetadataAttributeRegistryUtil.get(entity.getId(), metadataentityAccess);
				ProductMetadataAttributeRegistryUtil.delete(entity, metadataentityAccess);
			}
			
			ProductMetadataAttributeRegistryUtil.sendToRepository(metadataentityAccess);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	public void removeDefaultProductMetadataAttribute(String code) throws ProductRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.getRemovePermission());

		ProductMetadata defaultMetadata = getDefaultProductMetadata();
		
		if(defaultMetadata == null) {
			return;
		}
		
		ProductMetadataAttribute attr;
		try {
			attr = metadataentityAccess.findByCode(code, defaultMetadata);
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
		
		removeProductMetadataAttributes(Arrays.asList(attr), defaultMetadata);
		
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
	public void registerProductMetadataAttributeOptions(List<? extends ProductMetadataAttributeOption> options, ProductMetadataAttribute parent)
			throws ProductRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.OPTIONS.getRegisterPermission());
		
		try {
			for(ProductMetadataAttributeOption entity: options) {
				if(entity.getProductMetadataAttribute() <= 0) {
					entity.setProductMetadataAttribute(parent.getId());
				}
			}
			
			for(ProductMetadataAttributeOption entity: options) {
				ProductMetadataAttributeOptionRegistryUtil.validate(entity, parent, productMetadataAttributeOptionEntityAccess);
			}
			
			ProductMetadataAttributeOptionRegistryUtil.checkDuplicationCode(options, parent, productMetadataAttributeOptionEntityAccess);
			
			for(ProductMetadataAttributeOption entity: options) {
				ProductMetadataAttributeOptionRegistryUtil.saveOrUpdate(entity, productMetadataAttributeOptionEntityAccess);
			}
			
			ProductMetadataAttributeOptionRegistryUtil.sendToRepository(productMetadataAttributeOptionEntityAccess);
		}
		catch(ProductRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new ProductRegistryException(e);
		}
	}

	@Override
	@Transactional
	@ActivateRequestContext
	public void removeProductMetadataAttributeOptions(List<? extends ProductMetadataAttributeOption> options, ProductMetadataAttribute parent)
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
