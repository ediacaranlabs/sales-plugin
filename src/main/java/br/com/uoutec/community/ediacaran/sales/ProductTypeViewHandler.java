package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ProductTypeViewHandler extends PublicBean{

	String getProductOrderView();
	
	String getProductFormView();
	
	String getProductCartView();
	
	String getProductRegisterView();
	
}
