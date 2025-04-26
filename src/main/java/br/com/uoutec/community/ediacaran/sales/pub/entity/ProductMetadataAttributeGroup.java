package br.com.uoutec.community.ediacaran.sales.pub.entity;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;

public class ProductMetadataAttributeGroup {

	private ProductMetadataAttribute entity;
	
	private ProductMetadataAttributePubEntity pubEntity;

	public ProductMetadataAttributeGroup(ProductMetadataAttribute entity, ProductMetadataAttributePubEntity pubEntity) {
		this.entity = entity;
		this.pubEntity = pubEntity;
	}

	public ProductMetadataAttribute getEntity() {
		return entity;
	}

	public void setEntity(ProductMetadataAttribute entity) {
		this.entity = entity;
	}

	public ProductMetadataAttributePubEntity getPubEntity() {
		return pubEntity;
	}

	public void setPubEntity(ProductMetadataAttributePubEntity pubEntity) {
		this.pubEntity = pubEntity;
	}
	
}
