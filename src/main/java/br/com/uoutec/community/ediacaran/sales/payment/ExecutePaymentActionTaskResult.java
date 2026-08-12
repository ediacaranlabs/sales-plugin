package br.com.uoutec.community.ediacaran.sales.payment;

public class ExecutePaymentActionTaskResult {

	private PaymentRequest paymentRequest;
	
	private Throwable error;

	public ExecutePaymentActionTaskResult(PaymentRequest paymentRequest, Throwable error) {
		this.paymentRequest = paymentRequest;
		this.error = error;
	}

	public PaymentRequest getPaymentRequest() {
		return paymentRequest;
	}

	public Throwable getError() {
		return error;
	}

	
}
