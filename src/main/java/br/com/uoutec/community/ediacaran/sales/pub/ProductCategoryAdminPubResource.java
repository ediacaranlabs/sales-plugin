package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.AcceptRequestType;
import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.ResponseType;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.MediaTypes;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;
import org.brandao.brutos.web.WebResultAction;

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategoryResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategorySearch;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductCategoryPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductCategoryResultSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductCategorySearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistryException;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/products/category", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductCategoryAdminPubResource {

	@Transient
	@Inject
	private ProductCategoryRegistry productCategoryRegistry;
	
	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/product/category/index")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.CATEGORY.SEARCH)
	@Result("vars")
	public Map<String,Object> index(
			WebResultAction result,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale ) throws ProductCategoryRegistryException {
		
		List<ProductCategory> list = productCategoryRegistry.findByParent(null);
		Map<String,Object> map = new HashMap<>();
		map.put("categories", list);
		return map;
	}
	
	@Action("/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.CATEGORY.SEARCH)
	public ProductCategoryResultSearchPubEntity searchProduct(
			@DetachedName
			ProductCategorySearchPubEntity productCategorySearch,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		ProductCategorySearch search = null;
		try{
			search = productCategorySearch.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_load_request, 
							locale);
			throw new InvalidRequestException(error, ex);
		}

		ProductCategoryResultSearch result;
		
		try{
			result = productCategoryRegistry.searchProductCategory(search);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_search, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		return new ProductCategoryResultSearchPubEntity(result, locale);		
	}

	@Action("/productCategory/{productCategory.protectedID}")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.CATEGORY.SEARCH)
	public List<ProductCategoryPubEntity> getProduct(
			@Basic(bean = "productCategory")
			ProductCategoryPubEntity productCategoryPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		ProductCategory productCategory = null;
		
		try{
			productCategory = productCategoryPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_load_request, 
							locale);
			throw new InvalidRequestException(error, ex);
		}

		List<ProductCategory> list;
		List<ProductCategoryPubEntity> result;
		
		try{
			list = productCategoryRegistry.findByParent(productCategory);
			result = new ArrayList<>();
			for(ProductCategory pc: list) {
				result.add(new ProductCategoryPubEntity(pc, locale));
			}
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_search, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		return result;		
	}
	
	@Action({"/edit","/edit/{productCategory.protectedID:[^/\\s//]+}"})
	@View("${plugins.ediacaran.sales.template}/admin/product/category/edit")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SHOW)
	public Map<String,Object> edit(
			@Basic(bean = "productCategory")
			ProductCategoryPubEntity productCategoryPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Map<String,Object> map = new HashMap<>();
		ProductCategory productCategory;
		List<ProductCategory> parent2 = null;
		List<ProductCategory> parent1 = null;
		
		try {
			if(productCategoryPubEntity == null) {
				productCategoryPubEntity = new ProductCategoryPubEntity();
			}
			
			productCategory = productCategoryPubEntity.rebuild(productCategoryPubEntity.getProtectedID() != null, false, true);
			
			parent1 = productCategoryRegistry.findByParent(null);
			
			if(productCategory.getParent2() != null) {
				parent2 = productCategoryRegistry.findByParent(productCategory.getParent1());
			}
			
			map.put("categories1", parent1);
			map.put("categories2", parent2);
			map.put("entity", productCategory);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			map.put("excepiton", new InvalidRequestException(error, ex));
		}
		
		return map;
	}

	@Action({"/save"})
	@RequestMethod("POST")
	@View("${plugins.ediacaran.sales.template}/admin/product/category/result")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SAVE)
	public Map<String,Object> save(
			@Basic(bean="productCategory")
			ProductCategoryPubEntity productCategoryPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		ProductCategory productCategory = null;
		Throwable exception = null;
		
		try {
			productCategory = productCategoryPubEntity.rebuild(productCategoryPubEntity.getProtectedID() != null, true, true);
			productCategoryRegistry.registerProductCategory(productCategory);
		}
		catch(Throwable ex){
			exception = ex;
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("product_category", productCategory);
		map.put("exception", exception);
		return map;
		
	}

	@Action({"/delete"})
	@RequestMethod("POST")
	@View("${plugins.ediacaran.sales.template}/admin/product/category/result")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.DELETE)
	public Map<String,Object> remove(
			@Basic(bean="productCategory")
			ProductCategoryPubEntity productCategoryPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		ProductCategory productCategory = null;
		Throwable exception = null;
		
		try {
			productCategory = productCategoryPubEntity.rebuild(productCategoryPubEntity.getProtectedID() != null, false, true);
			productCategoryRegistry.removeProductCategory(productCategory);
		}
		catch(Throwable ex){
			exception = ex;
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("product_category", productCategory);
		map.put("exception", exception);
		return map;
		
	}
	
}
