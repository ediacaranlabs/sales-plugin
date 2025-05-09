package br.com.uoutec.community.ediacaran.sales.entity;

import java.math.BigDecimal;
import java.util.Set;

public class ProductSearch {

	private String name;
	
	private String description;
	
	private BigDecimal minCost;

	private BigDecimal maxCost;

	private Set<ProductSearchFilter> filters;
	
	private String productType;
	
	private Integer page;
	
	private Integer resultPerPage;
	
	private Boolean display;
	
	public Set<ProductSearchFilter> getFilters() {
		return filters;
	}

	public void setFilters(Set<ProductSearchFilter> filters) {
		this.filters = filters;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getResultPerPage() {
		return resultPerPage;
	}

	public void setResultPerPage(Integer resultPerPage) {
		this.resultPerPage = resultPerPage;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

}
