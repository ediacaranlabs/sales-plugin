package br.com.uoutec.community.ediacaran.sales.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductTypeSearch {

	private String name;
	
	private String description;
	
	private BigDecimal minCost;

	private BigDecimal maxCost;

	private Map<String,String> params;
	
	public ProductTypeSearch() {
		this.params = new HashMap<>();
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
	
	public void set(String name, String value) {
		params.put(name, value);
	}

	public String get(String name) {
		return params.get(name);
	}
	
}
