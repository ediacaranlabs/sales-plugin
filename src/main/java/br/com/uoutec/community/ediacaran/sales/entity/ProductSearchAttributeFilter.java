package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;
import java.util.Set;

public class ProductSearchAttributeFilter {

	private ProductMetadataAttribute productMetadataAttribute;
	
	private Set<Object> value;

	public ProductMetadataAttribute getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(ProductMetadataAttribute productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
	}

	public Set<Object> getValue() {
		return value;
	}

	public void setValue(Set<Object> value) {
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
