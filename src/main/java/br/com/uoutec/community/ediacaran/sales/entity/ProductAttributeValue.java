package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductAttributeValue {

	private ProductAttributeValueType type;
	
	private int productAttributeId;
	
	private String productAttributeCode;
	
	private Set<Object> values;

	public ProductAttributeValue() {
	}

	public ProductAttributeValue(int productAttributeId, String productAttributeCode, ProductAttributeValueType type, Set<Object> values) {
		this.productAttributeId = productAttributeId;
		this.productAttributeCode = productAttributeCode;
		this.type = type;
		this.values = values;
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
		return values == null || values.isEmpty()? null : values.iterator().next();
	}

	public Object[] getValues() {
		return values == null? null : values.stream().toArray(Object[]::new);
	}

	public Set<Object> getSetValues() {
		return values == null? null : values.stream().collect(Collectors.toSet());
	}
	
	public void addValue(Object value) {
		this.values.add(value);
	}
	
	public void setValue(Object ... value) {
		this.values = Arrays.stream(value).collect(Collectors.toSet());
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
