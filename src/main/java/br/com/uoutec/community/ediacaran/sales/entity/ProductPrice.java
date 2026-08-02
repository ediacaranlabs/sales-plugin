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
		
		String[] valurSTR = value.toPlainString().split("\\.");		
		this.wholeNumber =  new BigDecimal(valurSTR[0]);
		this.fractionalPart = valurSTR.length > 1? new BigDecimal(valurSTR[1]) : BigDecimal.ZERO;
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
