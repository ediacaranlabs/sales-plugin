package br.com.uoutec.community.ediacaran.sales.payment;

public class ExecuteRefundActionTaskResult {

	private RefundRequest refundRequest;
	
	private Throwable error;

	public ExecuteRefundActionTaskResult(RefundRequest refundRequest, Throwable error) {
		this.refundRequest = refundRequest;
		this.error = error;
	}

	public RefundRequest getRefundRequest() {
		return refundRequest;
	}

	public Throwable getError() {
		return error;
	}

	
}
