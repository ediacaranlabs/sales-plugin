package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;

public class OrderReportMessageResultSearch {

	private Boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<OrderReportMessage> itens;

	public OrderReportMessageResultSearch(boolean hasNextPage, int maxPages, int page, List<OrderReportMessage> itens) {
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

	public List<OrderReportMessage> getItens() {
		return itens;
	}

	public void setItens(List<OrderReportMessage> itens) {
		this.itens = itens;
	}
	
}
