package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

public class ShippingType implements Serializable{

	private static final long serialVersionUID = 7593192344481504325L;

	private int id;

	private String name;
	
	private String className;
	
	public ShippingType(){
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
