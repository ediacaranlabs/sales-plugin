package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;

@SessionScoped
public class AdminCart implements Serializable{

	private static final long serialVersionUID = -7174582418923726908L;

	private Cart cart;
	
	public AdminCart() {
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Client getClient() {
		return cart.getClient();
	}

	public void setClient(Client client) {
		cart.setClient(client);
	}

	public Address getBillingAddress() {
		return cart.getBillingAddress();
	}

	public void setBillingAddress(Address billingAddress) {
		cart.setBillingAddress(billingAddress);
	}

	public Address getShippingAddress() {
		return cart.getShippingAddress();
	}

	public void setShippingAddress(Address shippingAddress) {
		cart.setShippingAddress(shippingAddress);
	}
	
}
