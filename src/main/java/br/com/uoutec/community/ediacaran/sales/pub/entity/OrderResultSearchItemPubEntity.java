package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrderResultSearchItemPubEntity extends AbstractPubEntity<OrderResultSearch>{

	private static final long serialVersionUID = -4887905873425384110L;

	private String id;
	
	private String owner;

	private String date;
	
	private String status;

	private String subTotal;

	private String taxes;
	
	private String total;
	
	//private String invoice;

	@Constructor
	public OrderResultSearchItemPubEntity() {
		
	}
	public OrderResultSearchItemPubEntity(Order order, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id = order.getId();
		this.owner = order.getClient().getFirstName() + " " + order.getClient().getLastName();
		//this.date = order.getDate() == null? null : DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale).format(order.getDate());
		this.date = order.getDate() == null? null : dateTimeFormatter.format(order.getDate());
		this.status = order.getStatus() == null? null : order.getStatus().getName(locale);
		this.subTotal = order.getDisplaySubtotal();
		this.taxes = order.getDisplayTax();
		this.total = order.getDisplayTotal();
		//this.invoice = orderResultSearch.getOrder().getInvoice() == null? null : orderResultSearch.getOrder().getInvoice().getId();
	}

	@Override
	protected boolean isEqualId(OrderResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(OrderResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected OrderResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected OrderResultSearch createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(OrderResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
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
	
}
