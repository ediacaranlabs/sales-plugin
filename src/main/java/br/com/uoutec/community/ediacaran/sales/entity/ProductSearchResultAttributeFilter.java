package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Map;

public class ProductSearchResultAttributeFilter {

	private String title;
	
	private int productMetadata;
	
	private int productAttribute;
	
	private ProductAttributeValueType type;
	
	private Map<String,Object> options;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(int productMetadata) {
		this.productMetadata = productMetadata;
	}

	public int getProductAttribute() {
		return productAttribute;
	}

	public void setProductAttribute(int productAttribute) {
		this.productAttribute = productAttribute;
	}

	public ProductAttributeValueType getType() {
		return type;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	
}
