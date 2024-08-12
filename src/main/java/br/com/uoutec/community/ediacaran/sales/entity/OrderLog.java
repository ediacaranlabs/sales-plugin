package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.Date;

public class OrderLog implements Serializable{

	private static final long serialVersionUID = 2795809142976062486L;

	private int id;
	
	private int systemUserId;
	
	private String orderId;
	
	private Date date;
	
	private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSystemUserId() {
		return systemUserId;
	}

	public void setSystemUserId(int systemUserId) {
		this.systemUserId = systemUserId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
