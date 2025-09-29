package br.com.uoutec.community.ediacaran.sales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.brandao.brutos.web.WebResultAction;
import org.brandao.brutos.web.WebResultActionImp;

import br.com.uoutec.community.ediacaran.sales.entity.MeasurementUnit;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductImagePubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.InvalidRequestException;

public abstract class AbstractProductTypeViewHandler 
	implements ProductTypeViewHandler{
	
	/*
	@Override
	public WebResultAction edit(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		Product product = productPubEntity.rebuild(productPubEntity.getProtectedID() != null, false, false);
		return edit(product, locale);
	}
	*/
	
	@Override
	public WebResultAction edit(Product entity, Locale locale) throws InvalidRequestException {
		
		ProductCategoryRegistry productCategoryRegistry = EntityContextPlugin.getEntity(ProductCategoryRegistry.class);
		ProductRegistry productRegistry = EntityContextPlugin.getEntity(ProductRegistry.class);
		ProductMetadataRegistry productMetadataRegistry = EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);

		List<ProductImage> images = null;
		Map<Integer, ProductMetadataAttribute> attributesMetadata;
		List<ProductMetadata> productMetadataList;
		List<Currency> currencyList;
		List<ProductCategory> categoryList;
		
		Throwable exception = null;

		WebResultAction ra = new WebResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/product/edit_product.jsp"), true);
		
		try {
			
			images = productRegistry.getImagesByProduct(entity);
			productMetadataList = productMetadataRegistry.getAllProductMetadata();
			categoryList = productCategoryRegistry.getAll();
			
			ProductMetadata productMetadata = productMetadataRegistry.findProductMetadataById(entity.getMetadata());
			ProductMetadata defaultProductMetadata = productMetadataRegistry.getDefaultProductMetadata();
			
			List<ProductMetadataAttribute> listAttributeMetadata = new ArrayList<>();

			if(productMetadata != null) {
				listAttributeMetadata.addAll(productMetadata.getAttributeList());
				
				if(defaultProductMetadata != null && defaultProductMetadata.getId() != productMetadata.getId()) {
					listAttributeMetadata.addAll(defaultProductMetadata.getAttributeList());
				}
				
			}
			else {
				listAttributeMetadata.addAll(defaultProductMetadata.getAttributeList());
			}
			
			attributesMetadata = listAttributeMetadata.stream().collect(Collectors.toMap((e)->e.getId(), (e)->e));
			
			currencyList = Currency.getAvailableCurrencies().stream().collect(Collectors.toList());
			Collections.sort(currencyList, (a,b)->a.getCurrencyCode().compareTo(b.getCurrencyCode()));
		}
		catch(Throwable ex) {
			exception = ex;
			exception.printStackTrace();
			ra.add("exception", exception);
			return ra;
		}
		
		ra.add("entity", entity);
		ra.add("categories", categoryList);
		ra.add("attributesMetadata", attributesMetadata);
		ra.add("productMetadataList", productMetadataList);
		ra.add("images", images);
		ra.add("currencyList", currencyList);
		ra.add("measurementUnit", MeasurementUnit.values());
		
		return ra;
	}

	/*
	@Override
	public WebResultAction save(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		productPubEntity.setLocale(locale);
		Product product = productPubEntity.rebuild(productPubEntity.getProtectedID() != null, true, true);
		return save(product, locale);
	}
	*/
	
	@Override
	public WebResultAction save(Product entity, Throwable exception, Locale locale) throws InvalidRequestException {
		
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		WebResultAction ra = new WebResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/product/result.jsp"), true);
		
		Map<String,Object> vars = new HashMap<>();
		vars.put("entity", entity);
		vars.put("images", entity.getImages());
		
		ra.add("vars", vars);
		ra.add("exception", exception);
		
		return ra;
	}

	@Override
	public WebResultAction remove(Product entity, Throwable exception, Locale locale) throws InvalidRequestException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebResultAction updateView(Product entity, String code, Locale locale)
			throws InvalidRequestException {
		
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		ProductMetadataRegistry productMetadataRegistry = EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		WebResultAction ra = new WebResultActionImp();
		
		//Product product = null;
		Map<Integer, ProductMetadataAttribute> attributesMetadata;
		List<ProductMetadata> productMetadataList;
		
		Throwable exception = null;

		try {
			//product = productPubEntity.rebuild(productPubEntity.getProtectedID() != null, true, false);
			productMetadataList = productMetadataRegistry.getAllProductMetadata();
			
			ProductMetadata productMetadata = productMetadataRegistry.findProductMetadataById(entity.getMetadata());
			ProductMetadata defaultProductMetadata = productMetadataRegistry.getDefaultProductMetadata();
			
			List<ProductMetadataAttribute> listAttributeMetadata = new ArrayList<>();
			if(defaultProductMetadata != null && defaultProductMetadata.getId() != productMetadata.getId()) {
				listAttributeMetadata.addAll(defaultProductMetadata.getAttributeList());
			}
			
			listAttributeMetadata.addAll(productMetadata.getAttributeList());
			attributesMetadata = listAttributeMetadata.stream().collect(Collectors.toMap((e)->e.getId(), (e)->e));
		}
		catch(Throwable ex) {
			ex.printStackTrace();
			exception = ex;
			ra.add("exception", exception);
			return ra;
		}
		
		if("attribute_tab".equals(code)){
			ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/product/attribute_tab.jsp"), true);
		}

		Map<String,Object> vars = new HashMap<>();
		
		vars.put("entity", entity);
		vars.put("attributesMetadata", attributesMetadata);
		vars.put("productMetadataList", productMetadataList);
		
		ra.add("vars", vars);
		
		return ra;
	}
	
	@Override
	public WebResultAction getProductFormView(Product entity, Locale locale) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		WebResultAction ra = new WebResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product_form.jsp"), true);
		return ra;
	}
	
	@Override
	public WebResultAction getProductCartView(ProductRequest product) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		WebResultAction ra = new WebResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/front/cart/product.jsp"), true);
		return ra;
	}

	@Override
	public WebResultAction getProductCartView(String productType) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		WebResultAction ra = new WebResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/front/cart/product.jsp"), true);
		return ra;
	}
	
	@Override
	public WebResultAction getProductOrderView(ProductRequest product) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		WebResultAction ra = new WebResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product.jsp"), true);
		return ra;
	}

	@Override
	public WebResultAction getProductOrderView(String productType) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		WebResultAction ra = new WebResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product.jsp"), true);
		return ra;
	}
	
	public static class ProductImageGroup {
		
		private ProductImagePubEntity pubEntity;
		
		private ProductImage entity;

		public ProductImageGroup(ProductImagePubEntity pubEntity, ProductImage entity) {
			this.pubEntity = pubEntity;
			this.entity = entity;
		}

		public ProductImagePubEntity getPubEntity() {
			return pubEntity;
		}

		public void setPubEntity(ProductImagePubEntity pubEntity) {
			this.pubEntity = pubEntity;
		}

		public ProductImage getEntity() {
			return entity;
		}

		public void setEntity(ProductImage entity) {
			this.entity = entity;
		}
		
	}
}
