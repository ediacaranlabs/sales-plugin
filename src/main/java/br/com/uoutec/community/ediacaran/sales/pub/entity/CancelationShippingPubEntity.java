package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.groups.Default;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingCancelValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.IdValidation;

public class CancelationShippingPubEntity extends ShippingPubEntity {

	private static final long serialVersionUID = 1647504574319126033L;
	
	public CancelationShippingPubEntity() {
		super();
	}
	
	public CancelationShippingPubEntity(Shipping e, Locale locale) {
		super(e, locale);
	}
	
	protected void copyTo(Shipping o, boolean reload, boolean override, boolean validate) throws Throwable {
		if(o.getCancelJustification() == null) {
			o.setCancelJustification(super.getCancelJustification());
		}
	}
	
	protected void validate(boolean reload, boolean override) throws ValidationException{
		
		if(reload && override)
			validate(reload, override, Default.class, IdValidation.class, ShippingCancelValidation.class);
		else
		if(override)
			validate(reload, override, Default.class, ShippingCancelValidation.class);
		else
			validate(reload, override, Default.class, IdValidation.class);
		
	}
	
}
