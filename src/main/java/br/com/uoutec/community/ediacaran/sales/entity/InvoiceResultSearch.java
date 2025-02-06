package br.com.uoutec.community.ediacaran.sales.entity;

public class InvoiceResultSearch {

	private Invoice invoice;
	
	private Client owner;

	public InvoiceResultSearch(Invoice invoice, Client owner) {
		this.invoice = invoice;
		this.owner = owner;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Client getOwner() {
		return owner;
	}

	public void setOwner(Client owner) {
		this.owner = owner;
	}
	
}
