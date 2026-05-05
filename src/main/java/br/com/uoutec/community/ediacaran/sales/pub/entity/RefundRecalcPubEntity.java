package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;

public class RefundRecalcPubEntity
	extends RefundPubEntity
	implements Serializable{

	private static final long serialVersionUID = -3321824418533377419L;

	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductRequestRecalcPubEntity> itens;
	
	private BigDecimal subtotal;

	private BigDecimal discounts;
	
	private BigDecimal taxes;
	
	private BigDecimal total;
	
	private String displaySubtotal;

	private String displayDiscounts;
	
	private String displayTaxes;
	
	private String displayTotal;
	
	@Constructor
	public RefundRecalcPubEntity() {
	}
	
	public RefundRecalcPubEntity(Refund e, Locale locale) {
		super(e, locale);
	
		this.itens = new ArrayList<>();
		for(ProductRequest x: e.getProducts()) {
			this.itens.add(new ProductRequestRecalcPubEntity(x, locale));
		}
		
		this.subtotal = e.getSubtotal();
		this.discounts = e.getDiscount();
		this.taxes = e.getTax();
		this.total = e.getTotal();
		
		this.displaySubtotal = e.getDisplaySubtotal();
		this.displayDiscounts = e.getDisplayDiscount();
		this.displayTaxes = e.getDisplayTax();
		this.displayTotal = e.getDisplayTotal();
		
	}

	@Transient
	public List<ProductRequestRecalcPubEntity> getProductRequests() {
		return itens;
	}

	public void setProductRequests(List<ProductRequestRecalcPubEntity> itens) {
		this.itens = itens;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getDiscounts() {
		return discounts;
	}

	public void setDiscounts(BigDecimal discounts) {
		this.discounts = discounts;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getDisplaySubtotal() {
		return displaySubtotal;
	}

	public void setDisplaySubtotal(String displaySubtotal) {
		this.displaySubtotal = displaySubtotal;
	}

	public String getDisplayDiscounts() {
		return displayDiscounts;
	}

	public void setDisplayDiscounts(String displayDiscounts) {
		this.displayDiscounts = displayDiscounts;
	}

	public String getDisplayTaxes() {
		return displayTaxes;
	}

	public void setDisplayTaxes(String displayTaxes) {
		this.displayTaxes = displayTaxes;
	}

	public String getDisplayTotal() {
		return displayTotal;
	}

	public void setDisplayTotal(String displayTotal) {
		this.displayTotal = displayTotal;
	}

}
