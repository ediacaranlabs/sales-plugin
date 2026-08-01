package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;

public class ProductAttribute {

	private String name;
	
	private String value;

	private String description;
	
	public ProductAttribute() {
	}
	
	public ProductAttribute(String name, String value, String description) {
		super();
		this.name = name;
		this.value = value;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public String toString() {
		return description == null? value : description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductAttribute other = (ProductAttribute) obj;
		return Objects.equals(name, other.name);
	}
	
}
