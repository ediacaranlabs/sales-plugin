package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductAttributeValueEntityID implements Serializable{

	private static final long serialVersionUID = -78362272347002284L;

	@Column(name="cod_product", length=11)
	private Integer productID;
	
	@Column(name="dsc_value", length=32)
	private String value;
	
	@Column(name="cod_prod_mtda_attr", length=11)
	private Integer productMetadataAttributeID;

	public ProductAttributeValueEntityID() {
	}

	public ProductAttributeValueEntityID(String value, Integer productMetadataAttributeID, Integer productID) {
		this.productID = productID;
		this.productMetadataAttributeID = productMetadataAttributeID;
		this.value = value;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getProductMetadataAttributeID() {
		return productMetadataAttributeID;
	}

	public void setProductMetadataAttributeID(Integer productMetadataAttributeID) {
		this.productMetadataAttributeID = productMetadataAttributeID;
	}

	
	
}
