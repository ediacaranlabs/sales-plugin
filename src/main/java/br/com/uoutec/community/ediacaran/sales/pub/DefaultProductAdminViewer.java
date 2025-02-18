package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.brandao.brutos.ResultAction;
import org.brandao.brutos.web.HttpStatus;
import org.brandao.brutos.web.WebResultAction;
import org.brandao.brutos.web.WebResultActionImp;

import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.InvalidRequestException;

public class DefaultProductAdminViewer implements ProductAdminViewer{

	@Override
	public ResultAction showProductSearch(Locale locale) throws InvalidRequestException {
		
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		
		WebResultAction r = new WebResultActionImp();
		
		try {
			List<ProductType> list = productTypeRegistry.getProductTypes();
			Map<String,Object> map = new HashMap<>();
			map.put("productTypes", list);
			
			r.setView("${plugins.ediacaran.sales.template}/admin/product/index");
			r.setResponseStatus(HttpStatus.OK);
			r.add("vars", map);
		}
		catch(Throwable ex) {
			r.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			r.setReason("product type misconfiguration");
			r.add("exception", productTypeRegistry);
		}
		
		return r;
	}

	@Override
	public ProductsSearchResultPubEntity searchProduct(ProductSearchPubEntity productSearch, Locale locale)
			throws InvalidRequestException {
		
		ProductRegistry productRegistry = EntityContextPlugin.getEntity(ProductRegistry.class);
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		
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

	@Override
	public ResultAction showProductEdit(ProductPubEntity productPubEntity, Locale locale)
			throws InvalidRequestException {
		
		WebResultAction r = new WebResultActionImp();
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		
		try {
			String type = productPubEntity.getProductType();
			ProductType productType = productTypeRegistry.getProductType(type);
			ResultAction ra = productType.getViewHandler().edit(productPubEntity, locale);
			
			Map<String,Object> vars = new HashMap<>();
			vars.put("product_view", ra);
			
			r.setView("${plugins.ediacaran.sales.template}/admin/product/edit");
			r.add("vars", vars);
			
			return r;
		}
		catch(InvalidRequestException ex){
			throw ex;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
	}

	@Override
	public ResultAction saveProduct(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		
		try {
			String type = productPubEntity.getProductType();
			ProductType productType = productTypeRegistry.getProductType(type);
			return productType.getViewHandler().save(productPubEntity, locale);
		}
		catch(InvalidRequestException ex){
			throw ex;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
	}

	@Override
	public ResultAction removeProduct(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		
		try {
			String type = productPubEntity.getProductType();
			ProductType productType = productTypeRegistry.getProductType(type);
			return productType.getViewHandler().remove(productPubEntity, locale);
		}
		catch(InvalidRequestException ex){
			throw ex;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.edit.error.fail_load_request, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

}
