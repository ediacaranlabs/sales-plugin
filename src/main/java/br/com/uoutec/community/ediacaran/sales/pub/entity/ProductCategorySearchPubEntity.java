package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;

import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategorySearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductCategorySearchPubEntity extends AbstractPubEntity<ProductCategorySearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	private Integer id;
	
	private String name;

	private String parent;
	
	private Integer page;
	
	@Max(100)
	private Integer resultPerPage;

	@Transient
	private Locale locale;
	
	@Override
	protected boolean isEqualId(ProductCategorySearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductCategorySearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductCategorySearch reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected ProductCategorySearch createNewInstance() throws Throwable {
		return new ProductCategorySearch();
	}

	@Override
	protected void copyTo(ProductCategorySearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setId(this.id);
		o.setName(this.name);
		o.setPage(this.page);
		
		if(parent != null) {
			ProductCategory c = new ProductCategory();
			c.setProtectedID(this.parent);
			o.setParent(c);
		}
		
		o.setResultPerPage(this.resultPerPage);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
}
