package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;

public class ShippingResultSearch {

	private Boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<Shipping> itens;

	public ShippingResultSearch(boolean hasNextPage, int maxPages, int page, List<Shipping> itens) {
		super();
		this.maxPages = maxPages;
		this.page = page;
		this.itens = itens;
		this.hasNextPage = hasNextPage;
	}

	public Boolean getHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(Boolean hasNextPage) {
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

	public List<Shipping> getItens() {
		return itens;
	}

	public void setItens(List<Shipping> itens) {
		this.itens = itens;
	}
	
}
