package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;

public class ProductType implements Serializable{

	private static final long serialVersionUID = -7978798526200588374L;

	private final String code;
	
	private final String name;
	
	private final int maxExtra;
	
	private ProductTypeHandler handler;

	public ProductType(String code, String name, int maxExtra, ProductTypeHandler handler) {
		this.code = code;
		this.name = name;
		this.maxExtra = maxExtra;
		this.handler = handler;
	}

	public ProductTypeHandler getHandler() {
		return handler;
	}

	public void setHandler(ProductTypeHandler handler) {
		this.handler = handler;
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
