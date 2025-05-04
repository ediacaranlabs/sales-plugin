package br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway;

import java.math.BigDecimal;

import br.com.uoutec.community.ediacaran.sales.payment.AbstractPaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;

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

}
