package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ShippingResultSearchItemPubEntity extends AbstractPubEntity<Shipping>{

	private static final long serialVersionUID = -4887905873425384110L;

	private String id;
	
	private String client;

	private String date;

	private String receivedDate;
	
	private Long daysAfterCreated;
	
	private Boolean closed;
	
	
	@Constructor
	public ShippingResultSearchItemPubEntity() {
	}
	
	public ShippingResultSearchItemPubEntity(Shipping shipping, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id = shipping.getId();
		this.client = shipping.getClient().getFirstName() + " " + shipping.getClient().getLastName();
		this.date = shipping.getDate() == null? null : dateTimeFormatter.format(shipping.getDate());
		this.receivedDate = shipping.getReceivedDate() == null? null : dateTimeFormatter.format(shipping.getReceivedDate());
		this.closed = shipping.isClosed();
		this.daysAfterCreated = shipping.getDaysAfterCreated();
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

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
	
	public Long getDaysAfterCreated() {
		return daysAfterCreated;
	}

	public void setDaysAfterCreated(Long daysAfterCreated) {
		this.daysAfterCreated = daysAfterCreated;
	}

	@Override
	protected boolean isEqualId(Shipping instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(Shipping instance) throws Throwable {
		return false;
	}

	@Override
	protected Shipping reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected Shipping createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(Shipping o, boolean reload, boolean override, boolean validate) throws Throwable {
	}

}
