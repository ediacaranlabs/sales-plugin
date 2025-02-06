package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

public class OrderResultSearch implements Serializable {

	private static final long serialVersionUID = 9166429937476180931L;

	private Order order;
	
	private Client owner;

	public OrderResultSearch(Order order, Client owner) {
		this.order = order;
		this.owner = owner;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Client getOwner() {
		return owner;
	}

	public void setOwner(Client owner) {
		this.owner = owner;
	}
	
	
}
