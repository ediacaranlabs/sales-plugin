package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;

public class ProductMetadataAttributeSearchResultFilter {

	private String title;
	
	private int productMetadataAttribute;
	
	private boolean multiselect;
	
	private List<ProductMetadataAttributeSearchResultOptionFilter> options;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(int productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
	}

	public boolean isMultiselect() {
		return multiselect;
	}

	public void setMultiselect(boolean multiselect) {
		this.multiselect = multiselect;
	}

	public List<ProductMetadataAttributeSearchResultOptionFilter> getOptions() {
		return options;
	}

	public void setOptions(List<ProductMetadataAttributeSearchResultOptionFilter> options) {
		this.options = options;
	}

}
