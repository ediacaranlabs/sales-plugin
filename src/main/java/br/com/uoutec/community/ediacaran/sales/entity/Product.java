package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Product implements Serializable{

	private static final long serialVersionUID = 2357774653199543812L;

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	protected int id;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128, groups = DataValidation.class)
	protected String name;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Size(max=2048)
	protected String description;
	
	@NotNull(groups = DataValidation.class)
	protected PeriodType periodType;
	
	@NotNull(groups = DataValidation.class)
	protected ProductType productType;
	
	@NotNull(groups = DataValidation.class)
	protected BigDecimal additionalCost;
	
	@NotNull(groups = DataValidation.class)
	protected BigDecimal cost;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.CURRENCY, groups = DataValidation.class)
	protected String currency;

	/* transaient properties */
	
	protected List<Discount> discounts;

	protected BigDecimal discount;
	
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

	public PeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
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
