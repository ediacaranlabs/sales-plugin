package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class RefundResultSearchItemPubEntity extends AbstractPubEntity<Refund>{

	private static final long serialVersionUID = -4887905873425384110L;

	private String id;
	
	private String client;

	private String date;

	private String refundDate;
	
	private String status;
	
	@Constructor
	public RefundResultSearchItemPubEntity() {
	}
	
	public RefundResultSearchItemPubEntity(Refund refund, Locale locale, DateTimeFormatter dateTimeFormatter) {
		this.id = refund.getId();
		this.status = refund.getStatus() == null? null : refund.getStatus().getName(locale);
		this.client = refund.getClient().getFirstName() + " " + refund.getClient().getLastName();
		this.date = refund.getDate() == null? null : dateTimeFormatter.format(refund.getDate());
		this.refundDate = refund.getRefundDate() == null? null : dateTimeFormatter.format(refund.getRefundDate());
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}

	@Override
	protected boolean isEqualId(Refund instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(Refund instance) throws Throwable {
		return false;
	}

	@Override
	protected Refund reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected Refund createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(Refund o, boolean reload, boolean override, boolean validate) throws Throwable {
	}

}
