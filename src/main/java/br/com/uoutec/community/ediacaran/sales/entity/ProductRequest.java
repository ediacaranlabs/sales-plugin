package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.entity.registry.IdValidation;

public class ProductRequest implements Serializable{

	private static final long serialVersionUID = -4588518156028055308L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+")
	@Length(min = 10, max = 38)
	private String id;

	@NotNull
	private String serial;
	
	@NotNull
	@Valid
	private Product product;
	
	@Length(max = 30)
	private String productID;
	
	@Min(1)
	private int units;
	
	@NotNull
	private PeriodType periodType;
	
	@NotNull
	private BigDecimal cost;

	@NotNull
	private BigDecimal additionalCost;
	
	@NotNull
	private BigDecimal discount;

	@NotNull
	private BigDecimal tax;
	
	@NotNull
	private String currency;
	
	private Map<String, String> addData;

	@NotNull
	@Length(min = 5, max = 128)
	private String shortDescription;
	
	@NotNull
	@Length(min = 5, max = 128)
	private String description;
	
	/* Campos transientes */
	
	private boolean availability;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
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

	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(BigDecimal additionalCost) {
		this.additionalCost = additionalCost;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getSubtotal(){
		BigDecimal value = this.cost;
		
		if(this.units > 1){
			BigDecimal ex = 
					this.additionalCost
					.multiply(new BigDecimal(this.units - 1));
			value = value.add(ex);
		}
		
		return value;
	}

	public BigDecimal getTotal(){
		BigDecimal value = this.getSubtotal();
		
		if(this.discount != null && this.discount.compareTo(BigDecimal.ZERO) > 0){ 
			if(this.discount.compareTo(value) <= 0){
				value = value.subtract(this.discount);
			}
			else{
				value = BigDecimal.ZERO;
			}
		}

		if(this.tax != null && this.tax.compareTo(BigDecimal.ZERO) > 0){
			value = value.add(this.tax);
		}
		
		return value;
	}
	
	public BigDecimal getRemainingValue(LocalDate validate){
		BigDecimal subTotal = this.getSubtotal();
		LocalDate now       = LocalDate.now();

		if(subTotal.compareTo(BigDecimal.ZERO)  <= 0 || this.periodType == null || now.isAfter(validate)){
			return BigDecimal.ZERO;
		}
		
		BigDecimal hours    = new BigDecimal(this.periodType.toHours());
		BigDecimal byHour   = subTotal.divide(hours, 24, RoundingMode.HALF_UP);
		Duration duration   = Duration.between(now, validate);//DateUtil.getHours(now, validate);
		long hoursRemaining = duration.get(ChronoUnit.HOURS);
		return byHour.multiply(new BigDecimal(hoursRemaining));
	}

	public BigDecimal getProporcionalTime(LocalDate validate, Product product){
		
		BigDecimal value   = this.getRemainingValue(validate);
		BigDecimal hours   = new BigDecimal(this.periodType.toHours());
		BigDecimal byHour  = this.cost.divide(hours);
		int hoursRemaining = value.divide(byHour, 24, RoundingMode.HALF_UP).intValue();
		
		return byHour.multiply(new BigDecimal(hoursRemaining));
		
	}
	
}
