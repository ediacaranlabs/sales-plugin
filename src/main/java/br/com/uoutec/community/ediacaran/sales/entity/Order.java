package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

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

	@NotNull(groups = DataValidation.class)
	@Min(value = 1, groups = DataValidation.class)
	private int client;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.UUID)
	private String cartID;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;
	
	@NotNull(groups = DataValidation.class)
	private OrderStatus status;

	@Valid
	@NotNull(groups = DataValidation.class)
	private Address billingAddress;

	@Valid
	@NotNull(groups = DataValidation.class)
	private Address shippingAddress;
	
	@Valid
	@NotNull(groups = DataValidation.class)
	private Payment payment;

	@NotNull(groups = DataValidation.class)
	private String paymentType;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.CURRENCY)
	private String currency;
	
	private LocalDateTime completeInvoice;

	private LocalDateTime completeShipping;
	
	@Valid
	@Size(min = 1, groups = DataValidation.class)
	private List<ProductRequest> itens;

	private List<Tax> taxes;
	
	private boolean removed;
	
	public Order() {
		this.status = OrderStatus.NEW;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	public String toStringDate(Locale locale) {
		if(date == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
		return date.format(dateTimeFormatter);
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

	public LocalDateTime getCompleteInvoice() {
		return completeInvoice;
	}

	public void setCompleteInvoice(LocalDateTime completeInvoice) {
		this.completeInvoice = completeInvoice;
	}

	public String getSymbol() {
		return Currency.getInstance(currency).getSymbol();
	}
	
	/*
	public List<Invoice> getInvoice() {
		return invoices;
	}

	public void setInvoice(List<Invoice> invoices) {
		this.invoices = invoices;
	}
    */
	
	public LocalDateTime getCompleteShipping() {
		return completeShipping;
	}

	public void setCompleteShipping(LocalDateTime completeShipping) {
		this.completeShipping = completeShipping;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
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

	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getSubtotal(){
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: itens) {
			value = value.add(pr.getTotal());
		}
		return value;
	}

	public BigDecimal getDiscount() {
		
		BigDecimal value = getSubtotal();
		BigDecimal discount = BigDecimal.ZERO;
		
		if(taxes != null) {
			
			List<Tax> tx = taxes;
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
				if(t.isDiscount()) {
					discount = discount.add(taxUnit);
				}
			}
			
		}
		
		return discount;
	}

	public BigDecimal getTax() {
		
		BigDecimal value = getSubtotal();
		BigDecimal tax = BigDecimal.ZERO;
		
		if(taxes != null) {
			
			List<Tax> tx = taxes;
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
				if(!t.isDiscount()) {
					tax = tax.add(taxUnit);
				}
			}
			
		}
		
		return tax;
	}
	
	public BigDecimal getTotal(){
		
		BigDecimal value = getSubtotal();
		
		if(taxes != null) {
			
			List<Tax> tx = taxes;
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
			}
			
		}
		
		return value;
	}
	
}
