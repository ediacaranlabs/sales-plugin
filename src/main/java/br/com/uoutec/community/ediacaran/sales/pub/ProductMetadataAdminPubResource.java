package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResult;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataAttributeUpdate;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataUpdate;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadatasSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.AttributeCodeDuplicatedProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OptionCodeDuplicatedProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.service.ProductMetadataService;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/product-metadata", defaultActionName="/")
@ResponseErrors(rendered=false)
public class ProductMetadataAdminPubResource {

	private static final Logger logger = LoggerFactory.getLogger(ProductMetadataAdminPubResource.class);
	
	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private ProductMetadataService productMetadataService;
	
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
			ProductMetadataSearchResult result = productMetadataService.search(searchRequest);
			return new ProductMetadatasSearchResultPubEntity(result, locale);
		}
		catch(InvalidRequestException ex) {
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			throw ex;
		}
		catch(Throwable ex) {
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			return null;
		}
		
	}
	
	@Action({
		"/edit/{product_metadata.protectedID:[^/\\s//]+}",
		"/edit"
	})
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
			if(productMetadataPubEntity == null) {
				productMetadataPubEntity = new ProductMetadataPubEntity();
			}
			
			productMetadataPubEntity.setLocale(locale);
			entity = productMetadataPubEntity.rebuild(productMetadataPubEntity.getProtectedID() != null, false, true);
			Map<String,Object> map = new HashMap<>();
			map.put("entity", entity);
			map.put("types", ProductAttributeType.values());
			map.put("valueTypes", ProductAttributeValueType.values());
			return map;
		}
		catch(Throwable ex){
			
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);

			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
	}
	
	@RequestMethod("POST")
	@Action({"/save"})
	@View("${plugins.ediacaran.sales.template}/admin/product_metadata/result")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.SAVE)
	@Transactional
	public Map<String,Object> save(
			@Basic(bean = "product_metadata")
			ProductMetadataPubEntity productMetadataPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		

		ProductMetadataUpdate entity;
		try{
			if(productMetadataPubEntity != null) {
				productMetadataPubEntity.setLocale(locale);
			}
			entity = productMetadataPubEntity.rebuild(productMetadataPubEntity.getProtectedID() != null, true, true);
		}
		catch(Throwable ex){
			
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.save.error.fail_load_request, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			List<ProductMetadataAttribute> list = entity.getAttributeList();
			productMetadataService.registerProductMetadata(entity);
			
			if(list != null) {
				Collections.sort(
						list, 
						(a,b)-> 
							((ProductMetadataAttributeUpdate)a).getIndex() - ((ProductMetadataAttributeUpdate)b).getIndex() 
				);
			}
			
			Map<String,Object> map = new HashMap<>();
			map.put("entity", entity);
			map.put("attributes", list);
			return map;
		}
		catch(AttributeCodeDuplicatedProductRegistryException ex){

			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.save.error.attribute_code_duplicated, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		catch(OptionCodeDuplicatedProductRegistryException ex){
			
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.save.error.option_code_duplicated, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		catch(Throwable ex){
			
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.save.error.fail_save, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
	}

	@RequestMethod("POST")
	@Action({"/delete"})
	@View("${plugins.ediacaran.sales.template}/admin/product_metadata/result")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.PRODUCT.DELETE)
	public Map<String,Object> remove(
			@Basic(bean = "product_metadata")
			ProductMetadataPubEntity productMetadataPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		ProductMetadata entity;
		try{
			if(productMetadataPubEntity != null) {
				productMetadataPubEntity.setLocale(locale);
			}
			entity = productMetadataPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.delete.error.fail_load_request, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			productMetadataService.unregisterProductMetadata(entity);
		}
		catch(Throwable ex){
			
			if(logger.isErrorEnabled()) {
				logger.error("request fail", ex);
			}
			
			String error = i18nRegistry
					.getString(
							ProductMetadataAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductMetadataAdminPubResourceMessages.delete.error.fail_remove, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<>();
		map.put("entity", entity);
		return map;
		
	}
	
}
