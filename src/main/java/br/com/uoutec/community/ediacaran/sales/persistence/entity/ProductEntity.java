package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.com.uoutec.community.ediacaran.sales.entity.PeriodType;
import br.com.uoutec.community.ediacaran.sales.entity.Product;

@Entity
@Table(name="rw_product")
@EntityListeners(ProductEntityListener.class)
public class ProductEntity implements Serializable{

	private static final long serialVersionUID = 7360107228997614767L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_product", length=11)
	private Integer id;
	
	@Column(name="dsc_name", length=128)
	private String name;
	
	@Column(name="dsc_description")
	@Lob
	private String description;
	
	@Column(name="cod_product_type")
	private String productType;

	@Enumerated(EnumType.STRING)
	@Column(name="set_period_type", length=32)
	private PeriodType periodType;
	
	@Column(name="vlr_add_cost", scale=2, precision=12)
	private BigDecimal additionalCost;

	@Column(name="vlr_cost", scale=2, precision=12)
	private BigDecimal cost;
	
	@Column(name="dsc_currency", length=3)
	private String currency;

	public ProductEntity(){
	}
	
	public ProductEntity(Product e){
		this.cost           = e.getCost();
		this.currency       = e.getCurrency();
		this.productType    = e.getProductType();
		this.description    = e.getDescription();
		this.additionalCost = e.getAdditionalCost();
		this.periodType     = e.getPeriodType();
		this.id             = e.getId();
		this.name           = e.getName();
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

	public PeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}

	public BigDecimal getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(BigDecimal additionalCost) {
		this.additionalCost = additionalCost;
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
		e.setAdditionalCost(this.additionalCost);
		e.setPeriodType(this.periodType);
		e.setDescription(this.description);
		e.setId(this.id == null? 0 : this.id);
		e.setName(this.name);
		
		return e;
	}
}