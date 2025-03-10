package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;

public class ProductMetadataAttributeSearchResultEntityFilter {

	private ProductMetadataAttribute productMetadataAttribute;
	
	private Map<String,Object> options;

	public ProductMetadataAttribute getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(ProductMetadataAttribute productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	
}
