package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValue;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

@Entity
@Table(name="rw_product_index")
@EntityListeners(ProductIndexEntityListener.class)
public class ProductIndexEntity implements Serializable {

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cod_category1", referencedColumnName = "cod_category")
	private ProductCategoryEntity category1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cod_category2", referencedColumnName = "cod_category")
	private ProductCategoryEntity category2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cod_category3", referencedColumnName = "cod_category")
	private ProductCategoryEntity category3;
	
	@Column(name="bit_display", length=1)
	private Boolean display;
	
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
		this.display        = e.isDisplay();

		if(e.getCategory() != null) {
			if(e.getCategory().getParent2() != null) {
				this.category1 = new ProductCategoryEntity(e.getCategory().getParent1());
				this.category2 = new ProductCategoryEntity(e.getCategory().getParent2());
				this.category3 = new ProductCategoryEntity(e.getCategory());
			}
			else
			if(e.getCategory().getParent1() != null) {
				this.category1 = new ProductCategoryEntity(e.getCategory().getParent1());
				this.category2 = new ProductCategoryEntity(e.getCategory());
			}
			else {
				this.category1 = new ProductCategoryEntity(e.getCategory());
			}
		}
		
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
		
		if(e.getAttributes() != null) {
			this.attributes = new ArrayList<>();
			for(ProductAttributeValue x: e.getAttributes().values()) {
				
				Set<Object> values = x.getSetValues();
				
				if(values != null) {
					for(Object v: values) {
						ProductAttributeValueIndexEntity k = new ProductAttributeValueIndexEntity(v, x, e);
						this.attributes.add(k);
					}
				}
				
			}
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

	public ProductCategoryEntity getCategory1() {
		return category1;
	}

	public void setCategory1(ProductCategoryEntity category1) {
		this.category1 = category1;
	}

	public ProductCategoryEntity getCategory2() {
		return category2;
	}

	public void setCategory2(ProductCategoryEntity category2) {
		this.category2 = category2;
	}

	public ProductCategoryEntity getCategory3() {
		return category3;
	}

	public void setCategory3(ProductCategoryEntity category3) {
		this.category3 = category3;
	}

	public List<ProductAttributeValueIndexEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ProductAttributeValueIndexEntity> attributes) {
		this.attributes = attributes;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public Product toEntity(){
		return toEntity(null);
	}
	
	public Product toEntity(Product e){
		
		if(e == null) {
			e = new Product();
		}
		
		e.setId(this.id == null? 0 : this.id);
		
		if(attributes != null) {
			Map<String, ProductAttributeValue> attrs = new HashMap<>();
			
			for(ProductAttributeValueIndexEntity x: this.attributes) {
				
				ProductAttributeValue att = attrs.get(x.getAttributeID());
				
				if(att == null) {
					att = x.toEntity();
					attrs.put(x.getAttributeID(), att);
				}
				else {
					att.addValue(x.parseValue());
				}
				
			}
			
			e.setAttributes(attrs);
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
		ProductIndexEntity other = (ProductIndexEntity) obj;
		return Objects.equals(id, other.id);
	}
	
}
