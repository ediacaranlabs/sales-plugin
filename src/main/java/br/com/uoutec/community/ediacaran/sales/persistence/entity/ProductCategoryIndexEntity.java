package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

@Entity
@Table(name="rw_product_category")
@EntityListeners(ProductCategoryEntityListener.class)
public class ProductCategoryIndexEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@Id
	@Column(name="cod_category", length=11)
	private Integer id;
	
	@Column(name="dsc_name", length=128)
	private String name;
	
	@Column(name="dsc_description", length=255)
	private String description;

	public ProductCategoryIndexEntity(){
	}
	
	public ProductCategoryIndexEntity(ProductCategory e){
		this.id = e.getId();
		
		if(e.getName() != null) {
			this.name = StringUtil.toSearch(e.getName());
		}
		
		if(e.getDescription() != null) {
			this.description = StringUtil.toSearch(e.getDescription());
			this.description = this.description.length() > 255? this.description.substring(0, 253) : this.description;
		}
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductCategory toEntity(){
		return toEntity(null);
	}
	
	public ProductCategory toEntity(ProductCategory e){
		
		if(e == null) {
			e = new ProductCategory();
		}
		
		e.setId(this.id);
		
		return e;
	}
	
}
