package br.com.uoutec.community.ediacaran.sales.entity;

public class ProductAttributeSearchResultOptionFilter {

	private Object value;
	
	private String description;

	private ProductAttributeValueType type;
	
	private boolean selected;
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductAttributeValueType getType() {
		return type;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
