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
@Table(name="rw_product_request_shipping")
@EntityListeners(ProductRequestEntityListener.class)
public class ProductRequestShippingEntity implements Serializable{

	private static final long serialVersionUID = -6395849000853228077L;

	@EmbeddedId
	private ProductRequestShippingIDEntity id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_shipping", referencedColumnName="cod_shipping", insertable = false, updatable = false)
	private ShippingEntity shipping;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_product_request", referencedColumnName="cod_product_request", insertable = false, updatable = false)
	private ProductRequestEntity productRequestEntity;
	
	@Column(name="vlr_units", length=11)
	private Integer units;

	
	public ProductRequestShippingEntity(){
	}
	
	public ProductRequestShippingEntity(ShippingEntity shipping, ProductRequest e){
		this.id                   = new ProductRequestShippingIDEntity(e.getId(), shipping.getId());
		this.units                = e.getUnits();
		this.productRequestEntity = e == null? null : new ProductRequestEntity(shipping, e);
		this.shipping             = shipping;
	}
	
	public ProductRequestShippingIDEntity getId() {
		return id;
	}

	public void setId(ProductRequestShippingIDEntity id) {
		this.id = id;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public ShippingEntity getShipping() {
		return shipping;
	}

	public void setShipping(ShippingEntity shipping) {
		this.shipping = shipping;
		this.id = 
			new ProductRequestShippingIDEntity(
				productRequestEntity == null? null : productRequestEntity.getId(), 
				shipping == null? null : shipping.getId()
			);
	}

	public ProductRequestEntity getProductRequestEntity() {
		return productRequestEntity;
	}

	public void setProductRequestEntity(ProductRequestEntity productRequestEntity) {
		this.productRequestEntity = productRequestEntity;
		this.id = 
				new ProductRequestShippingIDEntity(
					productRequestEntity == null? null : this.productRequestEntity.getId(), 
					shipping == null? null : shipping.getId()
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
