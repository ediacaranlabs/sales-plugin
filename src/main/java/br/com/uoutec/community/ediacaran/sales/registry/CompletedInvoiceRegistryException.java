package br.com.uoutec.community.ediacaran.sales.registry;

public class CompletedInvoiceRegistryException extends InvoiceRegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public CompletedInvoiceRegistryException() {
		super();
	}

	public CompletedInvoiceRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompletedInvoiceRegistryException(String message) {
		super(message);
	}

	public CompletedInvoiceRegistryException(Throwable cause) {
		super(cause);
	}
	
}
