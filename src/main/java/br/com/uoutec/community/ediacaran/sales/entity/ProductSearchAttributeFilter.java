package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;
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
