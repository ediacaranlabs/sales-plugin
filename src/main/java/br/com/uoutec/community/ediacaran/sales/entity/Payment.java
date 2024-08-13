package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.entity.registry.IdValidation;

public class Payment implements Serializable{

	private static final long serialVersionUID = 6482522225801595660L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+")
	@Length(max = 38, min = 10)
	private String id;
	
	@NotNull
	private String paymentType;
	
	@NotNull
	private BigDecimal value;
	
	@NotNull
	private BigDecimal tax;

	@NotNull
	private BigDecimal discount;
	
	@NotNull
	private String currency;
	
	private Map<String,String> addData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
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
	
	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTotal(){
		return this.value.subtract(this.discount).add(this.tax);
	}
	
}
