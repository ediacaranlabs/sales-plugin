package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.Product;

public class ProductEditPubEntity extends ProductPubEntity {

	private static final long serialVersionUID = -5240855789107084675L;

	@Constructor
	public ProductEditPubEntity(){
		super();
	}

	public ProductEditPubEntity(Product e, Locale locale){
		super(e, locale);
	}

	@Override
	protected Class<?> getGenericType() {
		return ProductEditPubEntity.class;
	}
	
	@Override
	protected Product createNewInstance() throws Throwable {
		Product e = new Product();
		e.setProductType(getProductType());
		return e;
	}
	
	protected void copyTo(Product o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		String productType = o.getProductType();
		super.copyTo(o, reload, override, validate);
		o.setProductType(productType);
	}
	
}
