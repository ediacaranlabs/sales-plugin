package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.List;

public class ProductMetadataSearchResult implements Serializable{

	private static final long serialVersionUID = 946881672430631849L;

	private boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<ProductMetadata> itens;

	public ProductMetadataSearchResult() {
	}
	
	public ProductMetadataSearchResult(boolean hasNextPage, int maxPages, int page, List<ProductMetadata> itens) {
		this.hasNextPage = hasNextPage;
		this.maxPages = maxPages;
		this.page = page;
		this.itens = itens;
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

	public List<ProductMetadata> getItens() {
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

	public void setItens(List<ProductMetadata> itens) {
		this.itens = itens;
	}

}
