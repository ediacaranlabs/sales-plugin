package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;

public class ShippingsResultSearch {

	private boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<ShippingResultSearch> itens;

	public ShippingsResultSearch(boolean hasNextPage, int maxPages, int page, List<ShippingResultSearch> itens) {
		super();
		this.maxPages = maxPages;
		this.page = page;
		this.itens = itens;
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public int getPage() {
		return page;
	}

	public List<ShippingResultSearch> getItens() {
		return itens;
	}
	
}
