package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Product implements Serializable{

	private static final long serialVersionUID = 2357774653199543812L;

	private int id;
	
	private String name;
	
	private String description;
	
	private ProductType productType;
	
	private AttractivenessType attractiveness;
	
	private PeriodType periodType;
	
	private BigDecimal additionalCost;
	
	private BigDecimal cost;
	
	private String currency;

	/* transaient properties */
	
	private List<Discount> discounts;

	private BigDecimal discount;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttractivenessType getAttractiveness() {
		return attractiveness;
	}

	public void setAttractiveness(AttractivenessType attractiveness) {
		this.attractiveness = attractiveness;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public boolean isFree(){
		return this.cost == null || this.cost.equals(BigDecimal.ZERO);
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

	public List<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}
	
	public BigDecimal getSubtotal(boolean additional){
		return additional? this.additionalCost : this.cost;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTotal(boolean additional){
		BigDecimal value = this.getSubtotal(additional);
		
		if(this.discount == null && 
			this.discount.compareTo(BigDecimal.ZERO) > 0 && 
			this.discount.compareTo(value) <= 0){
			return value.subtract(this.discount);
		}
		
		return value;
	}	
}
