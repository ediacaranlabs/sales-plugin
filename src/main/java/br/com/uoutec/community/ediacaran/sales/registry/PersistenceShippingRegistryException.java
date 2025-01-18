package br.com.uoutec.community.ediacaran.sales.registry;

public class PersistenceShippingRegistryException extends ShippingRegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public PersistenceShippingRegistryException() {
		super();
	}

	public PersistenceShippingRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceShippingRegistryException(String message) {
		super(message);
	}

	public PersistenceShippingRegistryException(Throwable cause) {
		super(cause);
	}

}
