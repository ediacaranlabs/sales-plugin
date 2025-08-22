package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrderReportMessageResultSearchItemPubEntity extends AbstractPubEntity<OrderReportMessage>{

	private static final long serialVersionUID = -4887905873425384110L;

	private String userName;
	
	private String date;
	
	private String message;
	
	private boolean client;
	
	@Constructor
	public OrderReportMessageResultSearchItemPubEntity() {
	}
	
	public OrderReportMessageResultSearchItemPubEntity(OrderReportMessage e, OrderReport orderReport, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.userName = e.getUser() == null? null : e.getUser().getFirstName() + " " + e.getUser().getLastName();
		this.date = e.toStringDate(locale);
		this.message = e.getMessage();
		this.client = e.getUser() == null || orderReport.getClient() == null? false : e.getUser().getId().equals(orderReport.getClient().getId());
	}

	public boolean isClient() {
		return client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	protected boolean isEqualId(OrderReportMessage instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(OrderReportMessage instance) throws Throwable {
		return false;
	}

	@Override
	protected OrderReportMessage reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected OrderReportMessage createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(OrderReportMessage o, boolean reload, boolean override, boolean validate) throws Throwable {
	}

}
