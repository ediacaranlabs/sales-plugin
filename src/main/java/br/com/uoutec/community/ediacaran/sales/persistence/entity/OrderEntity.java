package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
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

import br.com.uoutec.community.ediacaran.sales.entity.Discount;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;

@Entity
@Table(name="rw_order")
@EntityListeners(OrderEntityListener.class)
public class OrderEntity implements Serializable{

	private static final long serialVersionUID = 8764002166887883414L;

	@Id
	@Column(name="cod_order", length=38)
	private String id;

	@Column(name="cod_owner", length=11)
	private Integer owner;

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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_invoice", referencedColumnName="cod_invoice")
	private InvoiceEntity invoice;

	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)
	private List<ProductRequestEntity> itens;

	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)
	private List<OrderDiscountEntity> discounts;

	@Column(name="bit_removed", length=1)
	private Boolean removed;

	public OrderEntity(){
	}
	
	public OrderEntity(Order e){
		this.date = e.getDate();
		this.cartID = e.getCartID();
		this.id = e.getId();
		this.owner = e.getOwner();
		this.invoice = e.getInvoice() == null? null : new InvoiceEntity(e.getInvoice());
		
		if(e.getItens() != null){
			this.itens = new ArrayList<ProductRequestEntity>();
			for(ProductRequest p: e.getItens()){
				this.itens.add(new ProductRequestEntity(this,p));
			}
		}
		
		this.payment = e.getPayment() == null? null : new PaymentEntity(e.getPayment());
		this.removed = e.isRemoved();
		this.status = e.getStatus();
		
		List<Discount> discounts = e.getDiscounts();
		
		if(discounts != null){
			this.discounts = new ArrayList<OrderDiscountEntity>();
			for(Discount discount: discounts){
				this.discounts.add(new OrderDiscountEntity(this, discount));
			}
		}

	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
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

	public InvoiceEntity getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceEntity invoice) {
		this.invoice = invoice;
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

	public List<OrderDiscountEntity> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<OrderDiscountEntity> discounts) {
		this.discounts = discounts;
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
		e.setOwner(this.owner);
		e.setInvoice(this.invoice == null? null : this.invoice.toEntity());
		e.setPayment(this.payment == null? null : this.payment.toEntity());
		e.setRemoved(this.removed == null? false : this.removed);
		e.setStatus(this.status);
		e.setCartID(this.cartID);
		
		if(this.itens != null){
			List<ProductRequest> l = new ArrayList<ProductRequest>();
			for(ProductRequestEntity k: this.itens){
				l.add(k.toEntity());
			}
			e.setItens(l);
		}
		
		if(this.discounts != null){
			List<Discount> discounts = new ArrayList<Discount>();
			e.setDiscounts(discounts);
			
			for(OrderDiscountEntity prd: this.discounts){
				discounts.add(prd.toEntity());
			}
			
		}

		return e;
	}
}
