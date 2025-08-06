package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductRequestInvoiceIDEntity implements Serializable{

	private static final long serialVersionUID = -7914273839250723485L;

	@Column(name="cod_product_request", length=38)
	private String productRequestID;

	@Column(name="cod_invoice", length=32)
	private String invoiceID;

	public ProductRequestInvoiceIDEntity() {
	}
	
	public ProductRequestInvoiceIDEntity(String productRequestID, String invoiceID) {
		this.productRequestID = productRequestID;
		this.invoiceID = invoiceID;
	}

	public String getProductRequestID() {
		return productRequestID;
	}

	public String getInvoiceID() {
		return invoiceID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoiceID, productRequestID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRequestInvoiceIDEntity other = (ProductRequestInvoiceIDEntity) obj;
		return Objects.equals(invoiceID, other.invoiceID) && Objects.equals(productRequestID, other.productRequestID);
	}
	
}
