package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValue;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;

@Entity
@Table(
		name="rw_product_attr_val"
)
@EntityListeners(ProductAttributeValueEntityListener.class)
public class ProductAttributeValueEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@EmbeddedId
	private ProductAttributeValueEntityID id;

	@ManyToOne
	@JoinColumn(name = "cod_product", insertable = false, updatable = false)
	private ProductEntity product;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_product", insertable = false, updatable = false)
	private ProductIndexEntity productIndex;

	@Column(name="cod_product_metadata", length=11)
	private Integer productMetadataID;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_type", length=32)
	private ProductAttributeValueEntityType type;
	
	@Column(name="cod_attribute", length=32)
	private String productAttributeCode;
	
	@Column(name="vlr_value")
	private Long number;
	
	public ProductAttributeValueEntity(){
	}
	
	public ProductAttributeValueEntity(Object value, ProductAttributeValue e, Product product){
		this.id = new ProductAttributeValueEntityID(e.getType().toString(value, null), e.getProductAttributeId(), product.getId());
		this.productAttributeCode = e.getProductAttributeCode();
		this.productMetadataID = e.getProductMetadataId();
		//this.product = new ProductEntity();
		//this.product.setId(product.getId());
		
		//this.productIndex = new ProductIndexEntity();
		//this.productIndex.setId(product.getId());
		
		switch (e.getType()) {
		case TEXT:
			//this.value = (String)ProductAttributeValueEntityType.TEXT.parse(e.getValue()); // PK
			this.type = ProductAttributeValueEntityType.TEXT;
			break;
		case INTEGER:
			this.number = (Long)ProductAttributeValueEntityType.INTEGER.parse(value);
			this.type = ProductAttributeValueEntityType.INTEGER;
			break;
		case DECIMAL:
			this.number = (Long)ProductAttributeValueEntityType.DECIMAL.parse(value);
			this.type = ProductAttributeValueEntityType.DECIMAL;
			break;
		case DATE:
			this.number = (Long)ProductAttributeValueEntityType.DATE.parse(value);
			this.type = ProductAttributeValueEntityType.DATE;
			break;
		case DATE_TIME:
			this.number = (Long)ProductAttributeValueEntityType.DATE_TIME.parse(value);
			this.type = ProductAttributeValueEntityType.DATE_TIME;
			break;
		case TIME:
			this.number = (Long)ProductAttributeValueEntityType.TIME.parse(value);
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

	public ProductIndexEntity getProductIndex() {
		return productIndex;
	}

	public void setProductIndex(ProductIndexEntity productIndex) {
		this.productIndex = productIndex;
	}

	public ProductAttributeValueEntityType getType() {
		return type;
	}

	public void setType(ProductAttributeValueEntityType type) {
		this.type = type;
	}

	public String getProductAttributeCode() {
		return productAttributeCode;
	}

	public void setProductAttributeCode(String productAttributeCode) {
		this.productAttributeCode = productAttributeCode;
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
			return ProductAttributeValueEntityType.TEXT.toValue(this.id.getValue());
		case INTEGER:
			return ProductAttributeValueEntityType.INTEGER.toValue(this.number);
		case DECIMAL:
			return ProductAttributeValueEntityType.DECIMAL.toValue(this.number);
		case DATE:
			return ProductAttributeValueEntityType.DATE.toValue(this.number);
		case DATE_TIME:
			return ProductAttributeValueEntityType.DATE_TIME.toValue(this.number);
		case TIME:
			return ProductAttributeValueEntityType.TIME.toValue(this.number);
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
		
		e.setProductAttributeCode(this.productAttributeCode);
		e.setProductAttributeId(id.getProductMetadataAttributeID());
		e.setProductMetadataId(this.productMetadataID == null? null : this.productMetadataID.intValue());
		
		Object val = null;
		ProductAttributeValueType t = null;
		
		switch (this.type) {
		case TEXT:
			val = ProductAttributeValueEntityType.TEXT.toValue(this.id.getValue());
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

		e.setType(t);
		e.setValue(val);
				
		
		return e;
	}
	
}
