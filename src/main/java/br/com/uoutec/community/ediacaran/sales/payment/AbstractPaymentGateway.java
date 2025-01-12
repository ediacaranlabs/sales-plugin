package br.com.uoutec.community.ediacaran.sales.payment;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;

public abstract class AbstractPaymentGateway implements PaymentGateway{

	private final String id;
	
	private final String name;
	
	public AbstractPaymentGateway(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void payment(PaymentRequest paymentRequest) throws PaymentGatewayException {
		
		if(paymentRequest.getPayment().getStatus() == PaymentStatus.NEW) {
			authorize(paymentRequest.getPayment(), paymentRequest.getLocation());
		}
		else
		if(paymentRequest.getPayment().getStatus() == PaymentStatus.PENDING_PAYMENT) {
			
			capture(paymentRequest.getPayment(), paymentRequest.getLocation());
			
			if(paymentRequest.getPayment().getStatus() == PaymentStatus.SUSPECTED_FRAUD) {
				suspectedFraud(paymentRequest.getPayment(), paymentRequest.getLocation());
			}
			
		}
		
	}

	protected void changePaymentStatus(Payment payment, PaymentStatus newStatus) throws PaymentGatewayException {
		
		if(!payment.getStatus().isValidNextStatus(newStatus)) {
			throw new PaymentGatewayException("invalid status: " + payment.getStatus() + " -> " + newStatus);
		}
	
		payment.setStatus(newStatus);
	}
	
	protected PaymentStatus getStartPaymentTransactionStatus() {
		return PaymentStatus.PENDING_PAYMENT;
	}

	protected PaymentStatus getEndPaymentTransactionStatus() {
		return PaymentStatus.PAYMENT_RECEIVED;
	}
	
	public void authorize(Payment payment, PaymentLocation location) throws PaymentGatewayException{
		changePaymentStatus(payment, getStartPaymentTransactionStatus());
	}
	
	public void capture(Payment payment, PaymentLocation location) throws PaymentGatewayException{
		changePaymentStatus(payment, getEndPaymentTransactionStatus());
	}

	public void suspectedFraud(Payment payment, PaymentLocation location) throws PaymentGatewayException{
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractPaymentGateway other = (AbstractPaymentGateway) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
