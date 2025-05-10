package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrderResultSearchPubEntity extends AbstractPubEntity<OrderResultSearch>{

	private static final long serialVersionUID = -4887905873425384110L;

	private static final DecimalFormat df = new DecimalFormat("###,###,##0.00");
	
	private String id;
	
	private String owner;

	private String date;
	
	private String status;

	private String subTotal;

	private String taxes;
	
	private String total;
	
	//private String invoice;

	public OrderResultSearchPubEntity(OrderResultSearch orderResultSearch, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id = orderResultSearch.getOrder().getId();
		this.owner = orderResultSearch.getOwner().getFirstName() + " " + orderResultSearch.getOwner().getLastName();
		//this.date = order.getDate() == null? null : DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale).format(order.getDate());
		this.date = orderResultSearch.getOrder().getDate() == null? null : dateTimeFormatter.format(orderResultSearch.getOrder().getDate());
		this.status = orderResultSearch.getOrder().getStatus() == null? null : orderResultSearch.getOrder().getStatus().getName(locale);
		this.subTotal = orderResultSearch.getOrder().getSymbol() + " " + df.format(orderResultSearch.getOrder().getSubtotal());
		this.taxes = orderResultSearch.getOrder().getSymbol() + " " + df.format(orderResultSearch.getOrder().getTax());
		this.total = orderResultSearch.getOrder().getSymbol() + " " + df.format(orderResultSearch.getOrder().getTotal());
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
