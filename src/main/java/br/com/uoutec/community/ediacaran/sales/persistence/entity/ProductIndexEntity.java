package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.ediacaran.core.plugins.PublicType;

@Entity
@Table(name="rw_product_index")
@EntityListeners(ProductEntityListener.class)
public class ProductIndexEntity implements Serializable,PublicType{

	private static final long serialVersionUID = 7360107228997614767L;

	@Id
	@Column(name="cod_product", length=11)
	private Integer id;
	
	@Column(name="dsc_name", length=128)
	private String name;
	
	@Column(name="dsc_description", length=255)
	private String description;

	@Column(name = "dsc_search_tags", length = 255)
	private String tags;
	
	@Column(name="cod_product_type")
	private String productType;

	@Column(name="vlr_cost", scale=2, precision=12)
	private BigDecimal cost;
	
    @OneToMany(mappedBy = "productIndex")
	private List<ProductAttributeValueIndexEntity> attributes;
	
	public ProductIndexEntity(){
	}
	
	public ProductIndexEntity(Product e){
		this.cost           = e.getCost();
		this.productType    = e.getProductType();
		this.description    = e.getDescription();
		this.id             = e.getId() <= 0? null : e.getId();
		this.name           = e.getName();
		
		if(e.getDescription() != null) {
			this.description = StringUtil.toSearch(e.getDescription());
			this.description = this.description.length() > 255? this.description.substring(0, 253) : this.description;
		}
		
		if(e.getName() != null) {
			this.name = StringUtil.toSearch(e.getName());
		}

		if(e.getTags() != null) {
			this.tags = e.getTags().stream().collect(Collectors.joining(" "));
			this.tags = StringUtil.toSearch(e.getDescription());
			this.tags = this.tags.length() > 255? this.tags.substring(0, 253) : this.tags;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public List<ProductAttributeValueIndexEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ProductAttributeValueIndexEntity> attributes) {
		this.attributes = attributes;
	}

	public Product toEntity(){
		return toEntity(null);
	}
	
	public Product toEntity(Product e){
		
		if(e == null) {
			e = new Product();
		}
		
		e.setId(this.id == null? 0 : this.id);
		
		return e;
	}
}
