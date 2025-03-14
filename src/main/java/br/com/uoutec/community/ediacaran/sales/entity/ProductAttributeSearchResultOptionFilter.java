package br.com.uoutec.community.ediacaran.sales.entity;

public class ProductAttributeSearchResultOptionFilter {

	private int optionId;
	
	private Object value;
	
	private String description;

	private ProductAttributeValueType type;
	
	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

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
	
}
