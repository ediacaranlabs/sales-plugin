package br.com.uoutec.community.ediacaran.sales.pub.entity;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;

public class InvoicePanelPubEntity extends InvoicePubEntity{

	@Transient
	private static final long serialVersionUID = -8302935462723894018L;

	@Constructor
	public InvoicePanelPubEntity(){
	}
	
	@Override
	protected Invoice reloadEntity() throws Throwable {
		InvoiceRegistry invoiceRegistry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		return invoiceRegistry.findById(getId(), SystemUserRegistry.CURRENT_USER);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected Invoice createNewInstance() throws Throwable {
		return new Invoice();
	}

	@Override
	protected void copyTo(Invoice o, boolean reload, boolean override,
			boolean validate) throws Throwable {
	}
	
}
