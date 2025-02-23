package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductAttributeValueEntityID implements Serializable{

	private static final long serialVersionUID = -78362272347002284L;

	@Column(name="cod_product", length=11)
	private Integer productID;
	
	@Column(name="cod_attribute", length=32)
	private String attributeID;

	public ProductAttributeValueEntityID(Integer productID, String attributeID) {
		this.productID = productID;
		this.attributeID = attributeID;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(String attributeID) {
		this.attributeID = attributeID;
	}
	
}
