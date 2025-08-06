package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;

@Entity
@Table(name="rw_product_request_invoice")
@EntityListeners(ProductRequestEntityListener.class)
public class ProductRequestInvoiceEntity implements Serializable{

	private static final long serialVersionUID = -6395849000853228077L;

	@EmbeddedId
	private ProductRequestInvoiceIDEntity id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_invoice", referencedColumnName="cod_invoice", insertable = false, updatable = false)
	private InvoiceEntity invoice;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_product_request", referencedColumnName="cod_product_request", insertable = false, updatable = false)
	private ProductRequestEntity productRequestEntity;
	
	@Column(name="vlr_units", length=11)
	private Integer units;

	
	public ProductRequestInvoiceEntity(){
	}
	
	public ProductRequestInvoiceEntity(InvoiceEntity invoice, ProductRequest e){
		this.id                   = new ProductRequestInvoiceIDEntity(e.getId(), invoice.getId());
		this.units                = e.getUnits();
		this.productRequestEntity = e == null? null : new ProductRequestEntity(invoice, e);
		this.invoice              = invoice;
	}
	
	public ProductRequestInvoiceIDEntity getId() {
		return id;
	}

	public void setId(ProductRequestInvoiceIDEntity id) {
		this.id = id;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public InvoiceEntity getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceEntity invoice) {
		this.invoice = invoice;
		this.id = 
			new ProductRequestInvoiceIDEntity(
				productRequestEntity == null? null : productRequestEntity.getId(), 
				invoice == null? null : invoice.getId()
			);
	}

	public ProductRequestEntity getProductRequestEntity() {
		return productRequestEntity;
	}

	public void setProductRequestEntity(ProductRequestEntity productRequestEntity) {
		this.productRequestEntity = productRequestEntity;
		this.id = 
				new ProductRequestInvoiceIDEntity(
					productRequestEntity == null? null : this.productRequestEntity.getId(), 
					invoice == null? null : invoice.getId()
				);
	}

	public ProductRequest toEntity(){
		ProductRequest e = new ProductRequest();
		
		if(this.productRequestEntity != null) {
			e = this.productRequestEntity.toEntity(e);
		}
		
		e.setId(this.id.getProductRequestID());
		e.setUnits(this.units);
		
		return e;
	}
	
}
