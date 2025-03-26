package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;

public class ProductMetadataAttributeUpdate extends ProductMetadataAttribute{

	private List<ProductMetadataAttributeOptionUpdate> registerOptions;
	
	private List<ProductMetadataAttributeOptionUpdate> unregisterOptions;

	private int index;
	
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<ProductMetadataAttributeOptionUpdate> getRegisterOptions() {
		return registerOptions;
	}

	public void setRegisterOptions(List<ProductMetadataAttributeOptionUpdate> registerOptions) {
		this.registerOptions = registerOptions;
	}

	public List<ProductMetadataAttributeOptionUpdate> getUnregisterOptions() {
		return unregisterOptions;
	}

	public void setUnregisterOptions(List<ProductMetadataAttributeOptionUpdate> unregisterOptions) {
		this.unregisterOptions = unregisterOptions;
	}
	
}
