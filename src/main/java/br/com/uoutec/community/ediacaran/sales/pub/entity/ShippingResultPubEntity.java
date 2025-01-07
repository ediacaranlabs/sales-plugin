package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;

public class ShippingResultPubEntity extends ShippingPubEntity {
	
	private static final long serialVersionUID = 8112064051350456421L;

	@Constructor
	public ShippingResultPubEntity() {
	}
	
	public ShippingResultPubEntity(Shipping e, Locale locale) {
		super(e, locale);
	}
	
}
