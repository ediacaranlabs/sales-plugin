package br.com.uoutec.community.ediacaran.sales.registry;

public class CanceledInvoiceRegistryException extends InvoiceRegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public CanceledInvoiceRegistryException() {
		super();
	}

	public CanceledInvoiceRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanceledInvoiceRegistryException(String message) {
		super(message);
	}

	public CanceledInvoiceRegistryException(Throwable cause) {
		super(cause);
	}

}
