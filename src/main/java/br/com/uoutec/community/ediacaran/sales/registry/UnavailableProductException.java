package br.com.uoutec.community.ediacaran.sales.registry;

public class UnavailableProductException extends OrderRegistryException{

	private static final long serialVersionUID = 97925707815799367L;

	public UnavailableProductException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnavailableProductException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnavailableProductException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnavailableProductException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
