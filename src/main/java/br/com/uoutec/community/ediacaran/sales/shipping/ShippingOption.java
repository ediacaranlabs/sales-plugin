package br.com.uoutec.community.ediacaran.sales.shipping;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;

public class ShippingOption {

	private String id;
	
	private String method;
	
	private String title;
	
	private Integer productID;
	
	private String currency;
	
	private BigDecimal cost;
	
	private BigDecimal value;

	public ShippingOption() {
	}
	
	public ShippingOption(String id, String method, String title, String currency, BigDecimal cost, BigDecimal value) {
		this(id, method, title, null, currency, cost, value);
	}
	
	public ShippingOption(String id, String method, String title, Integer productID, String currency, BigDecimal cost, BigDecimal value) {
		this.id = id;
		this.method = method;
		this.title = title;
		this.currency = currency;
		this.cost = cost;
		this.value = value;
		this.productID = productID;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public String toString(Locale locale) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
		DecimalFormat df = new DecimalFormat("###,###,##0.00", dfs); 
		return Currency.getInstance(currency).getSymbol() + " " + df.format(getValue()) + " " + this.toString(); 
	}
	
	public String toString() {
		return title; 
	}
	
}
