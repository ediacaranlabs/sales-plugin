package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Locale;

import org.brandao.brutos.web.WebResultAction;

import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSimplifiedSearchResultPubEntity;
import br.com.uoutec.pub.entity.InvalidRequestException;

public interface ProductViewer {

	WebResultAction showProductSearch(Locale locale) throws InvalidRequestException;
	
	ProductsSimplifiedSearchResultPubEntity searchProduct(ProductSearchPubEntity productSearch, Locale locale) throws InvalidRequestException;

	WebResultAction showProduct(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException;
	
}
