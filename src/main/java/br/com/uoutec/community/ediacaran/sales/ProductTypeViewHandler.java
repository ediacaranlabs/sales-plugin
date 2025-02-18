package br.com.uoutec.community.ediacaran.sales;

import java.util.Locale;

import org.brandao.brutos.web.WebResultAction;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.pub.entity.InvalidRequestException;

public interface ProductTypeViewHandler extends PublicBean{

	WebResultAction getProductOrderView(ProductRequest product);

	WebResultAction getProductOrderView(String code);
	
	WebResultAction getProductFormView(ProductPubEntity productPubEntity, Locale locale);

	WebResultAction getProductCartView(ProductRequest product);

	WebResultAction getProductCartView(String code);
	
	WebResultAction edit(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException;
	
	WebResultAction save(ProductPubEntity productPubEntity,Locale locale) throws InvalidRequestException;

	WebResultAction remove(ProductPubEntity productPubEntity,Locale locale) throws InvalidRequestException;
	
	WebResultAction updateView(ProductPubEntity productPubEntity, String code, Locale locale) throws InvalidRequestException;
	
}
