package br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.sales.payment.AbstractPaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;
import br.com.uoutec.community.ediacaran.sales.payment.RefundRequest;

public class SimplePaymentGateway extends AbstractPaymentGateway {

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
	public void createRefund(RefundRequest refundRequest) throws PaymentGatewayException {
		refundRequest.getPayment().setStatus(PaymentStatus.REFOUND);
	}

	@Override
	public void createPartialRefund(RefundRequest refundRequest) throws PaymentGatewayException {
		refundRequest.getPayment().setStatus(PaymentStatus.PARTIAL_REFUND);
	}
	
	@Override
	public void confirmRefund(RefundRequest refundRequest) throws PaymentGatewayException {
		refundRequest.getRefund().setRefundDate(LocalDateTime.now());
	}
	
}
