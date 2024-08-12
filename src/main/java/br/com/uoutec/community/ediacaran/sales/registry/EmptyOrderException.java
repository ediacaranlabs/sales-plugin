package br.com.uoutec.community.ediacaran.sales.registry;

public class EmptyOrderException extends OrderRegistryException{

	private static final long serialVersionUID = 97925707815799367L;

	public EmptyOrderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmptyOrderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmptyOrderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmptyOrderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
