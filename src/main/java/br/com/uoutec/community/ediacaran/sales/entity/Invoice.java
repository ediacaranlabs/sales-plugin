package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Invoice implements Serializable{

	private static final long serialVersionUID = -58736758314320975L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Length(max = 38, min = 10, groups = DataValidation.class)
	private String order;
	
	@NotNull(groups = DataValidation.class)
	@Min(value = 1, groups = DataValidation.class)
	private int owner;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	private LocalDateTime cancelDate;
	
	private String cancelJustification;
	
	@Valid
	private List<ProductRequest> itens;
	
	private List<Tax> taxes;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.CURRENCY)
	private String currency;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelJustification() {
		return cancelJustification;
	}

	public void setCancelJustification(String cancelJustification) {
		this.cancelJustification = cancelJustification;
	}

	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public String toStringDate(Locale locale) {
		if(date == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return date.format(dateTimeFormatter);
	}
	
	public String toStringCancelDate(Locale locale) {
		if(cancelDate == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return cancelDate.format(dateTimeFormatter);
	}
	
	public List<ProductRequest> getItens() {
		return itens;
	}

	public void setItens(List<ProductRequest> itens) {
		this.itens = itens;
	}
	
	public BigDecimal getSubtotal(){
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: itens) {
			value = value.add(pr.getTotal());
		}
		return value;
	}

	public BigDecimal getDiscount() {

		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: itens) {
			value = value.add(pr.getDiscount());
		}
		
		return value.add(getDiscountBySubtotal());
	}

	private BigDecimal getDiscountBySubtotal() {
		
		BigDecimal value = getSubtotal();
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
		
		return discount;
	}
	
	public BigDecimal getTax() {
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: itens) {
			value = value.add(pr.getTax());
		}
		return value.add(getTaxBySubtotal());
	}

	private BigDecimal getTaxBySubtotal() {
		
		BigDecimal value = getSubtotal();
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
		
		return tax;
	}
	
	public BigDecimal getTotal(){
		
		BigDecimal value = getSubtotal();
		
		if(taxes != null) {
			
			List<Tax> tx = taxes;
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
			}
			
		}
		
		return value;
	}
	
}
