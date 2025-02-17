package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Locale;

import org.brandao.brutos.ResultAction;

import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSearchResultPubEntity;
import br.com.uoutec.pub.entity.InvalidRequestException;

public interface ProductViewerHandler {

	String getID();
	
	ResultAction showProductSearch(Locale locale) throws InvalidRequestException;
	
	ProductsSearchResultPubEntity searchProduct(ProductSearchPubEntity productSearch, Locale locale) throws InvalidRequestException;

	ResultAction showProduct(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException;
	
}
