package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OrderResult implements Serializable {

	private static final long serialVersionUID = 9166429937476180931L;

	private static final DecimalFormat df = new DecimalFormat("###,###,##0.00");
	
	private String id;
	
	private String owner;

	private String date;
	
	private String status;

	private String subTotal;

	private String taxes;
	
	private String total;
	
	//private String invoice;

	public OrderResult(OrderResultSearch orderResultSearch, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id = orderResultSearch.getOrder().getId();
		this.owner = orderResultSearch.getOwner().getFirstName() + " " + orderResultSearch.getOwner().getLastName();
		//this.date = order.getDate() == null? null : DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale).format(order.getDate());
		this.date = orderResultSearch.getOrder().getDate() == null? null : dateTimeFormatter.format(orderResultSearch.getOrder().getDate());
		this.status = orderResultSearch.getOrder().getStatus() == null? null : orderResultSearch.getOrder().getStatus().getName(locale);
		this.subTotal = orderResultSearch.getOrder().getCurrency() + " " + df.format(orderResultSearch.getOrder().getSubtotal());
		this.taxes = orderResultSearch.getOrder().getCurrency() + " " + df.format(orderResultSearch.getOrder().getTax());
		this.total = orderResultSearch.getOrder().getCurrency() + " " + df.format(orderResultSearch.getOrder().getTotal());
		//this.invoice = orderResultSearch.getOrder().getInvoice() == null? null : orderResultSearch.getOrder().getInvoice().getId();
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	/*
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
    */
	
}
