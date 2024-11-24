package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.user.entityaccess.jpa.entity.SystemUserEntity;

@Entity
@Table(name="rw_order")
@EntityListeners(OrderEntityListener.class)
public class OrderEntity implements Serializable{

	private static final long serialVersionUID = 8764002166887883414L;

	@Id
	@Column(name="cod_order", length=38)
	private String id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cod_owner", referencedColumnName="cod_system_user")
	private SystemUserEntity owner;

	@Column(name="dsc_cartid", length=128)
	private String cartID;
	
	@Column(name="dat_registry")
	private LocalDateTime date;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_status", length=28)
	private OrderStatus status;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_payment", referencedColumnName="cod_payment")
	private PaymentEntity payment;

	@Column(name="set_payment_type", length=32)
	private String paymentType;
	
	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)
	private List<ProductRequestEntity> itens;

	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)
	private List<InvoiceEntity> invoices;
	
	@OneToMany(mappedBy="orderEntity", fetch=FetchType.LAZY)
	private List<OrderTaxEntity> taxes;

	@Column(name="dsc_currency", length=3)
	private String currency;
	
	@Column(name="vlr_value", scale=3, precision=12)
	private BigDecimal value;

	@Column(name="vlr_discount", scale=3, precision=12)
	private BigDecimal discount;
	
	@Column(name="vlr_tax", scale=3, precision=12)
	private BigDecimal tax;

	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;
	
	@Column(name="bit_removed", length=1)
	private Boolean removed;

	public OrderEntity(){
	}
	
	public OrderEntity(Order e){
		this.date = e.getDate();
		this.cartID = e.getCartID();
		this.id = e.getId();
		
		if(e.getOwner() > 0) {
			this.owner = new SystemUserEntity();
			this.owner.setId(e.getOwner());
		}
		
		this.paymentType = e.getPaymentType();
		this.value = e.getSubtotal();
		this.discount = e.getDiscount();
		this.tax = e.getTax();
		this.total = e.getTotal();
		this.currency = e.getCurrency();
		
		if(e.getItens() != null){
			this.itens = new ArrayList<ProductRequestEntity>();
			for(ProductRequest p: e.getItens()){
				this.itens.add(new ProductRequestEntity(this,p));
			}
		}
		
		this.payment = e.getPayment() == null? null : new PaymentEntity(e.getPayment());
		this.removed = e.isRemoved();
		this.status = e.getStatus();
		
		List<Tax> discounts = e.getTaxes();
		
		if(discounts != null){
			this.taxes = new ArrayList<OrderTaxEntity>();
			for(Tax tax: discounts){
				this.taxes.add(new OrderTaxEntity(this, tax));
			}
		}

	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SystemUserEntity getOwner() {
		return owner;
	}

	public void setOwner(SystemUserEntity owner) {
		this.owner = owner;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public PaymentEntity getPayment() {
		return payment;
	}

	public void setPayment(PaymentEntity payment) {
		this.payment = payment;
	}

	public List<ProductRequestEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductRequestEntity> itens) {
		this.itens = itens;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}	

	public String getCartID() {
		return cartID;
	}

	public void setCartID(String cartID) {
		this.cartID = cartID;
	}

	public List<OrderTaxEntity> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<OrderTaxEntity> taxes) {
		this.taxes = taxes;
	}

	public List<InvoiceEntity> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<InvoiceEntity> invoices) {
		this.invoices = invoices;
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

	public Order toEntity(){
		return this.toEntity(null);
	}
	
	public Order toEntity(Order e){
		
		if(e == null){
			e = new Order();
		}
		
		e.setDate(this.date);
		e.setId(this.id);
		
		if(this.owner != null) {
			e.setOwner(this.owner.getId());
		}
		
		e.setPayment(this.payment == null? null : this.payment.toEntity());
		e.setRemoved(this.removed == null? false : this.removed);
		e.setStatus(this.status);
		e.setCartID(this.cartID);
		e.setPaymentType(this.paymentType);
		e.setCurrency(this.currency);
		
		if(this.itens != null){
			List<ProductRequest> l = new ArrayList<ProductRequest>();
			for(ProductRequestEntity k: this.itens){
				l.add(k.toEntity());
			}
			e.setItens(l);
		}

		if(this.invoices != null){
			List<Invoice> l = new ArrayList<Invoice>();
			for(InvoiceEntity k: this.invoices){
				l.add(k.toEntity());
			}
			e.setInvoice(l);
		}
		
		if(this.taxes != null){
			List<Tax> tax = new ArrayList<Tax>();
			e.setTaxes(tax);
			
			for(OrderTaxEntity prd: this.taxes){
				tax.add(prd.toEntity());
			}
			
		}

		return e;
	}
}
