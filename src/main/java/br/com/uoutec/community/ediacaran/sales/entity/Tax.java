package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.CurrencyUtil;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Tax implements Serializable {

	private static final long serialVersionUID = 7140999176118548327L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	@Size(min=1, max=32, groups = DataValidation.class)
	private String name;
	
	@NotNull(groups = DataValidation.class)
	@Size(min=1, max=128, groups = DataValidation.class)
	private String description;
	
	@NotNull(groups = DataValidation.class)
	private BigDecimal value;
	
	protected BigDecimal exchangeRate;
	
	protected String exchangeCurrency;
	
	@NotNull
	protected String currency;
	
	@NotNull(groups = DataValidation.class)
	private TaxType type;
	
	private boolean discount;
	
	private byte order;

	public Tax() {
	}
	
	public Tax(Tax tax) {
		this.description = tax.getDescription();
		this.discount    = tax.isDiscount();
		this.id          = tax.getId();
		this.name        = tax.getName();
		this.order       = tax.getOrder();
		this.type        = tax.getType();
		this.value       = tax.getValue();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public BigDecimal getOrigialValue() {
		return value;
	}
	
	public BigDecimal getValue() {
		return exchangeRate == null? value : value.multiply(exchangeRate);
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getSymbol() {
		return CurrencyUtil.getSymbol(currency);
	}
	
	public TaxType getType() {
		return type;
	}

	public void setType(TaxType type) {
		this.type = type;
	}

	public boolean isDiscount() {
		return discount;
	}

	public void setDiscount(boolean discount) {
		this.discount = discount;
	}

	public byte getOrder() {
		return order;
	}

	public void setOrder(byte order) {
		this.order = order;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getDisplayValue() {
		return CurrencyUtil.toString(currency, getValue());
	}
	
}
