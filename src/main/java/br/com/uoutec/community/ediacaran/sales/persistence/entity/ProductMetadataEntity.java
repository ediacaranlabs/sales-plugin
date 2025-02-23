package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;

@Entity
@Table(name="rw_product_metadata")
@EntityListeners(ProductMetadataEntityListener.class)
public class ProductMetadataEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_product_metadata", length=11)
	private Integer id;

	@Column(name="dsc_name", length=128)
	private String name;
	
	@Column(name="dsc_description", length=256)
	private String description;
	
    @OneToMany(mappedBy = "productMetadata")
	private List<ProductAttributeEntity> attributes;
	
	public ProductMetadataEntity(){
	}
	
	public ProductMetadataEntity(ProductMetadata e){
		this.description = e.getDescription();
		this.id = e.getId() == 0? null : e.getId();
		this.name = e.getName();
		
		if(e.getAttributes() != null) {
			this.attributes = new ArrayList<>();
			for(Entry<String, ProductAttribute> x : e.getAttributes().entrySet()) {
				this.attributes.add(new ProductAttributeEntity(x.getValue(), this));
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

	public ProductMetadata toEntity(){
		return toEntity(null);
	}
	
	public ProductMetadata toEntity(ProductMetadata e){
		
		if(e == null) {
			e = new ProductMetadata();
		}
		
		e.setDescription(this.description);
		e.setId(this.id == null? 0 : this.id.intValue());
		e.setName(this.name);

		if(this.attributes != null) {
			Map<String, ProductAttribute> map = new HashMap<>();
			for(ProductAttributeEntity x: this.attributes) {
				map.put(x.getCode(), x.toEntity());
			}
			e.setAttributes(map);
		}
		
		return e;
	}
}
