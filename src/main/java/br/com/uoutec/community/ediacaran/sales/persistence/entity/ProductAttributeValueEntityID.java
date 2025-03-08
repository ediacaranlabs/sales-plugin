package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductAttributeValueEntityID implements Serializable{

	private static final long serialVersionUID = -78362272347002284L;

	@Column(name="cod_product", length=11)
	private Integer productID;
	
	@Column(name="cod_prod_mtda_attr", length=11)
	private Integer metadataAttributeID;

	public ProductAttributeValueEntityID(Integer productID, Integer metadataAttributeID) {
		this.productID = productID;
		this.metadataAttributeID = metadataAttributeID;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public Integer getMetadataAttributeID() {
		return metadataAttributeID;
	}

	public void setMetadataAttributeID(Integer metadataAttributeID) {
		this.metadataAttributeID = metadataAttributeID;
	}

	
}
