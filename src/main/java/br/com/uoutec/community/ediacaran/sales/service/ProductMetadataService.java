package br.com.uoutec.community.ediacaran.sales.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResult;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataAttributeUpdate;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductMetadataUpdate;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;

@Singleton
public class ProductMetadataService {

	@Transient
	@Inject
	private ProductMetadataRegistry productMetadataRegistry;

	public List<ProductMetadataAttribute> getAttributes(ProductMetadata metadata) throws ProductRegistryException{
		return productMetadataRegistry.getProductMetadataAttributes(metadata);
	}
	
	@Transactional
	public void registerProductMetadata(ProductMetadata metadata) throws ProductRegistryException {
		
		if(metadata.getId() != 0 && metadata instanceof ProductMetadataUpdate) {
			
			ProductMetadataUpdate e = (ProductMetadataUpdate)metadata;
			
			e.setAttributes(null);
			
			productMetadataRegistry.registerProductMetadata(e);
			
			if(e.getRegisterAttributes() != null) {
				registerProductMetadataAttributes(e.getRegisterAttributes(), e);
			}
			
			if(e.getUnregisterAttributes() != null) {
				unregisterProductMetadataAttributes(e.getUnregisterAttributes(), e);
			}
			
		}
		else {
			productMetadataRegistry.registerProductMetadata(metadata);
		}
		
	}

	@Transactional
	public void unregisterProductMetadata(ProductMetadata metadata) throws ProductRegistryException {
		
		if(metadata.getId() != 0 && metadata instanceof ProductMetadataUpdate) {
			
			ProductMetadataUpdate e = (ProductMetadataUpdate)metadata;
			
			e.setAttributes(null);
			
			if(e.getRegisterAttributes() != null) {
				unregisterProductMetadataAttributes(e.getRegisterAttributes(), e);
			}
			
			if(e.getUnregisterAttributes() != null) {
				unregisterProductMetadataAttributes(e.getUnregisterAttributes(), e);
			}
			
			productMetadataRegistry.removeProductMetadata(e);
		}
		else {
			productMetadataRegistry.removeProductMetadata(metadata);
		}
		
	}
	
	public ProductMetadataSearchResult search(ProductMetadataSearch value)	throws ProductRegistryException {
		return productMetadataRegistry.search(value);
	}
	
	private void registerProductMetadataAttributes(List<ProductMetadataAttributeUpdate> list, ProductMetadataUpdate metadata) throws ProductRegistryException {

		for(ProductMetadataAttributeUpdate e: list) {
			e.setOptions(null);
		}
		
		productMetadataRegistry.registerProductMetadataAttributes(list, metadata);
		
		for(ProductMetadataAttributeUpdate e: list) {
			
			if(e.getRegisterOptions() != null) {
				registerProductMetadataAttributeOptions(e.getRegisterOptions(), e);
			}
			
			if(e.getUnregisterOptions() != null) {
				unregisterProductMetadataAttributeOptions(e.getUnregisterOptions(), e);
			}
			
		}
		
		
	}

	private void unregisterProductMetadataAttributes(List<ProductMetadataAttributeUpdate> list, ProductMetadataUpdate metadata) throws ProductRegistryException {
		productMetadataRegistry.removeProductMetadataAttributes(list, metadata);
	}

	private void registerProductMetadataAttributeOptions(List<ProductMetadataAttributeOption> list, ProductMetadataAttribute parent) throws ProductRegistryException {
		productMetadataRegistry.registerProductMetadataAttributeOptions(list, parent);
	}

	private void unregisterProductMetadataAttributeOptions(List<ProductMetadataAttributeOption> list, ProductMetadataAttribute parent) throws ProductRegistryException {
		productMetadataRegistry.removeProductMetadataAttributeOptions(list, parent);
	}
	
}
