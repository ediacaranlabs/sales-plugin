package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Payment implements Serializable{

	private static final long serialVersionUID = 6482522225801595660L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	private String paymentType;
	
	@NotNull(groups = DataValidation.class)
	@Min(value = 0, groups = DataValidation.class)
	private BigDecimal value;
	
	@NotNull(groups = DataValidation.class)
	private PaymentStatus status;
	
	@NotNull(groups = DataValidation.class)
	@Min(value = 0, groups = DataValidation.class)
	private BigDecimal tax;

	@NotNull(groups = DataValidation.class)
	@Min(value = 0, groups = DataValidation.class)
	private BigDecimal discount;

	@NotNull(groups = DataValidation.class)
	@Min(value = 0, groups = DataValidation.class)
	private BigDecimal total;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.CURRENCY)
	private String currency;
	
	private LocalDateTime receivedFrom;
	
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

	public LocalDateTime getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(LocalDateTime receivedFrom) {
		this.receivedFrom = receivedFrom;
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

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/*
	public BigDecimal getTotal(){
		return this.value.subtract(this.discount).add(this.tax);
	}
	*/
}
