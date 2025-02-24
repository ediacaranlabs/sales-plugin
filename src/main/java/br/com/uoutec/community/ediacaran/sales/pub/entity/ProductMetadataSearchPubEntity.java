package br.com.uoutec.community.ediacaran.sales.pub.entity;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductMetadataSearchPubEntity extends AbstractPubEntity<ProductMetadataSearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	private String name;
	
	private Integer page;
	
	@Max(100)
	private Integer resultPerPage;

	@Override
	protected boolean isEqualId(ProductMetadataSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductMetadataSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductMetadataSearch reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected ProductMetadataSearch createNewInstance() throws Throwable {
		return new ProductMetadataSearch();
	}

	@Override
	protected void copyTo(ProductMetadataSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setName(this.name);
		o.setPage(page);
		o.setResultPerPage(resultPerPage);
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
	
}
