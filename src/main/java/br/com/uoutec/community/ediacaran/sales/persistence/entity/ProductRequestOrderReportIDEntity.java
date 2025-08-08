package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductRequestOrderReportIDEntity implements Serializable{

	private static final long serialVersionUID = -7914273839250723485L;

	@Column(name="cod_product_request", length=38)
	private String productRequestID;

	@Column(name="cod_order_report", length=32)
	private String orderReportID;

	public ProductRequestOrderReportIDEntity() {
	}
	
	public ProductRequestOrderReportIDEntity(String productRequestID, String orderReportID) {
		this.productRequestID = productRequestID;
		this.orderReportID = orderReportID;
	}

	public String getProductRequestID() {
		return productRequestID;
	}

	public String getOrderReportID() {
		return orderReportID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderReportID, productRequestID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRequestOrderReportIDEntity other = (ProductRequestOrderReportIDEntity) obj;
		return Objects.equals(orderReportID, other.orderReportID) && Objects.equals(productRequestID, other.productRequestID);
	}
	
}
