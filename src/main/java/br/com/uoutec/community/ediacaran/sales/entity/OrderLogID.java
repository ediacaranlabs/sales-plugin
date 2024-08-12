package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

public class OrderLogID implements Serializable{

	private static final long serialVersionUID = 4030899553763676280L;

	private String orderID;
	
	private int id;

	public OrderLogID() {
	}

	public OrderLogID(String orderID, int id) {
		this.orderID = orderID;
		this.id = id;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((orderID == null) ? 0 : orderID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderLogID other = (OrderLogID) obj;
		if (id != other.id)
			return false;
		if (orderID == null) {
			if (other.orderID != null)
				return false;
		} else if (!orderID.equals(other.orderID))
			return false;
		return true;
	}
	
}
