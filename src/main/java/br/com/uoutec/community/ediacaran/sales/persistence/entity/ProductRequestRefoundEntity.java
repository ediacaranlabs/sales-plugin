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
@Table(name="rw_product_request_refound")
@EntityListeners(ProductRequestRefoundEntityListener.class)
public class ProductRequestRefoundEntity implements Serializable{

	private static final long serialVersionUID = -6395849000853228077L;

	@EmbeddedId
	private ProductRequestRefoundIDEntity id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_refound", referencedColumnName="cod_refound", insertable = false, updatable = false)
	private RefoundEntity refound;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_product_request", referencedColumnName="cod_product_request", insertable = false, updatable = false)
	private ProductRequestEntity productRequestEntity;
	
	@Column(name="vlr_units", length=11)
	private Integer units;
	
	public ProductRequestRefoundEntity(){
	}
	
	public ProductRequestRefoundEntity(RefoundEntity refound, ProductRequest e){
		this.id                   = new ProductRequestRefoundIDEntity(e.getId(), refound.getId());
		this.units                = e.getUnits();
		this.productRequestEntity = e == null? null : new ProductRequestEntity(refound, e);
		this.refound             = refound;
	}
	
	public ProductRequestRefoundIDEntity getId() {
		return id;
	}

	public void setId(ProductRequestRefoundIDEntity id) {
		this.id = id;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public RefoundEntity getRefound() {
		return refound;
	}

	public void setRefound(RefoundEntity refound) {
		this.refound = refound;
		this.id = 
			new ProductRequestRefoundIDEntity(
				productRequestEntity == null? null : productRequestEntity.getId(), 
				refound == null? null : refound.getId()
			);
	}

	public ProductRequestEntity getProductRequestEntity() {
		return productRequestEntity;
	}

	public void setProductRequestEntity(ProductRequestEntity productRequestEntity) {
		this.productRequestEntity = productRequestEntity;
		this.id = 
				new ProductRequestRefoundIDEntity(
					productRequestEntity == null? null : this.productRequestEntity.getId(), 
					refound == null? null : refound.getId()
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
