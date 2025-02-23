package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResult;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.entity.registry.Registry;

public interface ProductMetadataRegistry extends PublicBean, Registry{

	void registerProductMetadata(ProductMetadata entity) throws ProductRegistryException;
	
	void removeProductMetadata(ProductMetadata entity) throws ProductRegistryException;
	
	ProductMetadata findProductMetadataById(int id) throws ProductRegistryException;
	
	ProductMetadataSearchResult search(ProductMetadataSearch value)	throws ProductRegistryException;

	ProductMetadataAttribute findProductMetadataAttributeById(int id) throws ProductRegistryException;
	
	void registerProductMetadataAttributes(List<ProductMetadataAttribute> attributes, ProductMetadata parent) throws ProductRegistryException;

	void removeProductMetadataAttributes(List<ProductMetadataAttribute> attributes, ProductMetadata parent) throws ProductRegistryException;
	
	List<ProductMetadataAttribute> getProductMetadataAttributes(ProductMetadata parent)	throws ProductRegistryException;
	
	ProductMetadataAttributeOption findProductMetadataAttributeOptionById(int id) throws ProductRegistryException;

	void registerProductMetadataAttributeOptions(List<ProductMetadataAttributeOption> options, ProductMetadataAttribute parent)
			throws ProductRegistryException;

	void removeProductMetadataAttributeOptions(List<ProductMetadataAttributeOption> attributes, ProductMetadataAttribute parent)
			throws ProductRegistryException;

	List<ProductMetadataAttributeOption> getProductMetadataAttributeOptions(ProductMetadataAttribute parent)
			throws ProductRegistryException;
	
}
