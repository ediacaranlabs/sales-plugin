package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataSearchResultEntityFilter;

public class ProductSearchResult implements Serializable{

	private static final long serialVersionUID = 946881672430631849L;

	private boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<ProductMetadataSearchResultEntityFilter> filters;
	
	private List<Product> itens;

	public ProductSearchResult() {
	}
	
	public ProductSearchResult(boolean hasNextPage, int maxPages, int page, List<Product> itens, List<ProductMetadataSearchResultEntityFilter> filters) {
		this.hasNextPage = hasNextPage;
		this.maxPages = maxPages;
		this.page = page;
		this.itens = itens;
		this.filters = filters;
	}

	public List<ProductMetadataSearchResultEntityFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ProductMetadataSearchResultEntityFilter> filters) {
		this.filters = filters;
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

	public List<Product> getItens() {
		return itens;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setItens(List<Product> itens) {
		this.itens = itens;
	}

}
