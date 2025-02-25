package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
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
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResult;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadatasSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/products", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductMetadataAdminPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private ProductMetadataRegistry productMetadataRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/product_metadata/index")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT_METADATA.SEARCH)
	public void index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
			) {
	}
	
	@Action("/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT_METADATA.SEARCH)
	public ProductMetadatasSearchResultPubEntity searchProduct(
			@DetachedName
			ProductMetadataSearchPubEntity searchRequestPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		try {
			ProductMetadataSearch searchRequest = searchRequestPubEntity.rebuild(false, true, true);
			ProductMetadataSearchResult result = productMetadataRegistry.search(searchRequest);
			return new ProductMetadatasSearchResultPubEntity(result, locale);
		}
		catch(InvalidRequestException ex) {
			throw ex;
		}
		catch(Throwable ex) {
			return null;
		}
		
	}
	
	@Action("/edit/{product.protectedID:[^/\\s//]+}")
	@View("${plugins.ediacaran.sales.template}/admin/product_metadata/edit")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT_METADATA.SHOW)
	public Map<String,Object> edit(
			@Basic(bean = "product_metadata")
			ProductMetadataPubEntity productMetadataPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		ProductMetadata entity;
		try{
			entity = productMetadataPubEntity.rebuild(productMetadataPubEntity.getProtectedID() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<>();
		map.put("entity", entity);
		return map;
	}
	
	@RequestMethod("POST")
	@Action({"/save"})
	@View("${plugins.ediacaran.sales.template}/admin/product_metadata/result")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SAVE)
	public Map<String,Object> save(
			@Basic(bean = "product_metadata")
			ProductMetadataPubEntity productMetadataPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		

		ProductMetadata entity;
		try{
			entity = productMetadataPubEntity.rebuild(productMetadataPubEntity.getProtectedID() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			entity = productMetadataPubEntity.rebuild(productMetadataPubEntity.getProtectedID() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<>();
		map.put("entity", entity);
		return map;
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
		
		try {
			ProductViewerHandler handler = productViewerRegistry.getProductViewerHandler();
			return handler.getProductAdminViewer().removeProduct(productPubEntity, locale);
		}
		catch(InvalidRequestException ex) {
			throw ex;
		}
		catch(Throwable ex) {
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setReason("product viewer misconfiguration");
			return ra;
		}
		
	}
	
}
