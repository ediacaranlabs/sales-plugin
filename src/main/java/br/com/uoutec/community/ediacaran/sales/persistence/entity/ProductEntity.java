package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.MeasurementUnit;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.ediacaran.core.plugins.PublicType;

@Entity
@Table(name="rw_product")
@EntityListeners(ProductEntityListener.class)
public class ProductEntity implements Serializable,PublicType{

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
	
	@Column(name = "dsc_tags", length = 256)
	private String tags;
	
	@Column(name="cod_product_type")
	private String productType;

	@Enumerated(EnumType.STRING)
	@Column(name="set_measurement_unit", length=32)
	private MeasurementUnit measurementUnit;
	
	@Column(name="vlr_cost", scale=2, precision=12)
	private BigDecimal cost;
	
	@Column(name="dsc_currency", length=3)
	private String currency;

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
		
		if(e.getTags() != null) {
			this.tags = e.getTags().stream()
					.collect(Collectors.joining(";"));
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

	public Product toEntity(){
		
		Product e = new Product();
		
		e.setCost(this.cost);
		e.setCurrency(this.currency);
		e.setProductType(this.productType);
		e.setMeasurementUnit(this.measurementUnit);
		e.setDescription(this.description);
		e.setId(this.id == null? 0 : this.id);
		e.setName(this.name);
		e.setShortDescription(this.shortDescription);
		
		if(this.tags != null) {
			e.setTags(Arrays.stream(this.tags.split("\\;"))
					.collect(Collectors.toSet())
			);
		}
		
		return e;
	}
}
