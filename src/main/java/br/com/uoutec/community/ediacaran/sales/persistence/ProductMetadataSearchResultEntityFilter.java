package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;

public class ProductMetadataSearchResultEntityFilter {

	private ProductMetadata productMetadata;
	
	private Map<Integer,ProductMetadataAttributeSearchResultEntityFilter> filters;

	public ProductMetadata getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(ProductMetadata productMetadata) {
		this.productMetadata = productMetadata;
	}

	public Map<Integer, ProductMetadataAttributeSearchResultEntityFilter> getFilters() {
		return filters;
	}

	public void setFilters(Map<Integer, ProductMetadataAttributeSearchResultEntityFilter> filters) {
		this.filters = filters;
	}

	
}
