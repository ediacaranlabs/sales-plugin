package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductCategory;

@Entity
@Table(name="rw_product_category")
@EntityListeners(ProductCategoryEntityListener.class)
public class ProductCategoryEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_category", length=11)
	private Integer id;
	
	@Column(name="dsc_name", length=128)
	private String name;
	
	@Lob
	@Column(name="dsc_description", length=2048)
	private String description;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cod_parent", referencedColumnName = "cod_category" )
	private ProductCategoryEntity parent;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cod_parent1", referencedColumnName = "cod_category" )
	private ProductCategoryEntity parent1;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cod_parent2", referencedColumnName = "cod_category" )
	private ProductCategoryEntity parent2;
	
	public ProductCategoryEntity(){
	}
	
	public ProductCategoryEntity(ProductCategory e){
		this.description      = e.getDescription();
		this.id               = e.getId() <= 0? null : e.getId();
		this.name             = e.getName();
		
		if(e.getParent() != null) {
			this.parent = new ProductCategoryEntity(e.getParent());
		}
		
		if(e.getParent1() != null) {
			this.parent1 = new ProductCategoryEntity(e.getParent1());
		}
		
		if(e.getParent2() != null) {
			this.parent2 = new ProductCategoryEntity(e.getParent2());
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

	public ProductCategoryEntity getParent() {
		return parent;
	}

	public void setParent(ProductCategoryEntity parent) {
		this.parent = parent;
	}

	public ProductCategoryEntity getParent1() {
		return parent1;
	}

	public void setParent1(ProductCategoryEntity parent1) {
		this.parent1 = parent1;
	}

	public ProductCategoryEntity getParent2() {
		return parent2;
	}

	public void setParent2(ProductCategoryEntity parent2) {
		this.parent2 = parent2;
	}

	public ProductCategory toEntity(){
		return toEntity(null);
	}
	
	public ProductCategory toEntity(ProductCategory e){
		
		if(e == null) {
			e = new ProductCategory();
		}
		
		e.setDescription(this.description);
		e.setId(this.id == null? 0 : this.id);
		e.setName(this.name);
		
		if(this.parent != null) {
			ProductCategory c = new ProductCategory();
			c.setId(this.parent.getId());
			c.setName(name);
			e.setParent(c);
		}

		if(this.parent1 != null) {
			ProductCategory c = new ProductCategory();
			c.setId(this.parent1.getId());
			c.setName(name);
			e.setParent1(c);
		}

		if(this.parent2 != null) {
			ProductCategory c = new ProductCategory();
			c.setId(this.parent2.getId());
			c.setName(name);
			e.setParent2(c);
		}
		
		return e;
	}
	
}
