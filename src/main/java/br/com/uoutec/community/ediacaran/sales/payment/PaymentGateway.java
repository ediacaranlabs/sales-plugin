package br.com.uoutec.community.ediacaran.sales.payment;

public interface PaymentGateway {

	public static final String TYPE_NAME = "PAYMENT_GATEWAY";
	
	void payment(PaymentRequest paymentRequest) throws PaymentGatewayException;
	
	String redirectView(PaymentRequest paymentRequest) throws PaymentGatewayException;
	
	String getOwnerView(PaymentRequest paymentRequest) throws PaymentGatewayException;
	
	String getView() throws PaymentGatewayException;

	String getId();
	
	String getName();
	
	boolean isApplicable(PaymentRequest paymentRequest);
	
}
