package br.com.uoutec.community.ediacaran.sales.registry;

public class CompletedShippingRegistryException extends ShippingRegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public CompletedShippingRegistryException() {
		super();
	}

	public CompletedShippingRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompletedShippingRegistryException(String message) {
		super(message);
	}

	public CompletedShippingRegistryException(Throwable cause) {
		super(cause);
	}
	
}
