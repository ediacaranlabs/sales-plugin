package br.com.uoutec.community.ediacaran.sales.registry;

public class EmptyInvoiceException extends InvoiceRegistryException{

	private static final long serialVersionUID = 6206943818218896488L;

	public EmptyInvoiceException() {
		super();
	}

	public EmptyInvoiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyInvoiceException(String message) {
		super(message);
	}

	public EmptyInvoiceException(Throwable cause) {
		super(cause);
	}

}
