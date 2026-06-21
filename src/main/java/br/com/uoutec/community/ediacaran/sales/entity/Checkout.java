package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;

public class Checkout implements Serializable {

	private static final long serialVersionUID = -4391243641915806431L;

	@NotNull
	@Valid
	private Order order;
	
	@NotNull
	private PaymentGateway paymentGateway;

	public Checkout(){
	}
	
	public Checkout(Order order, PaymentGateway paymentGateway) {
		this.order = order;
		this.paymentGateway = paymentGateway;
	}

	public Order getOrder() {
		return order;
	}

	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}
	
	public String getPaymentResource() throws PaymentGatewayException {
		return paymentGateway.redirectView(new PaymentRequest(order));
	}
}
