package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.HashMap;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;

public class PaymentRequest {

	private Payment payment;
	
	private PaymentLocation location;
	
	public PaymentRequest() {
	}
	
	public PaymentRequest(Client client, Payment payment) {
		this.location = new PaymentLocation();
		this.location.setCountry(client.getCountry());
		this.location.setCity(client.getCity());
		this.location.setRegion(client.getRegion());
		this.location.setZip(client.getZip());
		this.payment = payment;
	}

	public PaymentRequest(Client client, Cart cart) {
		this.location = new PaymentLocation();
		this.location.setCountry(client.getCountry());
		this.location.setCity(client.getCity());
		this.location.setRegion(client.getRegion());
		this.location.setZip(client.getZip());
		
		this.payment = new Payment();
		this.payment.setValue(cart.getSubtotal());
		this.payment.setTax(cart.getTotalTax());
		this.payment.setDiscount(cart.getTotalDiscount());
		this.payment.setTotal( cart.getTotal());
		this.payment.setCurrency(cart.getCurrency());
		this.payment.setAddData(new HashMap<>());
	}

	public Payment getPayment() {
		return payment;
	}

	public PaymentLocation getLocation() {
		return location;
	}

	
}
