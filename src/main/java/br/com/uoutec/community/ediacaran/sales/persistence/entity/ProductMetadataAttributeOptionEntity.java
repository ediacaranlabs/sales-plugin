package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;

@Entity
@Table(
		name="rw_product_attr_option",
		indexes = {
				@Index(columnList = "cod_prod_mtda_attr, dsc_value", unique = true),
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

	@ManyToOne
	@JoinColumn(name = "cod_prod_mtda_attr")
	private ProductMetadataAttributeEntity 	productAttribute;
	
	@Column(name="dsc_description", length=256)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name="set_type", length=32)
	private ProductAttributeValueEntityType type;
	
	@Column(name="vlr_value")
	private Long number;

	@Column(name="dsc_value", length=32)
	private String value;
	
	public ProductMetadataAttributeOptionEntity() {
	}
	
	public ProductMetadataAttributeOptionEntity(ProductMetadataAttributeOption e) {
		this.id = e.getId() == 0? null : e.getId();
		this.description = e.getDescription();

		if(e.getProductMetadataAttribute() > 0) {
			this.productAttribute = new ProductMetadataAttributeEntity();
			this.productAttribute.setId(e.getProductMetadataAttribute());
		}
		
		switch (e.getValueType()) {
		case TEXT:
			this.value = (String)ProductAttributeValueEntityType.TEXT.parse(e.getValue());
			this.type = ProductAttributeValueEntityType.TEXT;
			break;
		case INTEGER:
			this.number = (Long)ProductAttributeValueEntityType.INTEGER.parse(e.getValue());
			this.type = ProductAttributeValueEntityType.INTEGER;
			break;
		case DECIMAL:
			this.number = (Long)ProductAttributeValueEntityType.DECIMAL.parse(e.getValue());
			this.type = ProductAttributeValueEntityType.DECIMAL;
			break;
		case DATE:
			this.number = (Long)ProductAttributeValueEntityType.DATE.parse(e.getValue());
			this.type = ProductAttributeValueEntityType.DATE;
			break;
		case DATE_TIME:
			this.number = (Long)ProductAttributeValueEntityType.DATE_TIME.parse(e.getValue());
			this.type = ProductAttributeValueEntityType.DATE_TIME;
			break;
		case TIME:
			this.number = (Long)ProductAttributeValueEntityType.TIME.parse(e.getValue());
			this.type = ProductAttributeValueEntityType.TIME;
			break;
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
		
		Object val = null;
		ProductAttributeValueType t = null;
		switch (this.type) {
		case TEXT:
			val = ProductAttributeValueEntityType.TEXT.toValue(this.value);
			t = ProductAttributeValueType.TEXT;
			break;
		case INTEGER:
			val = ProductAttributeValueEntityType.INTEGER.toValue(this.number);
			t = ProductAttributeValueType.INTEGER;
			break;
		case DECIMAL:
			val = ProductAttributeValueEntityType.DECIMAL.toValue(this.number);
			t = ProductAttributeValueType.DECIMAL;
			break;
		case DATE:
			val = ProductAttributeValueEntityType.DATE.toValue(this.number);
			t = ProductAttributeValueType.DATE;
			break;
		case DATE_TIME:
			val = ProductAttributeValueEntityType.DATE_TIME.toValue(this.number);
			t = ProductAttributeValueType.DATE_TIME;
			break;
		case TIME:
			val = ProductAttributeValueEntityType.TIME.toValue(this.number);
			t = ProductAttributeValueType.TIME;
			break;
		}		

		e.setValueType(t);
		e.setValue(val);
		
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
