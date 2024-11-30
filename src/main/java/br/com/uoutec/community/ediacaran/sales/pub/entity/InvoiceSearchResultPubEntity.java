package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.InvoiceEntitySearchResultPubEntity;

public class InvoiceSearchResultPubEntity implements Serializable {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<InvoiceEntitySearchResultPubEntity> data;

	public InvoiceSearchResultPubEntity(int maxPages, int page, boolean hasNextPage, List<InvoiceEntitySearchResultPubEntity> data) {
		super();
		this.maxPages = maxPages;
		this.page = page;
		this.data = data;
		this.hasNextPage = hasNextPage;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<InvoiceEntitySearchResultPubEntity> getData() {
		return data;
	}

	public void setData(List<InvoiceEntitySearchResultPubEntity> data) {
		this.data = data;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
}
