package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class ProductRequest implements Serializable {

	private static final long serialVersionUID = -4588518156028055308L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+")
	@Length(min = 10, max = 38)
	protected String id;

	@NotNull
	protected String serial;
	
	@NotNull
	@Valid
	protected Product product;
	
	@Length(max = 30)
	protected String productID;
	
	@Min(1)
	protected int units;

	@Min(0)
	private int maxExtra;
	
	@NotNull
	protected MeasurementUnit measurementUnit;
	
	protected BigDecimal exchangeRate;
	
	protected String exchangeCurrency;
	
	@NotNull
	protected String currency;
	
	@NotNull
	protected BigDecimal cost;

	protected List<Tax> taxes;
	
	protected Map<String, String> addData;

	@NotNull
	@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128)
	protected String name;
	
	@NotNull
	@Length(min = 5, max = 128)
	protected String shortDescription;
	
	@NotNull
	@Length(min = 5, max = 128)
	protected String description;
	
	/* Campos transientes */
	
	protected boolean availability;
	
	public ProductRequest() {
	}
	
	public ProductRequest(ProductRequest value) {
		setAddData(value.getAddData());
		setAvailability(value.isAvailability());
		setCost(value.getCost());
		setCurrency(value.getCurrency());
		setDescription(value.getDescription());
		setShortDescription(value.getShortDescription());
		setMaxExtra(value.getMaxExtra());
		setName(value.getName());
		setMeasurementUnit(value.getMeasurementUnit());
		setProduct(value.getProduct());
		setProductID(value.getProductID());
		setSerial(value.getSerial());
		setShortDescription(value.getShortDescription());
		setTaxes(value.getTaxes());
		setUnits(value.getUnits());
		setExchangeCurrency(value.getExchangeCurrency());
		setExchangeRate(value.getExchangeRate());
	}
	
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

	public int getMaxExtra() {
		return maxExtra;
	}

	public void setMaxExtra(int maxExtra) {
		this.maxExtra = maxExtra;
	}

	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getExchangeCurrency() {
		return exchangeCurrency;
	}

	public void setExchangeCurrency(String exchangeCurrency) {
		this.exchangeCurrency = exchangeCurrency;
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

	public String getSymbol() {
		return Currency.getInstance(exchangeCurrency == null? currency : exchangeCurrency).getSymbol();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getOrigialValue() {
		return cost;
	}
	
	public BigDecimal getValue() {
		return exchangeRate == null? cost : cost.multiply(exchangeRate);
	}
	
	public BigDecimal getSubtotal(){
		BigDecimal value = getValue();
		return value.multiply(new BigDecimal(this.units));
	}

	public BigDecimal getDiscount() {
		
		BigDecimal value = getValue();
		BigDecimal discount = BigDecimal.ZERO;
		
		if(taxes != null) {
			
			List<Tax> tx = taxes;
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
				if(t.isDiscount()) {
					discount = discount.add(taxUnit);
				}
			}
			
		}
		
		return discount.multiply(new BigDecimal(this.units));
	}

	public BigDecimal getTax() {
		
		BigDecimal value = getValue();
		BigDecimal tax = BigDecimal.ZERO;
		
		if(taxes != null) {
			
			List<Tax> tx = taxes;
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
				if(!t.isDiscount()) {
					tax = tax.add(taxUnit);
				}
			}
			
		}
		
		return tax.multiply(new BigDecimal(this.units));
	}
	
	public BigDecimal getTotal(){
		
		BigDecimal value = getValue();
		
		if(taxes != null) {
			
			List<Tax> tx = taxes;
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
			}
			
		}
		
		return value.multiply(new BigDecimal(this.units));
	}
	
	public BigDecimal getRemainingValue(LocalDate validate){
		BigDecimal subTotal = this.getSubtotal();
		LocalDate now       = LocalDate.now();

		if(subTotal.compareTo(BigDecimal.ZERO)  <= 0 || this.measurementUnit == null || now.isAfter(validate)){
			return BigDecimal.ZERO;
		}
		
		BigDecimal hours    = new BigDecimal(this.measurementUnit.toHours());
		BigDecimal byHour   = subTotal.divide(hours, 24, RoundingMode.HALF_UP);
		Duration duration   = Duration.between(now, validate);//DateUtil.getHours(now, validate);
		long hoursRemaining = duration.get(ChronoUnit.HOURS);
		return byHour.multiply(new BigDecimal(hoursRemaining));
	}

	public BigDecimal getProporcionalTime(LocalDate validate, Product product){
		
		BigDecimal value   = this.getRemainingValue(validate);
		BigDecimal hours   = new BigDecimal(this.measurementUnit.toHours());
		BigDecimal byHour  = this.cost.divide(hours);
		int hoursRemaining = value.divide(byHour, 24, RoundingMode.HALF_UP).intValue();
		
		return byHour.multiply(new BigDecimal(hoursRemaining));
		
	}
	
}
