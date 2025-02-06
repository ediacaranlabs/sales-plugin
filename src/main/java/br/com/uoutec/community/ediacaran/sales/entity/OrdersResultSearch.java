package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.List;

public class OrdersResultSearch implements Serializable {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<OrderResultSearch> data;

	public OrdersResultSearch(boolean hasNextPage, int maxPages, int page, List<OrderResultSearch> data) {
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

	public List<OrderResultSearch> getData() {
		return data;
	}

	public void setData(List<OrderResultSearch> data) {
		this.data = data;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
}
