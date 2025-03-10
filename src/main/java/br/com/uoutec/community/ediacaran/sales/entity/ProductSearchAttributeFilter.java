package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;

public class ProductSearchAttributeFilter {

	private int productMetadata;
	
	private int productAttribute;
	
	private ProductAttributeValueType type;
	
	private Object value;

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

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public ProductAttributeValueType getType() {
		return type;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productAttribute, productMetadata);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductSearchAttributeFilter other = (ProductSearchAttributeFilter) obj;
		return productAttribute == other.productAttribute && productMetadata == other.productMetadata;
	}
	
}
