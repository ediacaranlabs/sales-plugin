package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;

@Entity
@Table(
		name="rw_product_attr_option",
		indexes = {
				@Index(columnList = "cod_option, dsc_value", unique = true),
				@Index(columnList = "cod_prod_mtda_attr")
		}
)
@EntityListeners(ProductMetadataAttributeOptionEntityListener.class)
public class ProductMetadataAttributeOptionEntity implements Serializable{

	private static final long serialVersionUID = -306114830092128632L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_option", length=11)
	private Integer id;
	
	@Column(name="dsc_value", length=128)
	private String value;

	@ManyToMany
	@JoinColumn(name = "cod_prod_mtda_attr")
	private ProductMetadataAttributeEntity 	productAttribute;
	
	@Column(name="dsc_description", length=256)
	private String description;
	
	public ProductMetadataAttributeOptionEntity() {
	}
	
	public ProductMetadataAttributeOptionEntity(ProductMetadataAttributeOption e) {
		this.id = e.getId() == 0? null : e.getId();
		this.description = e.getDescription();
		this.value = e.getValue();
		if(e.getProductMetadataAttribute() > 0) {
			this.productAttribute = new ProductMetadataAttributeEntity();
			this.productAttribute.setId(e.getProductMetadataAttribute());
		}
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

	public ProductMetadataAttributeEntity getProductAttribute() {
		return productAttribute;
	}

	public void setProductAttribute(ProductMetadataAttributeEntity productAttribute) {
		this.productAttribute = productAttribute;
	}

	public ProductMetadataAttributeOption toEntity() {
		return toEntity(null);
	}
	
	public ProductMetadataAttributeOption toEntity(ProductMetadataAttributeOption e) {
		if(e == null) {
			e = new ProductMetadataAttributeOption();
		}
		
		e.setDescription(this.description);
		e.setValue(this.value);
		e.setId(this.id == null? 0 : this.id.intValue());
		
		if(this.productAttribute != null){
			e.setProductMetadataAttribute(this.productAttribute.getId());
		}
		
		return e;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductMetadataAttributeOptionEntity other = (ProductMetadataAttributeOptionEntity) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
