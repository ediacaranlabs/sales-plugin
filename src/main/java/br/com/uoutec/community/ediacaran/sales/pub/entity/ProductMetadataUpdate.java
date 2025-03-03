package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;

public class ProductMetadataUpdate extends ProductMetadata {

	private List<ProductMetadataAttributeUpdate> registerAttributes;
	
	private List<ProductMetadataAttributeUpdate> unregisterAttributes;

	public ProductMetadataUpdate() {
	}
	
	public ProductMetadataUpdate(ProductMetadata value) {
		this.setAttributes(value.getAttributes());
		this.setDescription(value.getDescription());
		this.setId(value.getId());
		this.setName(value.getName());
		this.setThumb(value.getThumb());
	}

	public List<ProductMetadataAttributeUpdate> getRegisterAttributes() {
		return registerAttributes;
	}

	public void setRegisterAttributes(List<ProductMetadataAttributeUpdate> registerAttributes) {
		this.registerAttributes = registerAttributes;
	}

	public List<ProductMetadataAttributeUpdate> getUnregisterAttributes() {
		return unregisterAttributes;
	}

	public void setUnregisterAttributes(List<ProductMetadataAttributeUpdate> unregisterAttributes) {
		this.unregisterAttributes = unregisterAttributes;
	}
	
}
