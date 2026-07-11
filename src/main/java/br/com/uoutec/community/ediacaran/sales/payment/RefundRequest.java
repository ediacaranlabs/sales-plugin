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
	
	private boolean partialRefund;
	
	private boolean newRefund;
	
	public RefundRequest() {
	}
	
	public RefundRequest(Order order, Refund refund, boolean partialRefund) {
		this(order, order.getClient(), order.getPayment(), refund, partialRefund);
	}

	public RefundRequest(Order order, Refund refund, boolean partialRefund, boolean newRefund) {
		this(order, order.getClient(), order.getPayment(), refund, newRefund, partialRefund);
	}
	
	public RefundRequest(Order order, Client client, Payment payment, Refund refund, boolean partialRefund) {
		this(order, client, payment, refund, false, partialRefund, order.getItens());
	}
	
	public RefundRequest(Order order, Client client, Payment payment, Refund refund, boolean newRefund, boolean partialRefund) {
		this(order, client, payment, refund, partialRefund, newRefund, order.getItens());
	}
	
	public RefundRequest(Order order, Client client, Payment payment, Refund refund, boolean partialRefund, boolean newRefund, List<ProductRequest> products) {
		
		this.location = new PaymentLocation();
		this.location.setCountry(client.getCountry());
		this.location.setCity(client.getCity());
		this.location.setRegion(client.getRegion());
		this.location.setZip(client.getZip());
		this.partialRefund = partialRefund;
		this.payment = payment;
		this.refund = refund;
		this.newRefund = newRefund;
		this.itens = new ArrayList<>();
		products.stream().forEach((e)->this.itens.add(new ProductRequest(e)));
		this.order = order;
		
	}
	
	public boolean isNewRefund() {
		return newRefund;
	}

	public boolean isPartialRefund() {
		return partialRefund;
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
