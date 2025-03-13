package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;

public class ProductMetadataSearchResultFilter {

	private String title;
	
	private int productMetadata;
	
	private List<ProductMetadataAttributeSearchResultFilter> filters;

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

	public List<ProductMetadataAttributeSearchResultFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ProductMetadataAttributeSearchResultFilter> filters) {
		this.filters = filters;
	}
	
}
