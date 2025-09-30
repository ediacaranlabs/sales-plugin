package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.MeasurementUnit;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValue;

@Entity
@Table(name="rw_product")
@EntityListeners(ProductEntityListener.class)
public class ProductEntity implements Serializable {

	private static final long serialVersionUID = 7360107228997614767L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_product", length=11)
	private Integer id;
	
	@Column(name="dsc_name", length=128)
	private String name;
	
	@Lob
	@Column(name="dsc_description", length=2048)
	private String description;

	@Column(name="dsc_short_description", length=256)
	private String shortDescription;

	@ManyToOne
	@JoinColumn(name = "cod_category", referencedColumnName = "cod_category")
	private ProductCategoryEntity category;

	@Column(name = "dsc_tags", length = 256)
	private String tags;
	
	@Column(name="cod_product_type")
	private String productType;

	@Column(name="cod_product_metadata", length=11)
	private Integer metadata;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_measurement_unit", length=10)
	private MeasurementUnit measurementUnit;

	@Column(name="bit_display", length=1)
	private Boolean display;
	
	@Column(name="vlr_cost", scale=2, precision=12)
	private BigDecimal cost;
	
	@Column(name="dat_offer")
	protected LocalDate offerDate;
	
	@Column(name="vlr_offer", scale=2, precision=12)
	protected BigDecimal offerDiscount;
	
	@Column(name="dsc_currency", length=3)
	private String currency;

    @OneToMany(mappedBy = "product")
	private List<ProductAttributeValueEntity> attributes;
	
	public ProductEntity(){
	}
	
	public ProductEntity(Product e){
		this.cost             = e.getCost();
		this.currency         = e.getCurrency();
		this.productType      = e.getProductType();
		this.description      = e.getDescription();
		this.measurementUnit  = e.getMeasurementUnit();
		this.id               = e.getId() <= 0? null : e.getId();
		this.name             = e.getName();
		this.shortDescription = e.getShortDescription();
		this.display          = e.getDisplay();
		this.metadata         = e.getMetadata() <= 0? null : e.getMetadata();
		this.offerDate        = e.getOfferDate();
		this.offerDiscount    = e.getOfferDiscount();
		
		if(e.getCategory() != null) {
			this.category = new ProductCategoryEntity(e.getCategory());
		}
		
		if(e.getTags() != null) {
			this.tags = e.getTags().stream()
					.collect(Collectors.joining(";"));
		}
		
		if(e.getAttributes() != null) {
			
			this.attributes = new ArrayList<>();
			for(Entry<String, ProductAttributeValue> x: e.getAttributes().entrySet()) {
				for(Object value: x.getValue().getValues()) {
					this.attributes.add(new ProductAttributeValueEntity(value, x.getValue(), e));
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Integer getMetadata() {
		return metadata;
	}

	public void setMetadata(Integer metadata) {
		this.metadata = metadata;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public List<ProductAttributeValueEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ProductAttributeValueEntity> attributes) {
		this.attributes = attributes;
	}

	public Product toEntity(){
		return toEntity(null);
	}
	
	public Product toEntity(Product e){
		
		if(e == null) {
			e = new Product();
		}
		
		e.setCost(this.cost);
		e.setCurrency(this.currency);
		e.setProductType(this.productType);
		e.setMeasurementUnit(this.measurementUnit);
		e.setDescription(this.description);
		e.setId(this.id == null? 0 : this.id);
		e.setName(this.name);
		e.setShortDescription(this.shortDescription);
		e.setDisplay(this.display);
		e.setMetadata(this.metadata == null? 0 : this.metadata);
		e.setOfferDate(this.offerDate);
		e.setOfferDiscount(this.offerDiscount);
		
		if(this.category != null) {
			e.setCategory(this.category.toEntity());
		}
		
		if(this.tags != null) {
			e.setTags(Arrays.stream(this.tags.split("\\;"))
					.collect(Collectors.toSet())
			);
		}
		
		if(attributes != null) {
			Map<String, ProductAttributeValue> attrs = new HashMap<>();
			
			for(ProductAttributeValueEntity x: this.attributes) {
				
				ProductAttributeValue att = attrs.get(x.getProductAttributeCode());
				
				if(att == null) {
					att = x.toEntity();
					attrs.put(x.getProductAttributeCode(), att);
				}
				else {
					att.addValue(x.parseValue());
				}
				
			}
			
			e.setAttributes(attrs);
		}
		return e;
	}
}
