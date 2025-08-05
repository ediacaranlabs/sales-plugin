package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class InvoiceSearchResultItemPubEntity extends AbstractPubEntity<Invoice> {

	private static final long serialVersionUID = 9166429937476180931L;

	private String id;
	
	private String client;
	
	private String order;
	
	private String date;
	
	private String subTotal;

	private String taxes;

	private String discounts;
	
	private String total;
	
	public InvoiceSearchResultItemPubEntity(Invoice invoice, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id =  invoice.getId();
		this.client = invoice.getClient().getFirstName() + " " + invoice.getClient().getLastName();
		this.date = invoice.getDate() == null? null : dateTimeFormatter.format(invoice.getDate());
		this.order = invoice.getOrder();
		this.subTotal = invoice.getDisplaySubtotal();
		this.taxes = invoice.getDisplayTax();
		this.discounts = invoice.getDisplayDiscount();
		this.total = invoice.getDisplayTotal();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
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

	@Override
	protected boolean isEqualId(Invoice instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(Invoice instance) throws Throwable {
		return false;
	}

	@Override
	protected Invoice reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected Invoice createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(Invoice o, boolean reload, boolean override, boolean validate) throws Throwable {
	}
	
}
