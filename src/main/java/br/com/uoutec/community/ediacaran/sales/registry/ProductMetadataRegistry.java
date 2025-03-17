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

	/* metadata */
	
	void registerProductMetadata(ProductMetadata entity) throws ProductRegistryException;
	
	void removeProductMetadata(ProductMetadata entity) throws ProductRegistryException;
	
	ProductMetadata findProductMetadataById(int id) throws ProductRegistryException;

	ProductMetadata getDefaultProductMetadata() throws ProductRegistryException;
	
	ProductMetadataSearchResult search(ProductMetadataSearch value)	throws ProductRegistryException;

	ProductMetadataAttribute findProductMetadataAttributeById(int id) throws ProductRegistryException;
	
	/* attributes */
	
	void registerProductMetadataAttributes(List<? extends ProductMetadataAttribute> attributes, ProductMetadata parent) throws ProductRegistryException;

	void registerDefaultProductMetadataAttribute(ProductMetadataAttribute attribute) throws ProductRegistryException;
	
	void removeProductMetadataAttributes(List<? extends ProductMetadataAttribute> attributes, ProductMetadata parent) throws ProductRegistryException;
	
	void removeDefaultProductMetadataAttribute(String code) throws ProductRegistryException;
	
	List<ProductMetadataAttribute> getProductMetadataAttributes(ProductMetadata parent)	throws ProductRegistryException;
	
	/* options */
	
	ProductMetadataAttributeOption findProductMetadataAttributeOptionById(int id) throws ProductRegistryException;

	void registerProductMetadataAttributeOptions(List<? extends ProductMetadataAttributeOption> options, ProductMetadataAttribute parent)
			throws ProductRegistryException;

	void removeProductMetadataAttributeOptions(List<? extends ProductMetadataAttributeOption> attributes, ProductMetadataAttribute parent)
			throws ProductRegistryException;

	List<ProductMetadataAttributeOption> getProductMetadataAttributeOptions(ProductMetadataAttribute parent)
			throws ProductRegistryException;
	
}
