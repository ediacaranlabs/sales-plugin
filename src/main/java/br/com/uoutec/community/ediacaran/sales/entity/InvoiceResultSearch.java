package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class InvoiceResultSearch {

	private Invoice invoice;
	
	private SystemUser owner;

	public InvoiceResultSearch(Invoice invoice, SystemUser owner) {
		this.invoice = invoice;
		this.owner = owner;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public SystemUser getOwner() {
		return owner;
	}

	public void setOwner(SystemUser owner) {
		this.owner = owner;
	}
	
}
