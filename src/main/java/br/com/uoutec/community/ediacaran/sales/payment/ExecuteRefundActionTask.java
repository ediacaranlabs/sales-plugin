package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.concurrent.Callable;

public class ExecuteRefundActionTask implements Callable<ExecuteRefundActionTaskResult>{

	private RefundRequest refundRequest;
	
	private PaymentGateway paymentGateway;
	
	public ExecuteRefundActionTask(RefundRequest refundRequest, PaymentGateway paymentGateway) {
		this.refundRequest = refundRequest;
		this.paymentGateway = paymentGateway;
	}

	@Override
	public ExecuteRefundActionTaskResult call() throws Exception {
		try {
			paymentGateway.refund(refundRequest);
			return new ExecuteRefundActionTaskResult(refundRequest, null);
		}
		catch(Throwable ex) {
			return new ExecuteRefundActionTaskResult(refundRequest, ex);
		}
	}
	
}