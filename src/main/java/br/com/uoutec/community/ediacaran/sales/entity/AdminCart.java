package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;

@SessionScoped
public class AdminCart implements Serializable{

	private static final long serialVersionUID = -7174582418923726908L;

	private Cart cart;
	
	private Client client;
	
	private Address billingAddress;
	
	private Address shippingAddress;
	
	public AdminCart() {
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
}
