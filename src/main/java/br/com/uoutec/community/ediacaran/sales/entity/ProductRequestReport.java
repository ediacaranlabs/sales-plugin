package br.com.uoutec.community.ediacaran.sales.entity;

public class ProductRequestReport extends ProductRequest {

	private static final long serialVersionUID = 6826735447143946102L;

	private ProductRequestReportCause cause;

	public ProductRequestReport() {
	}
	
	public ProductRequestReport(ProductRequestReport e) {
		super(e);
	}

	public ProductRequestReportCause getCause() {
		return cause;
	}

	public void setCause(ProductRequestReportCause cause) {
		this.cause = cause;
	}
	
}
