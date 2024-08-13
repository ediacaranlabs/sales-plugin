package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.DiscountType;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;


@Entity
@Table(name="rw_invoice")
public class InvoiceHibernateEntity implements Serializable{

	private static final long serialVersionUID = -8813652553986660245L;

	@Id
	@Column(name="cod_invoice", length=32)
	private String id;
	
	@Column(name="dat_date")
	private LocalDateTime date;

	@Column(name="vlr_value", scale=3, precision=12)
	private BigDecimal value;
	
	@Column(name="vlr_discount", scale=3, precision=12)
	private BigDecimal discount;

	@Column(name="set_discount_type", length=32)
	@Enumerated(EnumType.STRING)
	private DiscountType discountType;
	
	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;

	public InvoiceHibernateEntity(){
	}
	
	public InvoiceHibernateEntity(Invoice e){
		this.discountType = e.getDiscountType();
		this.date = e.getDate();
		this.discount = e.getDiscount(); 
		this.id = e.getId();
		this.total = e.getTotal();
		this.value = e.getValue();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Invoice toEntity(){
		return this.toEntity(null);
	}
	
	public Invoice toEntity(Invoice e){
		
		if(e == null){
			e = new Invoice();
		}
		
		e.setDiscountType(this.discountType);
		e.setDate(this.date);
		e.setDiscount(this.discount);
		e.setId(this.id);
		e.setTotal(this.total);
		e.setValue(this.value);
		
		return e;
	}
	
}
