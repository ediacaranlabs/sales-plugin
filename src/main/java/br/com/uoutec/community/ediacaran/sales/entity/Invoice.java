package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.entity.registry.IdValidation;

public class Invoice implements Serializable{

	private static final long serialVersionUID = -58736758314320975L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+")
	@Length(max = 38, min = 10)
	private String id;
	
	@NotNull
	private LocalDate date;

	@NotNull
	@Min(0)
	private BigDecimal value;
	
	@NotNull
	@Min(0)
	private BigDecimal discount;
	
	@NotNull
	private DiscountType discountType;
	
	@NotNull
	@Min(0)
	private BigDecimal total;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
