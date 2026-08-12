package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.concurrent.Callable;

public class ExecutePaymentActionTask implements Callable<ExecutePaymentActionTaskResult>{

	private PaymentRequest paymentRequest;
	
	private PaymentGateway paymentGateway;
	
	public ExecutePaymentActionTask(PaymentRequest paymentRequest, PaymentGateway paymentGateway) {
		this.paymentRequest = paymentRequest;
		this.paymentGateway = paymentGateway;
	}

	@Override
	public ExecutePaymentActionTaskResult call() throws Exception {
		try {
			paymentGateway.payment(paymentRequest);
			return new ExecutePaymentActionTaskResult(paymentRequest, null);
		}
		catch(Throwable ex) {
			return new ExecutePaymentActionTaskResult(paymentRequest, ex);
		}
	}
	
}