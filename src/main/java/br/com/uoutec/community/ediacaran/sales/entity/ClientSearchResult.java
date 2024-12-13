package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.List;

public class ClientSearchResult implements Serializable {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<Client> itens;

	public ClientSearchResult() {
	}
	
	public ClientSearchResult(boolean hasNextPage, int maxPages, int page, List<Client> itens) {
		super();
		this.maxPages = maxPages;
		this.page = page;
		this.itens = itens;
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
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

	public List<Client> getItens() {
		return itens;
	}

	public void setItens(List<Client> itens) {
		this.itens = itens;
	}
	
}
