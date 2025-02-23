package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.ediacaran.core.plugins.PublicType;

@Entity
@Table(name="rw_product_attr_val")
@EntityListeners(ProductAttributeValueEntityListener.class)
public class ProductAttributeValueEntity implements Serializable,PublicType{

	private static final long serialVersionUID = 7360107228997614767L;

	@EmbeddedId
	private ProductAttributeValueEntityID id;

	@ManyToOne
	@JoinColumn(name = "cod_product", insertable = false, updatable = false)
	private ProductEntity product;
	
	@Column(name="dsc_value", length=128)
	private String value;
	
	public ProductAttributeValueEntity(){
	}
	
	public ProductAttributeValueEntity(ProductEntity e, String attribute, String value){
		this.id = new ProductAttributeValueEntityID(e.getId(), attribute);
		this.product = e;
		this.value = value;
	}

	public ProductAttributeValueEntityID getId() {
		return id;
	}

	public void setId(ProductAttributeValueEntityID id) {
		this.id = id;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toEntity() {
		return value;
	}
	
}
