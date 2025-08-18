package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrderReportResultSearchItemPubEntity extends AbstractPubEntity<OrderReport>{

	private static final long serialVersionUID = -4887905873425384110L;

	private String id;
	
	private String user;
	
	private String client;

	private String date;

	private Long daysAfterCreated;
	
	private String status;
	
	private Boolean closed;
	
	@Constructor
	public OrderReportResultSearchItemPubEntity() {
	}
	
	public OrderReportResultSearchItemPubEntity(OrderReport e, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id = e.getId();
		this.client = e.getClient().getFirstName() + " " + e.getClient().getLastName();
		this.date = e.toStringDate(locale);
		this.daysAfterCreated = e.getDaysAfterCreated();
		this.status = e.getStatus() == null? null : e.getStatus().getName(locale);
		this.closed = e.getStatus() == OrderReportStatus.CLOSED;
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

	public Long getDaysAfterCreated() {
		return daysAfterCreated;
	}

	public void setDaysAfterCreated(Long daysAfterCreated) {
		this.daysAfterCreated = daysAfterCreated;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	protected boolean isEqualId(OrderReport instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(OrderReport instance) throws Throwable {
		return false;
	}

	@Override
	protected OrderReport reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected OrderReport createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(OrderReport o, boolean reload, boolean override, boolean validate) throws Throwable {
	}

}
