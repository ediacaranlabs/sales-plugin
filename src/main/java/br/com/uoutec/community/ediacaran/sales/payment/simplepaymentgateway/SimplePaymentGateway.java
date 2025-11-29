package br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway;

import java.math.BigDecimal;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.sales.payment.AbstractPaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;

public class SimplePaymentGateway extends AbstractPaymentGateway{

	public SimplePaymentGateway() {
		super("simple", "Simple Payment");
	}
	
	@Override
	public void payment(PaymentRequest paymentRequest) throws PaymentGatewayException {
		super.payment(paymentRequest);
	}

	@Override
	public String getView() throws PaymentGatewayException {
		return null;
	}

	@Override
	public String redirectView(PaymentRequest paymentRequest) throws PaymentGatewayException {
		return null;
	}

	@Override
	public String getOwnerView(PaymentRequest paymentRequest) throws PaymentGatewayException {
		return null;
	}
	
	@Override
	public boolean isApplicable(PaymentRequest paymentRequest) {
		return paymentRequest.getPayment().getTotal().compareTo(BigDecimal.ZERO) > 0;
	}

	@Override
	public Payment toPayment(Cart cart) {
		SimplePayment payment = new SimplePayment();
		payment.setStatus(PaymentStatus.NEW);
		payment.setPaymentType(getId());
		payment.setTax(cart.getTax());
		payment.setDiscount(cart.getTotalDiscount());
		payment.setCurrency(cart.getCurrency());
		payment.setValue(cart.getSubtotal());
		payment.setTotal(cart.getTotal());
		return payment;
	}
	
}
