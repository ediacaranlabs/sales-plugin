package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class OrderResultSearch {

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

	public SystemUser getOwner() {
		return owner;
	}

	public void setOwner(Client owner) {
		this.owner = owner;
	}
	
}
