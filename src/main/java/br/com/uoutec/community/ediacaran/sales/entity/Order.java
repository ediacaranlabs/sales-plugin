package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Order implements Serializable{

	private static final long serialVersionUID = -3268832345080113374L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String id;

	@Min(value = 1, groups = DataValidation.class)
	private int owner;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.UUID)
	private String cartID;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;
	
	@NotNull(groups = DataValidation.class)
	private OrderStatus status;
	
	@Valid
	@NotNull(groups = DataValidation.class)
	private Payment payment;
	
	@Valid
	private Invoice invoice;
	
	@Valid
	private Shipping shipping;
	
	@Valid
	@Size(min = 1, groups = DataValidation.class)
	private List<ProductRequest> itens;

	private List<Discount> discounts;
	
	private boolean removed;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getCartID() {
		return cartID;
	}

	public void setCartID(String cartID) {
		this.cartID = cartID;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public List<ProductRequest> getItens() {
		return itens;
	}

	public void setItens(List<ProductRequest> itens) {
		this.itens = itens;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public List<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

	public BigDecimal getSubtotal(){
		return this.payment.getValue();
	}
	
	public BigDecimal getDiscount(){
		return this.payment.getDiscount();
	}
	
	public BigDecimal getTax(){
		return this.payment.getTax();
	}
	
	public BigDecimal getTotal(){
		return this.payment.getTotal();
	}
	
}
