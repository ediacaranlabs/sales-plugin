package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.ResultAction;
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
import org.brandao.brutos.web.WebResultActionImp;

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/products", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductAdminPubResource {

	@Transient
	@Inject
	private ProductTypeRegistry productTypeRegistry;

	@Transient
	@Inject
	private ProductRegistry productRegistry;
	
	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/product/index")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SEARCH)
	@Result("vars")
	public Map<String,Object> index(
			WebResultAction result,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale ) throws ProductTypeRegistryException {
		
		List<ProductType> list = productTypeRegistry.getProductTypes();
		Map<String,Object> map = new HashMap<>();
		map.put("productTypes", list);
		return map;
	}
	
	@Action("/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SEARCH)
	public ProductsSearchResultPubEntity searchProduct(
			@DetachedName
			ProductSearchPubEntity productSearch,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		ProductSearch search = null;
		try{
			search = productSearch.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_load_request, 
							locale);
			throw new InvalidRequestException(error, ex);
		}

		ProductSearchResult result;
		
		try{
			result = productRegistry.search(search);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_search, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		return new ProductsSearchResultPubEntity(result, locale);		
	}
	
	@Action({
		"/edit/{product.productType:[^/\\s//]+}",
		"/edit/{product.productType:[^/\\s//]+}/{product.protectedID:[^/\\s//]+}"
	})
	@View("${plugins.ediacaran.sales.template}/admin/product/edit")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SHOW)
	public Map<String,Object> edit(
			@Basic(bean = "product")
			ProductPubEntity productPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Map<String,Object> map = new HashMap<>();
		Product product;
		
		try {
			productPubEntity.setLocale(locale);
			product = productPubEntity.rebuild(productPubEntity.getProtectedID() != null, false, true);
			String type = productPubEntity.getProductType();
			ProductType productType = productTypeRegistry.getProductType(type);
			map.put("product_view", productType.getViewHandler().edit(product, locale));
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

	@Action({"/show/{product.productType:[^/\\s//]+}/{area}"})
	@RequestMethod("POST")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SHOW)
	public WebResultAction show(
			@Basic(bean="product")
			ProductPubEntity productPubEntity,
			@Basic(bean="area")
			String area,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Product product = null;
		
		try {
			productPubEntity.setLocale(locale);
			product = productPubEntity.rebuild(productPubEntity.getProtectedID() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setReason(error);
			return ra;
			
		}
		
		try {
			String type = productPubEntity.getProductType();
			ProductType productType = productTypeRegistry.getProductType(type);
			return productType.getViewHandler().updateView(product, area, locale);
		}
		catch(Throwable ex) {
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setReason("product viewer misconfiguration");
			return ra;
		}		
		
	}
	
	@Action({"/save/{product.productType:[^/\\s//]+}"})
	@RequestMethod("POST")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SAVE)
	public ResultAction save(
			@Basic(bean="product")
			ProductPubEntity productPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Product product = null;
		Throwable exception = null;
		
		try {
			productPubEntity.setLocale(locale);
			product = productPubEntity.rebuild(productPubEntity.getProtectedID() != null, true, true);
			productRegistry.registerProduct(product);
		}
		catch(Throwable ex){
			ex.printStackTrace();
			exception = ex;
		}
		
		try {
			String type = productPubEntity.getProductType();
			ProductType productType = productTypeRegistry.getProductType(type);
			return productType.getViewHandler().save(product, exception, locale);
		}
		catch(Throwable ex) {
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setReason("product viewer misconfiguration");
			return ra;
		}
		
	}

	@Action({"/delete"})
	@RequestMethod("POST")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.DELETE)
	public ResultAction remove(
			@Basic(bean="product")
			ProductPubEntity productPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Product product = null;
		Throwable exception = null;
		
		try {
			productPubEntity.setLocale(locale);
			product = productPubEntity.rebuild(productPubEntity.getProtectedID() != null, false, true);
			productRegistry.removeProduct(product);
		}
		catch(Throwable ex){
			exception = ex;
		}
		
		try {
			String type = productPubEntity.getProductType();
			ProductType productType = productTypeRegistry.getProductType(type);
			return productType.getViewHandler().remove(product, exception, locale);
		}
		catch(Throwable ex) {
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setReason("product viewer misconfiguration");
			return ra;
		}
		
	}
	
}
