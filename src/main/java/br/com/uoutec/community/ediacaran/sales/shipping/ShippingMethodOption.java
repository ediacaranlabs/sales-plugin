package br.com.uoutec.community.ediacaran.sales.shipping;

import java.math.BigDecimal;

public class ShippingMethodOption {

	private String id;
	
	private String title;
	
	private BigDecimal cost;
	
	private BigDecimal value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
}
