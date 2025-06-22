package br.com.uoutec.community.ediacaran.sales;

import java.util.Locale;

import org.brandao.brutos.web.WebResultAction;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.pub.entity.InvalidRequestException;

public interface ProductTypeViewHandler extends PublicBean{

	WebResultAction getProductOrderView(ProductRequest product);

	WebResultAction getProductOrderView(String code);
	
	WebResultAction getProductFormView(Product entity, Locale locale);

	WebResultAction getProductCartView(ProductRequest product);

	WebResultAction getProductCartView(String code);
	
	WebResultAction edit(Product entity, Locale locale) throws InvalidRequestException;

	WebResultAction save(Product entity, Throwable exception, Locale locale) throws InvalidRequestException;

	WebResultAction remove(Product entity, Throwable exception, Locale locale) throws InvalidRequestException;
	
	WebResultAction updateView(Product entity, String code, Locale locale) throws InvalidRequestException;
	
}
