package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;

public class RefundRequest {

	private Order order;
	
	private Payment payment;

	private Refund refund;
	
	private PaymentLocation location;
	
	private List<ProductRequest> itens;
	
	public RefundRequest() {
	}
	
	public RefundRequest(Order order, Refund refund) {
		this(order, order.getClient(), order.getPayment(), refund);
	}

	public RefundRequest(Order order, Client client, Payment payment, Refund refund) {
		this(order, client, payment, refund, order.getItens());
	}
	
	public RefundRequest(Order order, Client client, Payment payment, Refund refund, List<ProductRequest> products) {
		
		this.location = new PaymentLocation();
		this.location.setCountry(client.getCountry());
		this.location.setCity(client.getCity());
		this.location.setRegion(client.getRegion());
		this.location.setZip(client.getZip());
		
		this.payment = payment;
		this.refund = refund;
		this.itens = new ArrayList<>();
		products.stream().forEach((e)->this.itens.add(new ProductRequest(e)));
		this.order = order;
		
	}
	
	public Refund getRefund() {
		return refund;
	}

	public void setRefund(Refund refund) {
		this.refund = refund;
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
