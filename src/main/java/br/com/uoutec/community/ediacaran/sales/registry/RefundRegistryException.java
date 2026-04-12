package br.com.uoutec.community.ediacaran.sales.registry;

import br.com.uoutec.entity.registry.RegistryException;

public class RefundRegistryException extends RegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public RefundRegistryException() {
		super();
	}

	public RefundRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RefundRegistryException(String message) {
		super(message);
	}

	public RefundRegistryException(Throwable cause) {
		super(cause);
	}

}
