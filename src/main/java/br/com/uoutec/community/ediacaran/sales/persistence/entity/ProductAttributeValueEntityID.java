package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductAttributeValueEntityID implements Serializable{

	private static final long serialVersionUID = -78362272347002284L;

	@Column(name="cod_product", length=11)
	private Integer productID;
	
	@Column(name="cod_product_metadata", length=11)
	private Integer productMetadataID;
	
	@Column(name="cod_prod_mtda_attr", length=11)
	private Integer productMetadataAttributeID;

	public ProductAttributeValueEntityID() {
	}

	public ProductAttributeValueEntityID(Integer productID, Integer productMetadataID,
			Integer productMetadataAttributeID) {
		this.productID = productID;
		this.productMetadataID = productMetadataID;
		this.productMetadataAttributeID = productMetadataAttributeID;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public Integer getProductMetadataID() {
		return productMetadataID;
	}

	public void setProductMetadataID(Integer productMetadataID) {
		this.productMetadataID = productMetadataID;
	}

	public Integer getProductMetadataAttributeID() {
		return productMetadataAttributeID;
	}

	public void setProductMetadataAttributeID(Integer productMetadataAttributeID) {
		this.productMetadataAttributeID = productMetadataAttributeID;
	}

	
	
}
