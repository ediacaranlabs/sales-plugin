package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;

public class ProductAttributeSearchResultFilter {

	private String title;
	
	private int productMetadataAttribute;
	
	private boolean multiselect;
	
	private List<ProductAttributeSearchResultOptionFilter> options;

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

	public List<ProductAttributeSearchResultOptionFilter> getOptions() {
		return options;
	}

	public void setOptions(List<ProductAttributeSearchResultOptionFilter> options) {
		this.options = options;
	}

}
