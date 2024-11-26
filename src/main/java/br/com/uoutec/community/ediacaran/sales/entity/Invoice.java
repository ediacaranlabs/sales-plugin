package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

	public String toStringDate(Locale locale) {
		if(date == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
		return date.format(dateTimeFormatter);
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
		return value;
		
	}

	public BigDecimal getTax() {
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: itens) {
			value = value.add(pr.getTax());
		}
		return value;
	}
	
	public BigDecimal getTotal(){
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: itens) {
			value = value.add(pr.getTotal());
		}
		return value;
	}
	
}
