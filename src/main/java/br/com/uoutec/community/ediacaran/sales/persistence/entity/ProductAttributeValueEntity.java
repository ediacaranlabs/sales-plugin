package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValue;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;

@Entity
@Table(
		name="rw_product_attr_val",
		indexes = {
				@Index(columnList = "vlr_value, cod_product"),
				@Index(columnList = "dsc_value, cod_product")
		}
)
@EntityListeners(ProductAttributeValueEntityListener.class)
public class ProductAttributeValueEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@EmbeddedId
	private ProductAttributeValueEntityID id;

	@ManyToOne
	@JoinColumn(name = "cod_product", insertable = false, updatable = false)
	private ProductEntity product;
	
	@Column(name="cod_attribute", length=32)
	private String attributeID;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_type", length=32)
	private ProductAttributeValueEntityType type;
	
	@Column(name="vlr_value")
	private Long number;
	
	@Column(name="dsc_value", length=32)
	private String value;
	
	public ProductAttributeValueEntity(){
	}
	
	public ProductAttributeValueEntity(ProductAttributeValue e, Product product){
		this.id = new ProductAttributeValueEntityID(product.getId(), e.getProductAttributeId());
		this.product = new ProductEntity();
		this.product.setId(product.getId());

		switch (e.getType()) {
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
	
	public String getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(String attributeID) {
		this.attributeID = attributeID;
	}

	public ProductAttributeValueEntityType getType() {
		return type;
	}

	public void setType(ProductAttributeValueEntityType type) {
		this.type = type;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public ProductAttributeValue toEntity() {
		return toEntity(null);
	}

	public ProductAttributeValue toEntity(ProductAttributeValue e) {
		
		if(e == null) {
			e = new ProductAttributeValue();
		}
		
		e.setProductAttributeCode(this.attributeID);
		e.setProductAttributeId(id.getMetadataAttributeID());

		Object val = null;
		ProductAttributeValueType t = null;
		
		switch (this.type) {
		case TEXT:
			val = (String)ProductAttributeValueEntityType.TEXT.toValue(e.getValue());
			t = ProductAttributeValueType.TEXT;
			break;
		case INTEGER:
			val = (String)ProductAttributeValueEntityType.INTEGER.toValue(e.getValue());
			t = ProductAttributeValueType.INTEGER;
			break;
		case DECIMAL:
			val = (String)ProductAttributeValueEntityType.DECIMAL.toValue(e.getValue());
			t = ProductAttributeValueType.DECIMAL;
			break;
		case DATE:
			val = (String)ProductAttributeValueEntityType.DATE.toValue(e.getValue());
			t = ProductAttributeValueType.DATE;
			break;
		case DATE_TIME:
			val = (String)ProductAttributeValueEntityType.DATE_TIME.toValue(e.getValue());
			t = ProductAttributeValueType.DATE_TIME;
			break;
		case TIME:
			val = (String)ProductAttributeValueEntityType.TIME.toValue(e.getValue());
			t = ProductAttributeValueType.TIME;
			break;
		}		

		e.setType(t);
		e.setValue(val);
		
		return e;
	}
	
}
