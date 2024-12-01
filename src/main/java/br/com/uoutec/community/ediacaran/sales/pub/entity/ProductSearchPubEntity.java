package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductSearchPubEntity extends AbstractPubEntity<ProductSearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	private String name;
	
	private String description;
	
	private BigDecimal minCost;

	private BigDecimal maxCost;

	@NotNull
	private String productType;
	
	private Integer page;
	
	@Max(100)
	private Integer resultPerPage;

	@Override
	protected boolean isEqualId(ProductSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductSearch reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected ProductSearch createNewInstance() throws Throwable {
		return new ProductSearch();
	}

	@Override
	protected void copyTo(ProductSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setMaxCost(this.maxCost);
		o.setMinCost(this.minCost);
		o.setName(this.name);
		o.setPage(page);
		o.setProductType(this.productType);
		o.setResultPerPage(resultPerPage);
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
	
}
