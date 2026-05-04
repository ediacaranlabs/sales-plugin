package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductRequestRefoundIDEntity implements Serializable{

	private static final long serialVersionUID = -7914273839250723485L;

	@Column(name="cod_product_request", length=38)
	private String productRequestID;

	@Column(name="cod_refund", length=32)
	private String refoundID;
	

	public ProductRequestRefoundIDEntity() {
	}
	
	public ProductRequestRefoundIDEntity(String productRequestID, String refoundID) {
		this.productRequestID = productRequestID;
		this.refoundID = refoundID;
	}

	public String getProductRequestID() {
		return productRequestID;
	}

	public String getRefoundID() {
		return refoundID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(refoundID, productRequestID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRequestRefoundIDEntity other = (ProductRequestRefoundIDEntity) obj;
		return Objects.equals(refoundID, other.refoundID) && Objects.equals(productRequestID, other.productRequestID);
	}
	
}
