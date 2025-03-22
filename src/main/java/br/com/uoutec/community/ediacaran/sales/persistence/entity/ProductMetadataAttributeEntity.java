package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;

@Entity
@Table(
		name="rw_product_metadata_attr",
		indexes = {
				@Index(columnList = "cod_product_metadata, cod_product_attr", unique = true),
				@Index(columnList = "cod_product_metadata")
		}
)
@EntityListeners(ProductMetadataAttributeEntityListener.class)
public class ProductMetadataAttributeEntity implements Serializable{

	private static final long serialVersionUID = 3514513692287877849L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_prod_mtda_attr", length=11)
	private Integer id;
	
	@Column(name="cod_product_attr", length=32)
	private String code;
	
	@Column(name = "cod_product_metadata")
	private Integer productMetadataID;

	@ManyToOne
	@JoinColumn(name = "cod_product_metadata", insertable = false, updatable = false)
	private ProductMetadataEntity productMetadata;
	
	@Column(name="dsc_name", length=128)
	private String name;

	@Column(name="set_type", length=32)
	private ProductAttributeType type;
	
	@Column(name="set_value_type", length=32)
	private ProductAttributeValueType valueType;
	
	@Column(name="dsc_description", length=128)
	private String description;

	@Column(name="bit_allow_empty", length = 1)
	private Boolean allowEmpty;
	
	@Column(name="vlr_rows", length = 2)
	private Short rows;
	
	@Column(name="vlr_min_len", length = 3)
	private Short minLength;
	
	@Column(name="vlr_max_len", length = 3)
	private Short maxLength;

	@Column(name="vlr_min", length = 3)
	private Double min;
	
	@Column(name="vlr_max", length = 3)
	private Double max;
	
	@Column(name="dsc_regex", length = 128)
	private String regex;

	@Column(name="dsc_prefix", length=32)
	private String prefix;

	@Column(name="dsc_suffix", length=32)
	private String suffix;
	
	@Column(name="vlr_order", length = 3)
	private Short order;

	//@OneToMany(mappedBy = "productAttribute", cascade = CascadeType.REFRESH)
	@OneToMany(mappedBy = "productAttribute")
	private List<ProductMetadataAttributeOptionEntity> options;
	
	public ProductMetadataAttributeEntity() {
	}
	
	public ProductMetadataAttributeEntity(ProductMetadataAttribute e) {
		this.allowEmpty = e.isAllowEmpty();
		this.description = e.getDescription();
		this.id = e.getId() == 0? null : e.getId();
		this.code = e.getCode();
		this.max = e.getMax();
		this.maxLength = e.getMaxLength();
		this.min = e.getMin();
		this.minLength = e.getMinLength();
		this.name = e.getName();
		this.order = e.getOrder();
		this.prefix = e.getPrefix();
		this.suffix = e.getSuffix();
		this.productMetadataID = e.getProductMetadata();
		/*
		if(e.getProductMetadata() > 0) {
			this.productMetadata = new ProductMetadataEntity();
			this.productMetadata.setId(e.getProductMetadata());
		}
		*/
		
		this.regex = e.getRegex();
		this.rows = e.getRows();
		this.type = e.getType();
		this.valueType = e.getValueType();
		
		if(e.getOptions() != null) {
			this.options = new ArrayList<>();
			for(ProductMetadataAttributeOption x: e.getOptions()) {
				this.options.add(new ProductMetadataAttributeOptionEntity(x));
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setOrder(Short order) {
		this.order = order;
	}

	public ProductMetadataEntity getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(ProductMetadataEntity productMetadata) {
		this.productMetadata = productMetadata;
		this.productMetadataID = productMetadata.getId();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProductMetadataID() {
		return productMetadataID;
	}

	public void setProductMetadataID(Integer productMetadataID) {
		this.productMetadataID = productMetadataID;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public ProductAttributeType getType() {
		return type;
	}

	public void setType(ProductAttributeType type) {
		this.type = type;
	}

	public ProductAttributeValueType getValueType() {
		return valueType;
	}

	public void setValueType(ProductAttributeValueType valueType) {
		this.valueType = valueType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(Boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public Short getRows() {
		return rows;
	}

	public void setRows(Short rows) {
		this.rows = rows;
	}

	public Short getMinLength() {
		return minLength;
	}

	public void setMinLength(Short minLength) {
		this.minLength = minLength;
	}

	public Short getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Short maxLength) {
		this.maxLength = maxLength;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public short getOrder() {
		return order;
	}

	public void setOrder(short order) {
		this.order = order;
	}

	public List<ProductMetadataAttributeOptionEntity> getOptions() {
		return options;
	}

	public void setOptions(List<ProductMetadataAttributeOptionEntity> options) {
		this.options = options;
	}
	
	public ProductMetadataAttribute toEntity() {
		return toEntity(null);
	}
	
	public ProductMetadataAttribute toEntity(ProductMetadataAttribute e) {
	
		if(e == null) {
			e = new ProductMetadataAttribute();
		}
		
		e.setAllowEmpty(this.allowEmpty == null? false : this.allowEmpty.booleanValue());
		e.setDescription(this.description);
		e.setId(this.id == null? 0 : this.id.intValue());
		e.setCode(this.code);
		e.setMax(this.max);
		e.setMaxLength(this.maxLength == null? 0 : this.maxLength.shortValue());
		e.setMin(this.min);
		e.setName(this.name);
		e.setOrder(this.order == null? 0 : this.order.shortValue());
		e.setRegex(this.regex);
		e.setRows(this.rows == null? 0 : this.rows.shortValue());
		e.setType(this.type);
		e.setValueType(this.valueType);
		e.setPrefix(this.prefix);
		e.setSuffix(this.suffix);
		e.setProductMetadata(this.productMetadataID == null? 0 : this.productMetadataID.intValue());
		
		if(this.productMetadata != null){
			e.setProductMetadata(this.productMetadata.getId());
		}
		
		if(this.options != null) {
			List<ProductMetadataAttributeOption> list = new ArrayList<>();
			for(ProductMetadataAttributeOptionEntity x: this.options) {
				list.add(x.toEntity());
			}
			e.setOptions(list);
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
		ProductMetadataAttributeEntity other = (ProductMetadataAttributeEntity) obj;
		return Objects.equals(id, other.id);
	}
	
}
