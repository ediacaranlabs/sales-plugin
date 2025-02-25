package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.i18n.ValidationException;

public class ProductMetadataAttributeUpdate extends ProductMetadataAttribute{

	private List<ProductMetadataAttributeOption> registerOptions;
	
	private List<ProductMetadataAttributeOption> unregisterOptions;

	private ProductMetadataAttribute advice;
	
	public ProductMetadataAttributeUpdate(ProductMetadataAttribute advice) {
		this.advice = advice;
	}

	public int getId() {
		return advice.getId();
	}

	public void setId(int id) {
		advice.setId(id);
	}

	public String getCode() {
		return advice.getCode();
	}

	public void setCode(String code) {
		advice.setCode(code);
	}

	public short getMinLength() {
		return advice.getMinLength();
	}

	public void setMinLength(short minLength) {
		advice.setMinLength(minLength);
	}

	public short getMaxLength() {
		return advice.getMaxLength();
	}

	public void setMaxLength(short maxLength) {
		advice.setMaxLength(maxLength);
	}

	public Double getMin() {
		return advice.getMin();
	}

	public void setMin(Double min) {
		advice.setMin(min);
	}

	public Double getMax() {
		return advice.getMax();
	}

	public void setMax(Double max) {
		advice.setMax(max);
	}

	public String getName() {
		return advice.getName();
	}

	public void setName(String name) {
		advice.setName(name);
	}

	public ProductAttributeType getType() {
		return advice.getType();
	}

	public void setType(ProductAttributeType type) {
		advice.setType(type);
	}

	public ProductAttributeValueType getValueType() {
		return advice.getValueType();
	}

	public void setValueType(ProductAttributeValueType valueType) {
		advice.setValueType(valueType);
	}

	public String getDescription() {
		return advice.getDescription();
	}

	public void setDescription(String description) {
		advice.setDescription(description);
	}

	public List<ProductMetadataAttributeOption> getOptions() {
		return advice.getOptions();
	}

	public void setOptions(List<ProductMetadataAttributeOption> options) {
		advice.setOptions(options);
	}

	public boolean isAllowEmpty() {
		return advice.isAllowEmpty();
	}

	public void setAllowEmpty(boolean allowEmpty) {
		advice.setAllowEmpty(allowEmpty);
	}

	public short getRows() {
		return advice.getRows();
	}

	public void setRows(short rows) {
		advice.setRows(rows);
	}

	public String getRegex() {
		return advice.getRegex();
	}

	public void setRegex(String regex) {
		advice.setRegex(regex);
	}

	public short getOrder() {
		return advice.getOrder();
	}

	public void setOrder(short order) {
		advice.setOrder(order);
	}

	public int getProductMetadata() {
		return advice.getProductMetadata();
	}

	public void setProductMetadata(int productMetadata) {
		advice.setProductMetadata(productMetadata);
	}

	public void validate(String value) throws ValidationException {
		advice.validate(value);
	}

	public int hashCode() {
		return advice.hashCode();
	}

	public boolean equals(Object obj) {
		return advice.equals(obj);
	}

	public String toString() {
		return advice.toString();
	}

	public List<ProductMetadataAttributeOption> getRegisterOptions() {
		return registerOptions;
	}

	public void setRegisterOptions(List<ProductMetadataAttributeOption> registerOptions) {
		this.registerOptions = registerOptions;
	}

	public List<ProductMetadataAttributeOption> getUnregisterOptions() {
		return unregisterOptions;
	}

	public void setUnregisterOptions(List<ProductMetadataAttributeOption> unregisterOptions) {
		this.unregisterOptions = unregisterOptions;
	}
	
}
