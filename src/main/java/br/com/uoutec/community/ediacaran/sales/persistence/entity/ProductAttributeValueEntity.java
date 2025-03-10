package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.Locale;

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

	@ManyToOne
	@JoinColumn(name = "cod_product", insertable = false, updatable = false)
	private ProductIndexEntity productIndex;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_type", length=32)
	private ProductAttributeValueType type;
	
	@Column(name="cod_attribute", length=32)
	private String attributeID;
	
	@Column(name="dsc_value", length=128)
	private String value;
	
	public ProductAttributeValueEntity(){
	}
	
	public ProductAttributeValueEntity(ProductAttributeValue e, Product product){
		this.id = new ProductAttributeValueEntityID(product.getId(), e.getProductAttributeId());
		this.attributeID = e.getProductAttributeCode();
		this.type = e.getType();
		this.product = new ProductEntity();
		this.product.setId(product.getId());
		this.value = e.getValue() == null? null : e.getType().toString(e.getValue(), Locale.US);
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

	public ProductIndexEntity getProductIndex() {
		return productIndex;
	}

	public void setProductIndex(ProductIndexEntity productIndex) {
		this.productIndex = productIndex;
	}

	public ProductAttributeValueType getType() {
		return type;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
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

		e.setType(this.type);
		e.setValue(this.value == null? null : this.type.parse(value, Locale.US));
		
		return e;
	}
	
}
