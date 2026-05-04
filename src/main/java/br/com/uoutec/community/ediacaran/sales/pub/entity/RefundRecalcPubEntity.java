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
	public List<ProductRequestRecalcPubEntity> itens;
	
	private BigDecimal subtotal;

	private BigDecimal discounts;
	
	private BigDecimal taxes;
	
	private BigDecimal total;
	
	@Constructor
	public RefundRecalcPubEntity() {
	}
	
	public RefundRecalcPubEntity(Refund refund, Locale locale) {
		super(refund, locale);
	
		this.itens = new ArrayList<>();
		for(ProductRequest e: refund.getProducts()) {
			this.itens.add(new ProductRequestRecalcPubEntity(e, locale));
		}
		
		this.subtotal = refund.getSubtotal();
		this.discounts = refund.getDiscount();
		this.taxes = refund.getTax();
		this.total = refund.getTotal();
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

}
