package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategoryResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategorySearch;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductCategoryEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductCategoryIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.ProductCategoryUtil;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidatorBean;

@Singleton
public class ProductCategoryRegistryImp implements ProductCategoryRegistry {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private ProductCategoryEntityAccess entityAccess;
	
	@Inject
	private ProductCategoryIndexEntityAccess indexEntityAccess;
	
	
	@Inject
	private ObjectsTemplateManager objectsManager;
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void registerProductCategory(ProductCategory entity) throws ProductCategoryRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.CATEGORY.getRegisterPermission());
		
		try {
			if(entity.getId() <= 0) {
				ValidatorBean.validate(entity, saveValidations);
				entityAccess.save(entity);
			}
			else {
				ValidatorBean.validate(entity, updateValidations);
				entityAccess.update(entity);
			}
			
			entityAccess.flush();
			
			if(entity.getThumb() != null) {
				String path = ProductCategoryUtil.getThumbPath(entity);
				objectsManager.registerObject(path, null, entity.getThumb());
				entity.setThumb((Path)objectsManager.getObject(path));
			}
			
		}
		catch(Throwable ex){
			throw new ProductCategoryRegistryException(ex);
		}
		
	}
	
	@Override
	@Transactional
	@ActivateRequestContext
	public void removeProductCategory(ProductCategory entity) throws ProductCategoryRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.CATEGORY.getRemovePermission());
		
		try {
			if(entity.getId() > 0) {
				entityAccess.delete(entity);
			}
			
			entityAccess.flush();
			
			if(entity.getId() > 0) {
				String path = ProductCategoryUtil.getThumbPath(entity);
				objectsManager.unregisterObject(path, null);
			}
			
		}
		catch(Throwable ex){
			throw new ProductCategoryRegistryException(ex);
		}
		
	}

	@Override
	@ActivateRequestContext
	public ProductCategory findById(int id) throws ProductCategoryRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.CATEGORY.getFindPermission());

		try {
			return entityAccess.findById(id);
		}
		catch(Throwable ex){
			throw new ProductCategoryRegistryException(ex);
		}
		
	}
	
	@Override
	@ActivateRequestContext
	public ProductCategoryResultSearch searchProductCategory(ProductCategorySearch value) throws ProductCategoryRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.CATEGORY.getSearchPermission());
		
		try{
			int page = value.getPage() == null? 0 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<ProductCategory> searchResult = indexEntityAccess.searchProduct(value, firstResult, maxResults);
			
			List<ProductCategory> list = new ArrayList<>();
			
			for(ProductCategory e: searchResult) {
				e = entityAccess.findById(e.getId());
				list.add(e);
			}
			
			return new ProductCategoryResultSearch(list.size() > maxItens, -1, page, list.size() > maxItens? list.subList(0, maxItens -1) : list);
		}
		catch(Throwable e){
			throw new ProductCategoryRegistryException(e);
		}
		
	}

	@Override
	public List<ProductCategory> findByParent(ProductCategory parent) throws ProductCategoryRegistryException {
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT.CATEGORY.getListPermission());

		try {
			return entityAccess.getProductByParent(parent);
		}
		catch(Throwable ex){
			throw new ProductCategoryRegistryException(ex);
		}
	}
	
}
