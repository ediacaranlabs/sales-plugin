package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;

public interface ProductTypeSearchResult {

	int getPages();
	
	int getPage();
	
	List<Product> getItens();
	
}
