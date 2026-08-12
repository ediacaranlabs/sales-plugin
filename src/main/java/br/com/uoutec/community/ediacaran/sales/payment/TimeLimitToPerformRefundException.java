package br.com.uoutec.community.ediacaran.sales.payment;

public class TimeLimitToPerformRefundException extends PaymentGatewayException{

	private static final long serialVersionUID = -2654628320783145682L;

	public TimeLimitToPerformRefundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TimeLimitToPerformRefundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public TimeLimitToPerformRefundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TimeLimitToPerformRefundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TimeLimitToPerformRefundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
