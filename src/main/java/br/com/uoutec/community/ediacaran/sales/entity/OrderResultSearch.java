package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class OrderResultSearch {

	private Order order;
	
	private SystemUser owner;

	public OrderResultSearch(Order order, SystemUser owner) {
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

	public void setOwner(SystemUser owner) {
		this.owner = owner;
	}
	
}
