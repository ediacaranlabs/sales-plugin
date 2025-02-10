package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public abstract class AbstractProductTypeViewHandler 
	implements ProductTypeViewHandler{
	
	@Override
	public String getProductFormView() {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		return varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product_form.jsp");
	}
	
	@Override
	public String getProductCartView() {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		return varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/front/cart/product.jsp");
	}
	
	@Override
	public String getProductOrderView() {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		return varParser.getValue("${plugins.ediacaran.sales.web_path}:${plugins.ediacaran.sales.template}/admin/cart/product.jsp");
	}
	
}
