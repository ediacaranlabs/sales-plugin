package br.com.uoutec.community.ediacaran.sales;

import java.util.Locale;

import org.brandao.brutos.ResultAction;

import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.pub.entity.InvalidRequestException;

public interface ProductTypeViewHandler extends PublicBean{

	String getProductOrderView();
	
	String getProductFormView();
	
	String getProductCartView();
	
	ResultAction edit(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException;
	
	ResultAction save(ProductPubEntity productPubEntity,Locale locale) throws InvalidRequestException;

	ResultAction remove(ProductPubEntity productPubEntity,Locale locale) throws InvalidRequestException;
	
	ResultAction updateView(ProductPubEntity productPubEntity, String code, Locale locale) throws InvalidRequestException;
	
}
