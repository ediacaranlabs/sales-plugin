package br.com.uoutec.community.ediacaran.sales.registry;

import br.com.uoutec.entity.registry.RegistryException;

public class ShippingRegistryException extends RegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public ShippingRegistryException() {
		super();
	}

	public ShippingRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShippingRegistryException(String message) {
		super(message);
	}

	public ShippingRegistryException(Throwable cause) {
		super(cause);
	}

}
