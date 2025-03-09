package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Set;

public class ProductSearchAttributeFilter {

	private int productMetadata;
	
	private int productAttribute;
	
	private Set<Object> values;

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

	public Set<Object> getValues() {
		return values;
	}

	public void setValues(Set<Object> values) {
		this.values = values;
	}
	
}
