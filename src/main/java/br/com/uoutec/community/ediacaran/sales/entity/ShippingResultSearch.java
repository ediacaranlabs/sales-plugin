package br.com.uoutec.community.ediacaran.sales.entity;

public class ShippingResultSearch {

	private Shipping shipping;
	
	private Client owner;

	public ShippingResultSearch(Shipping shipping, Client owner) {
		this.shipping = shipping;
		this.owner = owner;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public Client getOwner() {
		return owner;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public void setOwner(Client owner) {
		this.owner = owner;
	}
	
}
