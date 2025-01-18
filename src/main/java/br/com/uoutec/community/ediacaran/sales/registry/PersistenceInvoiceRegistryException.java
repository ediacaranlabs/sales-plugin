package br.com.uoutec.community.ediacaran.sales.registry;

public class PersistenceInvoiceRegistryException extends InvoiceRegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public PersistenceInvoiceRegistryException() {
		super();
	}

	public PersistenceInvoiceRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceInvoiceRegistryException(String message) {
		super(message);
	}

	public PersistenceInvoiceRegistryException(Throwable cause) {
		super(cause);
	}

}
