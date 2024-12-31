package br.com.uoutec.community.ediacaran.sales.shipping;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ShippingOption {

	private String id;
	
	private String method;
	
	private String title;
	
	private String currency;
	
	private BigDecimal cost;
	
	private BigDecimal value;

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
		return currency + " " + df.format(value) + " " + title + " (" + method + ")"; 
	}
	
}
