package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRecalcPubEntity implements Serializable{

	private static final long serialVersionUID = -3321824418533377419L;

	private static final DecimalFormat df = new DecimalFormat("###,###,##0.00"); 
	
	private String order;
	
	private List<ProductRequestRecalcPubEntity> itens;
	
	private String currency;

	private String subtotal;
	
	private String taxes;
	
	private String discounts;
	
	private String total;
	
	public InvoiceRecalcPubEntity(Invoice invoice) {
		this.order = invoice.getOrder();
		this.currency = invoice.getCurrency();
		this.subtotal = df.format(invoice.getSubtotal());
		this.taxes = df.format(invoice.getTax());
		this.discounts = df.format(invoice.getDiscount());
		this.total = df.format(invoice.getTotal());

		this.itens = new ArrayList<>();
		for(ProductRequest e: invoice.getItens()) {
			this.itens.add(new ProductRequestRecalcPubEntity(e));
		}
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<ProductRequestRecalcPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductRequestRecalcPubEntity> itens) {
		this.itens = itens;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

	public String getDiscounts() {
		return discounts;
	}

	public void setDiscounts(String discounts) {
		this.discounts = discounts;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
}
