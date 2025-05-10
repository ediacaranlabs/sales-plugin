package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InvoiceResultSearchPubEntity implements Serializable {

	private static final long serialVersionUID = 9166429937476180931L;

	private static final DecimalFormat df = new DecimalFormat("###,###,##0.00"); 
	private String id;
	
	private String owner;
	
	private String order;
	
	private String date;
	
	private String subTotal;

	private String taxes;

	private String discounts;
	
	private String total;
	
	//private String invoice;

	public InvoiceResultSearchPubEntity(InvoiceResultSearch invoiceResultSearch, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id = invoiceResultSearch.getInvoice().getId();
		this.owner = invoiceResultSearch.getOwner().getFirstName() + " " + invoiceResultSearch.getOwner().getLastName();
		this.date = invoiceResultSearch.getInvoice().getDate() == null? null : dateTimeFormatter.format(invoiceResultSearch.getInvoice().getDate());
		this.order = invoiceResultSearch.getInvoice().getOrder();
		this.subTotal = invoiceResultSearch.getInvoice().getSymbol() + " " + df.format(invoiceResultSearch.getInvoice().getSubtotal());
		this.taxes = invoiceResultSearch.getInvoice().getSymbol() + " " + df.format(invoiceResultSearch.getInvoice().getTax());
		this.discounts = invoiceResultSearch.getInvoice().getSymbol() + " " + df.format(invoiceResultSearch.getInvoice().getDiscount());
		this.total = invoiceResultSearch.getInvoice().getSymbol() + " " + df.format(invoiceResultSearch.getInvoice().getTotal());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDiscounts() {
		return discounts;
	}

	public void setDiscounts(String discounts) {
		this.discounts = discounts;
	}

	/*
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
    */
	
}
