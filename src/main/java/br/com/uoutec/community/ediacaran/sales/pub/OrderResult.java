package br.com.uoutec.community.ediacaran.sales.pub;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class OrderResult {

	private String id;
	
	private String owner;
	
	private String date;
	
	private String status;

	private BigDecimal subTotal;

	private BigDecimal taxes;
	
	private BigDecimal total;
	
	private String invoice;

	public OrderResult(Order order, SystemUser user, Locale locale) {
		this.id = order.getId();
		this.owner = user == null? null : user.getFirstName() + " " + user.getLastName();
		this.date = order.getDate() == null? null : DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale).format(order.getDate());
		this.status = order.getStatus() == null? null : order.getStatus().getName(locale);
		this.subTotal = order.getSubtotal();
		this.taxes = order.getTax();
		this.total = order.getTotal();
		this.invoice = order.getInvoice() == null? null : order.getInvoice().getId();
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

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	
}
