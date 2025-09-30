package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchFilter;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductSearchPubEntity extends AbstractPubEntity<ProductSearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	private String name;
	
	private String description;

	private String category;
	
	private BigDecimal minCost;

	private BigDecimal maxCost;

	private String productType;
	
	private Boolean display;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductSearchFilterPubEntity> filters;
	
	@Max(100)
	private Integer resultPerPage;

	@Transient
	private Locale locale;
	
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
		o.setDisplay(display);
		
		if(this.category != null) {
			ProductCategory c = new ProductCategory();
			c.setProtectedID(this.category);
			o.setCategory(c);
		}

		if(this.filters != null) {
			Set<ProductSearchFilter> set = new HashSet<>();
			for(ProductSearchFilterPubEntity x: this.filters) {
				x.setLocale(locale);
				set.add(x.rebuild(true, true, true));
			}
			o.setFilters(set);
		}
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

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
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

	public List<ProductSearchFilterPubEntity> getFilters() {
		return filters;
	}

	public void setFilters(List<ProductSearchFilterPubEntity> filters) {
		this.filters = filters;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}
