package br.com.uoutec.community.ediacaran.sales.payment;

import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;

public interface PaymentGateway {

	public static final String TYPE_NAME = "PAYMENT_GATEWAY";
	
	void payment(PaymentRequest paymentRequest) throws PaymentGatewayException;

	PaymentStatus getPaymentStatus(PaymentRequest paymentRequest) throws PaymentGatewayException;
	
	void refund(RefundRequest refundRequest) throws PaymentGatewayException;
	
	String redirectView(PaymentRequest paymentRequest) throws PaymentGatewayException;
	
	String getOwnerView(PaymentRequest paymentRequest) throws PaymentGatewayException;
	
	String getView() throws PaymentGatewayException;

	String getId();
	
	String getName();
	
	boolean isApplicable(PaymentRequest paymentRequest);
	
}
