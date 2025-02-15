package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;

@Entity
@Table(
		name="rw_product_image",
		indexes = {
				@Index(name="product_image_unique", columnList = "id, product", unique = true)
		}
)
@EntityListeners(ProductImageEntityListener.class)
public class ProductImageEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@Id
	@Column(name="cod_image", length=32)
	private String id;
	
	@Column(name="cod_product", length=11)
	private Integer product;
	
	@Column(name="dsc_description", length = 256)
	private String description;

	public ProductImageEntity(){
	}
	
	public ProductImageEntity(ProductImage e){
		this.id          = e.getId();
		this.description = e.getDescription();
		this.product     = e.getProduct();
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductImage toEntity(){
		return toEntity(null);
	}
	
	public ProductImage toEntity(ProductImage e){
		
		if(e == null) {
			e = new ProductImage();
		}
		
		e.setDescription(this.description);
		e.setId(this.id);
		e.setProduct(this.product == null? 0 : this.product.intValue());
		return e;
	}
	
}
