package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.ProductTypeViewHandler;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class ProductType implements Serializable{

	private static final long serialVersionUID = -7978798526200588374L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[A-Z0-9]{1,15}", groups = IdValidation.class)
	private final String code;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	private final String name;

	@Min(0)
	private final int maxExtra;
	
	@NotNull(groups = DataValidation.class)
	private final ProductTypeHandler handler;

	@NotNull(groups = DataValidation.class)
	private final ProductTypeViewHandler viewHandler;
	
	public ProductType(String code, String name, int maxExtra, ProductTypeHandler handler, ProductTypeViewHandler viewHandler) {
		this.code = code;
		this.name = name;
		this.maxExtra = maxExtra;
		this.handler = handler;
		this.viewHandler = viewHandler;
	}

	public ProductTypeViewHandler getViewHandler() {
		return viewHandler;
	}

	public ProductTypeHandler getHandler() {
		return handler;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getMaxExtra() {
		return maxExtra;
	}

}
