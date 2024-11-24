package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ProductRequestRecalcPubEntity implements Serializable{

	private static final long serialVersionUID = -3865099353684532991L;

	private static final DecimalFormat df = new DecimalFormat("###,###,##0.00"); 
	
	private String serial;
	
	private int units;

	private String name;
	
	private String shortDescription;
	
	private String description;
	
	private String currency;

	private String subtotal;
	
	private String taxes;
	
	private String discounts;
	
	private String total;
	
	public ProductRequestRecalcPubEntity(ProductRequest e) {
		this.currency = e.getCurrency();
		this.description = e.getDescription();
		this.name = e.getName();
		this.serial = e.getSerial();
		this.shortDescription = e.getShortDescription();
		this.units = e.getUnits();
		this.subtotal = df.format(e.getSubtotal());
		this.taxes = df.format(e.getTax());
		this.discounts = df.format(e.getDiscount());
		this.total = df.format(e.getTotal());
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

	public String getDiscounts() {
		return discounts;
	}

	public void setDiscounts(String discounts) {
		this.discounts = discounts;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
}
