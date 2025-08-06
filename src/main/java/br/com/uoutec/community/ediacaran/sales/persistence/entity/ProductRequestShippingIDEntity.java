package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductRequestShippingIDEntity implements Serializable{

	private static final long serialVersionUID = -7914273839250723485L;

	@Column(name="cod_product_request", length=38)
	private String productRequestID;

	@Column(name="cod_shipping", length=32)
	private String shippingID;

	public ProductRequestShippingIDEntity() {
	}
	
	public ProductRequestShippingIDEntity(String productRequestID, String shippingID) {
		this.productRequestID = productRequestID;
		this.shippingID = shippingID;
	}

	public String getProductRequestID() {
		return productRequestID;
	}

	public String getShippingID() {
		return shippingID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(shippingID, productRequestID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRequestShippingIDEntity other = (ProductRequestShippingIDEntity) obj;
		return Objects.equals(shippingID, other.shippingID) && Objects.equals(productRequestID, other.productRequestID);
	}
	
}
