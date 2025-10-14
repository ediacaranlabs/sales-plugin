package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

import br.com.uoutec.community.ediacaran.sales.CurrencyUtil;

public class ProductPrice implements Serializable {

	private static final long serialVersionUID = -236635229202714493L;

	private static NumberFormat nb;
	
	static{ 
		nb = NumberFormat.getNumberInstance();
		nb.setMinimumIntegerDigits(2);
	}

	private String currency;
	
	private String symbol;
	
	private BigDecimal wholeNumber;
	
	private BigDecimal fractionalPart;

	private BigDecimal value;
	
	public ProductPrice(String currency, BigDecimal value) {
		this.currency = currency;
		this.symbol = CurrencyUtil.getSymbol(currency);
		this.wholeNumber = value.setScale(0, BigDecimal.ROUND_UNNECESSARY);
		this.fractionalPart = value.subtract(wholeNumber).multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_UNNECESSARY);
		this.value = value;
	}
	
	public String getCurrency() {
		return currency;
	}

	public BigDecimal getValue() {
		return value;
	}

	public String getSymbol() {
		return symbol;
	}

	public BigDecimal getWholeNumber() {
		return wholeNumber;
	}
	
	public String getWholeNumberString() {
		return wholeNumber.toString();
	}
	
	public BigDecimal getFractionalPart() {
		return fractionalPart;
	}

	public String getFractionalPartString() {
		return nb.format(fractionalPart.doubleValue());
	}


	public String toString() {
		return CurrencyUtil.toString(currency, value);
	}
	
}
