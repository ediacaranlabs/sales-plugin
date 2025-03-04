package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;

public class ProductMetadataAttributeUpdate extends ProductMetadataAttribute{

	private List<ProductMetadataAttributeOption> registerOptions;
	
	private List<ProductMetadataAttributeOption> unregisterOptions;

	public ProductMetadataAttributeUpdate() {
	}
	
	public ProductMetadataAttributeUpdate(ProductMetadataAttribute value) {
		setAllowEmpty(value.isAllowEmpty());
		setCode(value.getCode());
		setDescription(value.getDescription());
		setId(value.getId());
		setMax(value.getMax());
		setMaxLength(value.getMaxLength());
		setMin(value.getMin());
		setMinLength(value.getMinLength());
		setName(value.getName());
		setOptions(value.getOptions());
		setOrder(value.getOrder());
		setPrefix(value.getPrefix());
		setProductMetadata(value.getProductMetadata());
		setRegex(value.getRegex());
		setRows(value.getRows());
		setSuffix(value.getSuffix());
		setType(value.getType());
		setValueType(value.getValueType());
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
