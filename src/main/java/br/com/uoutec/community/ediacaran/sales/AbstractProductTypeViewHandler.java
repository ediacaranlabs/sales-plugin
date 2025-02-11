package br.com.uoutec.community.ediacaran.sales;

import java.util.Locale;

import org.brandao.brutos.ResultAction;
import org.brandao.brutos.ResultActionImp;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.InvalidRequestException;

public abstract class AbstractProductTypeViewHandler 
	implements ProductTypeViewHandler{
	
	@Override
	public ResultAction edit(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultAction save(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultAction remove(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultAction updateView(ProductPubEntity productPubEntity, String code, Locale locale)
			throws InvalidRequestException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ResultAction getProductFormView(ProductPubEntity productPubEntity, Locale locale) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		ResultAction ra = new ResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product_form.jsp"), true);
		return ra;
	}
	
	@Override
	public ResultAction getProductCartView(ProductRequest product) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		ResultAction ra = new ResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/front/cart/product.jsp"), true);
		return ra;
	}

	@Override
	public ResultAction getProductCartView(String productType) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		ResultAction ra = new ResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/front/cart/product.jsp"), true);
		return ra;
	}
	
	@Override
	public ResultAction getProductOrderView(ProductRequest product) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		ResultAction ra = new ResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product.jsp"), true);
		return ra;
	}

	@Override
	public ResultAction getProductOrderView(String productType) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		ResultAction ra = new ResultActionImp();
		ra.setView(varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product.jsp"), true);
		return ra;
	}
	
}
