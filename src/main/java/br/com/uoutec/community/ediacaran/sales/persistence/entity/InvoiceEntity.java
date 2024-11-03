package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.TaxType;


@Entity
@Table(name="rw_invoice")
@EntityListeners(InvoiceEntityListener.class)
public class InvoiceEntity implements Serializable{

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
	private TaxType taxType;
	
	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;

	public InvoiceEntity(){
	}
	
	public InvoiceEntity(Invoice e){
		this.taxType = e.getTaxType();
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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public TaxType getTaxType() {
		return taxType;
	}

	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	public Invoice toEntity(){
		return this.toEntity(null);
	}
	
	public Invoice toEntity(Invoice e){
		
		if(e == null){
			e = new Invoice();
		}
		
		e.setTaxType(this.taxType);
		e.setDate(this.date);
		e.setDiscount(this.discount);
		e.setId(this.id);
		e.setTotal(this.total);
		e.setValue(this.value);
		
		return e;
	}
	
}
