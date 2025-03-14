package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;

public class ProductSearchAttributeFilter {

	private ProductMetadataAttribute productMetadataAttribute;
	
	private Object value;

	public ProductMetadataAttribute getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(ProductMetadataAttribute productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productMetadataAttribute, value);
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
		return Objects.equals(productMetadataAttribute, other.productMetadataAttribute)
				&& Objects.equals(value, other.value);
	}
	
}
