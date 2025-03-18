package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResultFilter;

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
	
	public ProductSearchResult toEntity(int maxItens, int maxPages, int page) {
		ProductSearchResult e = new ProductSearchResult();
		e.setHasNextPage(itens.size() > maxItens);
		e.setMaxPages(maxPages);
		e.setPage(page);
		e.setItens(itens.size() > maxItens? itens.subList(0, maxItens -1) : itens);
		
		if(this.filters != null) {
			List<ProductSearchResultFilter> list = new ArrayList<>();
			for(ProductMetadataSearchResultEntityFilter f: this.filters.values()) {
				list.add(f.toEntity());
			}
			e.setFilters(list);
		}
		return e;
	}
}
