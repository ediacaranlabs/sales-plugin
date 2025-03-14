package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;
import java.util.Set;

public class ProductSearchFilter {

	private ProductMetadata productMetadata;

	private Set<ProductSearchAttributeFilter> attributeFilters;

	public ProductMetadata getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(ProductMetadata productMetadata) {
		this.productMetadata = productMetadata;
	}

	public Set<ProductSearchAttributeFilter> getAttributeFilters() {
		return attributeFilters;
	}

	public void setAttributeFilters(Set<ProductSearchAttributeFilter> attributeFilters) {
		this.attributeFilters = attributeFilters;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productMetadata);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductSearchFilter other = (ProductSearchFilter) obj;
		return Objects.equals(productMetadata, other.productMetadata);
	}
	
}
