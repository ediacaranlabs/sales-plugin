package br.com.uoutec.community.ediacaran.sales.registry;

public class ClientExistsRegistryException extends ClientRegistryException{

	private static final long serialVersionUID = 1363024273506336521L;

	public ClientExistsRegistryException() {
		super();
	}

	public ClientExistsRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientExistsRegistryException(String message) {
		super(message);
	}

	public ClientExistsRegistryException(Throwable cause) {
		super(cause);
	}

}
