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
		name="rw_product_attr_val_idx",
		indexes = {
				@Index(columnList = "vlr_value, cod_product"),
				@Index(columnList = "dsc_value, cod_product")
		}
)
@EntityListeners(ProductAttributeValueIndexEntityListener.class)
public class ProductAttributeValueIndexEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@EmbeddedId
	private ProductAttributeValueEntityID id;

	@ManyToOne
	@JoinColumn(name = "cod_product", insertable = false, updatable = false)
	private ProductIndexEntity productIndex;

	@ManyToOne
	@JoinColumn(name="cod_product_metadata", insertable = false, updatable = false)
	private ProductMetadataEntity productMetadata;

	@ManyToOne
	@JoinColumn(name="cod_prod_mtda_attr", insertable = false, updatable = false)
	private ProductMetadataAttributeEntity attributeMetadata;
	
	@Column(name="cod_attribute", length=32)
	private String attributeID;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_type", length=32)
	private ProductAttributeValueEntityType type;
	
	@Column(name="vlr_value")
	private Long number;
	
	@Column(name="dsc_value", length=32)
	private String value;
	
	public ProductAttributeValueIndexEntity(){
	}
	
	public ProductAttributeValueIndexEntity(ProductAttributeValue e, Product product){
		this.id = new ProductAttributeValueEntityID(product.getId(), product.getMetadata(), e.getProductAttributeId());
		this.attributeID = e.getProductAttributeCode();
		
		this.productIndex = new ProductIndexEntity();
		this.productIndex.setId(product.getId());
		
		this.productMetadata = new ProductMetadataEntity();
		this.productMetadata.setId(product.getMetadata());

		this.attributeMetadata = new ProductMetadataAttributeEntity();
		this.attributeMetadata.setId(e.getProductAttributeId());
		
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

	public ProductIndexEntity getProductIndex() {
		return productIndex;
	}

	public void setProductIndex(ProductIndexEntity productIndex) {
		this.productIndex = productIndex;
	}

	public ProductMetadataEntity getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(ProductMetadataEntity productMetadata) {
		this.productMetadata = productMetadata;
	}

	public ProductMetadataAttributeEntity getAttributeMetadata() {
		return attributeMetadata;
	}

	public void setAttributeMetadata(ProductMetadataAttributeEntity attributeMetadata) {
		this.attributeMetadata = attributeMetadata;
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

	public Object parseValue() {
		switch (this.type) {
		case TEXT:
			return ProductAttributeValueEntityType.TEXT.toValue(this.value);
		case INTEGER:
			return ProductAttributeValueEntityType.INTEGER.toValue(this.value);
		case DECIMAL:
			return ProductAttributeValueEntityType.DECIMAL.toValue(this.value);
		case DATE:
			return ProductAttributeValueEntityType.DATE.toValue(this.value);
		case DATE_TIME:
			return ProductAttributeValueEntityType.DATE_TIME.toValue(this.value);
		case TIME:
			return ProductAttributeValueEntityType.TIME.toValue(this.value);
		}		

		return null;
	}
	public ProductAttributeValue toEntity() {
		return toEntity(null);
	}

	public ProductAttributeValue toEntity(ProductAttributeValue e) {
		
		if(e == null) {
			e = new ProductAttributeValue();
		}
		
		e.setProductAttributeCode(this.attributeID);
		e.setProductAttributeId(id.getProductMetadataAttributeID());

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
