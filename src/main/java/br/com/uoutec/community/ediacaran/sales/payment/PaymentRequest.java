package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.ArrayList;
import java.util.List;

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
		this.location = new PaymentLocation();
		this.location.setCountry(order.getClient().getCountry());
		this.location.setCity(order.getClient().getCity());
		this.location.setRegion(order.getClient().getRegion());
		this.location.setZip(order.getClient().getZip());
		
		this.payment = order.getPayment();
		
		this.itens = new ArrayList<>();
		order.getItens().stream().forEach((e)->this.itens.add(new ProductRequest(e)));
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
