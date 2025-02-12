package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
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

	@Column(name="cod_product_type")
	private String productType;

	@Column(name="vlr_cost", scale=2, precision=12)
	private BigDecimal cost;
	
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
