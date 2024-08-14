package br.com.uoutec.community.ediacaran.sales.pub.entity;

import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;

public class PaymentPubEntity 
	extends GenericPubEntity<Payment>{

	private static final long serialVersionUID = 2237071113104066230L;
	
	private String paymentType;
	
	public PaymentPubEntity(){
	}
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	protected boolean isEqualId(Payment instance) throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean hasId(Payment instance) throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Payment reloadEntity() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Payment createNewInstance() throws Throwable {
		return new Payment();
	}

	@Override
	protected void copyTo(Payment o, boolean reload, boolean override,
			boolean validate) throws Throwable {
	}

	@Override
	protected Class<?> getGenericType() {
		return PaymentPubEntity.class;
	}

	@Override
	protected void loadProperties(GenericPubEntity<Payment> e) {
		PaymentPubEntity u = (PaymentPubEntity)e;
		this.paymentType = u.paymentType;
	}

	@Override
	protected String getCodeType() {
		return getPaymentType();
	}
	
}
