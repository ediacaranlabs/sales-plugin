package br.com.uoutec.community.ediacaran.sales.entity;

import java.math.BigDecimal;

public class ProductTypeSearch {

	private String name;
	
	private String description;
	
	private BigDecimal minCost;

	private BigDecimal maxCost;

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

	public BigDecimal getMinCost() {
		return minCost;
	}

	public void setMinCost(BigDecimal minCost) {
		this.minCost = minCost;
	}

	public BigDecimal getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(BigDecimal maxCost) {
		this.maxCost = maxCost;
	}
	
}
