package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;

public class PaymentRequest {

	private Order order;
	
	private Payment payment;
	
	private PaymentLocation location;
	
	private List<ProductRequest> itens;
	
	public PaymentRequest() {
	}
	
	public PaymentRequest(Order order) {
		this(order, order.getClient(), order.getPayment());
	}

	public PaymentRequest(Order order, Client client, Payment payment) {
		this(order, client, payment, order.getItens());
	}
	
	public PaymentRequest(Order order, Client client, Payment payment, List<ProductRequest> products) {
		
		this.location = new PaymentLocation();
		this.location.setCountry(client.getCountry());
		this.location.setCity(client.getCity());
		this.location.setRegion(client.getRegion());
		this.location.setZip(client.getZip());
		
		this.payment = payment;
		
		this.itens = new ArrayList<>();
		products.stream().forEach((e)->this.itens.add(new ProductRequest(e)));
		this.order = order;
		
	}
	
	public Payment getPayment() {
		return payment;
	}

	public PaymentLocation getLocation() {
		return location;
	}

	public List<ProductRequest> getItens() {
		return itens;
	}

	public Order getOrder() {
		return order;
	}
	
}
