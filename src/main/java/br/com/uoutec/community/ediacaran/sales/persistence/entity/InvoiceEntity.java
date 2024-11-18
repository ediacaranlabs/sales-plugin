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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.user.entityaccess.jpa.entity.SystemUserEntity;


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

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cod_owner", referencedColumnName="cod_system_user")
	private SystemUserEntity owner;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cod_order", referencedColumnName="cod_order")
	private OrderEntity order;
	
	@Column(name="vlr_value", scale=3, precision=12)
	private BigDecimal value;

	@Column(name="vlr_discount", scale=3, precision=12)
	private BigDecimal discount;
	
	@Column(name="vlr_tax", scale=3, precision=12)
	private BigDecimal tax;

	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;
	
	@OneToMany(mappedBy="invoice", fetch=FetchType.LAZY)
	private List<ProductRequestEntity> itens;
	
	public InvoiceEntity(){
	}
	
	public InvoiceEntity(Invoice e){
		
		this.order = new OrderEntity();
		this.order.setId(e.getOrder());
		
		this.date = e.getDate();
		this.id = e.getId();
		
		if(e.getOwner() > 0) {
			this.owner = new SystemUserEntity();
			this.owner.setId(e.getOwner());
		}
		
		this.value = e.getSubtotal();
		this.discount = e.getDiscount();
		this.tax = e.getTax();
		this.total = e.getTotal();
		
		if(e.getItens() != null){
			this.itens = new ArrayList<ProductRequestEntity>();
			for(ProductRequest k: e.getItens()){
				itens.add(new ProductRequestEntity(this, k));
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

	public SystemUserEntity getOwner() {
		return owner;
	}

	public void setOwner(SystemUserEntity owner) {
		this.owner = owner;
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

	public List<ProductRequestEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductRequestEntity> itens) {
		this.itens = itens;
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

		if(this.owner != null) {
			e.setOwner(this.owner.getId());
		}
		
		if(this.itens != null){
			List<ProductRequest> l = new ArrayList<ProductRequest>();
			for(ProductRequestEntity k: this.itens){
				l.add(k.toEntity());
			}
			e.setItens(l);
		}
		
		return e;
	}
	
}
