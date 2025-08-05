package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

@Entity
@Table(name="rw_invoice_index")
@EntityListeners(InvoiceEntityListener.class)
public class InvoiceIndexEntity implements Serializable{

	private static final long serialVersionUID = -8813652553986660245L;

	@Id
	@Column(name="cod_invoice", length=32)
	private String id;
	
	@Column(name="dat_date")
	private LocalDateTime date;

	@Column(name="cod_client")
	private Integer client;
	
	@Column(name="dsc_client_name", length=255)
	private String clientName;
	
	@Column(name="cod_order", length=32)
	private String order;

	@Column(name="dat_cancellation")
	private LocalDateTime cancelDate;
	
	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;
	
	public InvoiceIndexEntity(){
	}
	
	public InvoiceIndexEntity(Invoice e){
		this.order = e.getOrder();
		this.date = e.getDate();
		this.id = e.getId();
		this.cancelDate = e.getCancelDate();
		this.client = e.getClient() == null? null : e.getClient().getId();
		this.total = e.getTotal();
		
		if(e.getClient().getFirstName() != null) {
			this.clientName = this.clientName + " " + e.getClient().getFirstName();
		}
		
		if(e.getClient().getLastName() != null) {
			this.clientName = this.clientName + " " + e.getClient().getLastName();
		}
		
		this.clientName = StringUtil.toSearch(this.clientName);
		
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

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Invoice toEntity() {
		return toEntity(null);
	}
	
	public Invoice toEntity(Invoice e) {
		
		if(e == null) {
			e = new Invoice();
		}
		
		e.setId(id);
		
		return e;
	}
	
}
