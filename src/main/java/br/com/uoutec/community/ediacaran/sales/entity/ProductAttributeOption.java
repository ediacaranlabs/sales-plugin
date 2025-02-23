package br.com.uoutec.community.ediacaran.sales.entity;

public class ProductAttributeOption {

	private int id;
	
	private String value;
	
	private String description;

	public ProductAttributeOption() {
	}
	
	public ProductAttributeOption(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
