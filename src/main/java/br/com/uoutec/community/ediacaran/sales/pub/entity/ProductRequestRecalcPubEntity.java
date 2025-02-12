package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;

public class ProductRequestRecalcPubEntity
	extends ProductRequestPubEntity
	implements Serializable{

	private static final long serialVersionUID = -3865099353684532991L;

	protected String serial;
	
	private BigDecimal subtotal;

	private BigDecimal discounts;
	
	private BigDecimal taxes;
	
	private BigDecimal total;

	@Constructor
	public ProductRequestRecalcPubEntity() {
	}
	
	public ProductRequestRecalcPubEntity(ProductRequest e, Locale locale) {
		super(e, locale);
		this.subtotal = e.getSubtotal();
		this.discounts = e.getDiscount();
		this.taxes = e.getTax();
		this.total = e.getTotal();
		this.serial = e.getSerial();
	}

	@Transient
	public ProductPubEntity getProduct() {
		return super.getProduct();
	}

	@Transient
	public Map<String, String> getAddData() {
		return super.getAddData();
	}
	
	@Transient
	public String getId() {
		return super.getId();
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
