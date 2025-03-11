package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;

public class ProductMetadataAttributeSearchResultEntityFilter {

	private ProductMetadataAttribute productMetadataAttribute;
	
	private Map<Object,ProductMetadataAttributeSearchResultOptionEntityFilter> options;

	public ProductMetadataAttribute getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(ProductMetadataAttribute productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
	}

	public Map<Object, ProductMetadataAttributeSearchResultOptionEntityFilter> getOptions() {
		return options;
	}

	public void setOptions(Map<Object, ProductMetadataAttributeSearchResultOptionEntityFilter> options) {
		this.options = options;
	}

	
}
