package br.com.uoutec.community.ediacaran.sales.registry;

import org.brandao.brutos.ResultAction;

import br.com.uoutec.community.ediacaran.sales.entity.Product;

public interface ProductWidget {

	String getId();
	
	String getTitle();
	
	ResultAction getContent(Product product);
	
}
