package br.com.uoutec.community.ediacaran.sales.registry;

import br.com.uoutec.entity.registry.RegistryException;

public class ClientRegistryException extends RegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public ClientRegistryException() {
		super();
	}

	public ClientRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientRegistryException(String message) {
		super(message);
	}

	public ClientRegistryException(Throwable cause) {
		super(cause);
	}

}
