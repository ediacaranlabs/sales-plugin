package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReportCause;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ProductRequestReportPubEntity extends ProductRequestPubEntity {

	private static final long serialVersionUID = 7608872251741536893L;

	private ProductRequestReportCause cause;
	
	@Constructor
	public ProductRequestReportPubEntity(){
	}
	
	public ProductRequestReportPubEntity(ProductRequestReport e, Locale locale){
		super(e, locale);
		this.cause = e.getCause(); 
	}

	public ProductRequestReportCause getCause() {
		return cause;
	}

	public void setCause(ProductRequestReportCause cause) {
		this.cause = cause;
	}
	
	@Override
	protected ProductRequest reloadEntity()	throws Throwable {
		OrderReportRegistry registry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		OrderReport or = new OrderReport();
		or.setId(super.getId());
		
		return registry.getProductRequestReport(this.getId(), or);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ProductRequest createNewInstance() throws Throwable {
		return new ProductRequestReport();
	}

	@Override
	protected void copyTo(ProductRequest o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		super.copyTo(o, reload, override, validate);
		
		ProductRequestReport e = (ProductRequestReport)o;
		e.setCause(this.cause);
	}
	
}
