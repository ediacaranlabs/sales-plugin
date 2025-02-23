package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeOption;

@Entity
@Table(name="rw_product_attr_option")
@EntityListeners(ProductAttributeOptionEntityListener.class)
public class ProductAttributeOptionEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_option", length=11)
	private Integer id;
	
	@Column(name="dsc_value", length=128)
	private String value;
	
	@Column(name="dsc_description", length=256)
	private String description;
	
	public ProductAttributeOptionEntity() {
	}
	
	public ProductAttributeOptionEntity(ProductAttributeOption e) {
		this.id = e.getId() == 0? null : e.getId();
		this.description = e.getDescription();
		this.value = e.getValue();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public ProductAttributeOption toEntity() {
		return toEntity(null);
	}
	
	public ProductAttributeOption toEntity(ProductAttributeOption e) {
		if(e == null) {
			e = new ProductAttributeOption();
		}
		
		e.setDescription(this.description);
		e.setValue(this.value);
		e.setId(this.id == null? 0 : this.id.intValue());
		
		return e;
	}
}
