package br.com.uoutec.community.ediacaran.sales.registry;

public class EmptyShippingRegistryException extends ShippingRegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public EmptyShippingRegistryException() {
		super();
	}

	public EmptyShippingRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyShippingRegistryException(String message) {
		super(message);
	}

	public EmptyShippingRegistryException(Throwable cause) {
		super(cause);
	}

}
