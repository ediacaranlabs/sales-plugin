package br.com.uoutec.community.ediacaran.sales.entity;

import javax.validation.constraints.NotNull;

import br.com.uoutec.entity.registry.DataValidation;

public class ProductRequestReport extends ProductRequest {

	private static final long serialVersionUID = 6826735447143946102L;

	@NotNull(groups = DataValidation.class )
	private ProductRequestReportCause cause;

	private String orderReport;
	
	public ProductRequestReport() {
	}
	
	public ProductRequestReport(ProductRequest e) {
		super(e);
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

	public String getOrderReport() {
		return orderReport;
	}

	public void setOrderReport(String orderReport) {
		this.orderReport = orderReport;
	}
	
}
