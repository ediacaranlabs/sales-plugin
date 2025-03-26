package br.com.uoutec.community.ediacaran.sales.pub.entity;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;

public class ProductMetadataAttributeOptionUpdate extends ProductMetadataAttributeOption{

	private int index;
	
	public ProductMetadataAttributeOptionUpdate() {
	}
	
	public ProductMetadataAttributeOptionUpdate(ProductMetadataAttributeOption e) {
		super.setDescription(e.getDescription());
		super.setId(e.getId());
		super.setProductMetadataAttribute(e.getProductMetadataAttribute());
		super.setValue(e.getValue());
		super.setValueType(e.getValueType());
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
