package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

public class ProductType implements Serializable{

	private static final long serialVersionUID = -7978798526200588374L;

	private int id;
	
	private String code;
	
	private String name;
	
	private int maxExtra;
	
	private String className;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxExtra() {
		return maxExtra;
	}

	public void setMaxExtra(int maxExtra) {
		this.maxExtra = maxExtra;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
