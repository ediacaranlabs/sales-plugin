package br.com.uoutec.community.ediacaran.sales.payment;

import java.time.LocalDateTime;
import java.util.Map;

public class PaymentResponse {

	private PaymentRequest paymentRequest;
	
	private boolean pending;
	
	private LocalDateTime receivedDate;
	
	private Map<String,String> addData;
	
	public PaymentRequest getPaymentRequest() {
		return paymentRequest;
	}

	public void setPaymentRequest(PaymentRequest paymentRequest) {
		this.paymentRequest = paymentRequest;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}

	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}
	
}
