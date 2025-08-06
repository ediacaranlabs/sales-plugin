package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;

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

	@Column(name="cod_client", length = 11)
	private Integer client;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cod_order",  updatable = false, referencedColumnName="cod_order")
	private OrderEntity order;

	@Column(name="dat_cancellation")
	private LocalDateTime cancelDate;
	
	@Lob
	@Column(name="dsc_cancellation")
	private String cancelJustification;
	
	@Column(name="vlr_value", scale=3, precision=12)
	private BigDecimal value;

	@Column(name="vlr_discount", scale=3, precision=12)
	private BigDecimal discount;
	
	@Column(name="vlr_tax", scale=3, precision=12)
	private BigDecimal tax;

	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;
	
	@Column(name="dsc_currency", length=3)
	private String currency;
	
	@OneToMany(mappedBy="invoice", fetch=FetchType.LAZY)
	private List<ProductRequestInvoiceEntity> itens;

	@OneToMany(mappedBy="invoice", fetch=FetchType.LAZY)
	private List<InvoiceTaxEntity> taxes;
	
	public InvoiceEntity(){
	}
	
	public InvoiceEntity(Invoice e){
		
		this.order = new OrderEntity();
		this.order.setId(e.getOrder());
		
		this.date = e.getDate();
		this.id = e.getId();
		this.currency = e.getCurrency();
		this.cancelDate = e.getCancelDate();
		this.cancelJustification = e.getCancelJustification();
		
		if(e.getClient() != null) {
			this.client = e.getClient().getId();
		}
		
		this.value = e.getSubtotal();
		this.discount = e.getDiscount();
		this.tax = e.getTax();
		this.total = e.getTotal();
		
		if(e.getItens() != null){
			this.itens = new ArrayList<ProductRequestInvoiceEntity>();
			for(ProductRequest k: e.getItens()){
				itens.add(new ProductRequestInvoiceEntity(this, k));
			}
		}

		if(e.getTaxes() != null) {
			this.taxes = new ArrayList<>();
			for(Tax t: e.getTaxes()) {
				this.taxes.add(new InvoiceTaxEntity(t, this));
			}
		}
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

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<ProductRequestInvoiceEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductRequestInvoiceEntity> itens) {
		this.itens = itens;
	}

	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelJustification() {
		return cancelJustification;
	}

	public void setCancelJustification(String cancelJustification) {
		this.cancelJustification = cancelJustification;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<InvoiceTaxEntity> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<InvoiceTaxEntity> taxes) {
		this.taxes = taxes;
	}

	public Invoice toEntity(){
		return this.toEntity(null);
	}
	
	public Invoice toEntity(Invoice e){
		
		if(e == null){
			e = new Invoice();
		}
		
		e.setDate(this.date);
		e.setId(this.id);
		e.setOrder(this.order == null? null : this.order.getId());
		e.setCurrency(this.currency);
		e.setCancelDate(this.cancelDate);
		e.setCancelJustification(this.cancelJustification);
		
		if(this.client != null) {
			Client client = new Client();
			client.setId(this.client);
			e.setClient(client);
		}
		
		if(this.itens != null){
			List<ProductRequest> l = new ArrayList<ProductRequest>();
			for(ProductRequestInvoiceEntity k: this.itens){
				l.add(k.toEntity());
			}
			e.setItens(l);
		}
		
		if(this.taxes != null) {
			List<Tax> list = new ArrayList<>();
			e.setTaxes(list);
			for(InvoiceTaxEntity t: this.taxes) {
				list.add(t.toEntity());
			}
		}
		
		return e;
	}
	
}
