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
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReportCause;

@Entity
@Table(name="rw_product_request_order_report")
@EntityListeners(ProductRequestOrderReportEntityListener.class)
public class ProductRequestOrderReportEntity implements Serializable {

	private static final long serialVersionUID = -6395849000853228077L;

	@EmbeddedId
	private ProductRequestOrderReportIDEntity id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_order_report", referencedColumnName="cod_order_report", insertable = false, updatable = false)
	private OrderReportEntity orderReport;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_product_request", referencedColumnName="cod_product_request", insertable = false, updatable = false)
	private ProductRequestEntity productRequestEntity;
	
	@Column(name="set_cause", length=11)
	private ProductRequestReportCause cause;

	
	public ProductRequestOrderReportEntity(){
	}
	
	public ProductRequestOrderReportEntity(OrderReportEntity orderReport, ProductRequest e){
		this.id                   = new ProductRequestOrderReportIDEntity(e.getId(), orderReport.getId());
		this.productRequestEntity = e == null? null : new ProductRequestEntity(e);
	}
	
	public ProductRequestOrderReportIDEntity getId() {
		return id;
	}

	public void setId(ProductRequestOrderReportIDEntity id) {
		this.id = id;
	}

	public ProductRequestReportCause getCause() {
		return cause;
	}

	public void setCause(ProductRequestReportCause cause) {
		this.cause = cause;
	}

	public OrderReportEntity getOrderReport() {
		return orderReport;
	}

	public void setOrderReport(OrderReportEntity orderReport) {
		this.orderReport = orderReport;
		this.id = 
			new ProductRequestOrderReportIDEntity(
				productRequestEntity == null? null : productRequestEntity.getId(), 
				orderReport == null? null : orderReport.getId()
			);
	}

	public ProductRequestEntity getProductRequestEntity() {
		return productRequestEntity;
	}

	public void setProductRequestEntity(ProductRequestEntity productRequestEntity) {
		this.productRequestEntity = productRequestEntity;
		this.id = 
				new ProductRequestOrderReportIDEntity(
					productRequestEntity == null? null : this.productRequestEntity.getId(), 
					orderReport == null? null : orderReport.getId()
				);
	}

	public ProductRequestReport toEntity(){
		return toEntity(null);
	}
	
	public ProductRequestReport toEntity(ProductRequestReport e){

		if(e == null) {
			e = new ProductRequestReport();
		}
		
		if(this.productRequestEntity != null) {
			e = (ProductRequestReport)this.productRequestEntity.toEntity(e);
		}
		
		e.setCause(this.cause);
		
		return e;
	}
	
}
