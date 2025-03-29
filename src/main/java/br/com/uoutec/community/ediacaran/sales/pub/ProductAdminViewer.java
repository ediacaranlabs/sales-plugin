package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Locale;

import org.brandao.brutos.web.WebResultAction;

import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSearchResultPubEntity;
import br.com.uoutec.pub.entity.InvalidRequestException;

public interface ProductAdminViewer {

	WebResultAction showProductSearch(Locale locale) throws InvalidRequestException;

	ProductsSearchResultPubEntity searchProduct(ProductSearchPubEntity productSearch,Locale locale) throws InvalidRequestException;
	
	WebResultAction showProductEdit(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException;
	
	WebResultAction showProductEdit(ProductPubEntity productPubEntity, String area, Locale locale) throws InvalidRequestException;
	
	WebResultAction saveProduct(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException;
	
	WebResultAction removeProduct(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException;
	
}
