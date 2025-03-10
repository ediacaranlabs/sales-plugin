package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Product;

public class ProductEntitySearchResult {

	private List<Product> itens;
	
	private Map<Integer,ProductMetadataSearchResultEntityFilter> filters;

	public ProductEntitySearchResult() {
	}
	
	public List<Product> getItens() {
		return itens;
	}

	public void setItens(List<Product> itens) {
		this.itens = itens;
	}

	public Map<Integer,ProductMetadataSearchResultEntityFilter> getFilters() {
		return filters;
	}

	public void setFilters(Map<Integer,ProductMetadataSearchResultEntityFilter> filters) {
		this.filters = filters;
	}
	
}
