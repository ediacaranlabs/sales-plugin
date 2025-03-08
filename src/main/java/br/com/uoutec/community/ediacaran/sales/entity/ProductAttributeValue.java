package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;

public class ProductAttributeValue {

	private ProductAttributeValueType type;
	
	private int productAttributeId;
	
	private String productAttributeCode;
	
	private Object value;

	public ProductAttributeValue() {
	}

	public ProductAttributeValue(int productAttributeId, String productAttributeCode, ProductAttributeValueType type, Object value) {
		this.productAttributeId = productAttributeId;
		this.productAttributeCode = productAttributeCode;
		this.type = type;
		this.value = value;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
	}

	public void setProductAttributeId(int productAttributeId) {
		this.productAttributeId = productAttributeId;
	}

	public void setProductAttributeCode(String productAttributeCode) {
		this.productAttributeCode = productAttributeCode;
	}

	public int getProductAttributeId() {
		return productAttributeId;
	}

	public String getProductAttributeCode() {
		return productAttributeCode;
	}

	public ProductAttributeValueType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productAttributeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductAttributeValue other = (ProductAttributeValue) obj;
		return productAttributeId == other.productAttributeId;
	}
	
}
