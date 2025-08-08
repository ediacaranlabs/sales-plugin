package br.com.uoutec.community.ediacaran.sales.registry;

import br.com.uoutec.entity.registry.RegistryException;

public class OrderReportRegistryException extends RegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public OrderReportRegistryException() {
		super();
	}

	public OrderReportRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderReportRegistryException(String message) {
		super(message);
	}

	public OrderReportRegistryException(Throwable cause) {
		super(cause);
	}

}
