package br.com.uoutec.community.ediacaran.sales.registry;

import br.com.uoutec.entity.registry.RegistryException;

public class InvoiceRegistryException extends RegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public InvoiceRegistryException() {
		super();
	}

	public InvoiceRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvoiceRegistryException(String message) {
		super(message);
	}

	public InvoiceRegistryException(Throwable cause) {
		super(cause);
	}

}
