package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.CurrencyUtil;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Order implements Serializable{

	private static final long serialVersionUID = -3268832345080113374L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Size(max = 38, min = 10, groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	private Client client;
	
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
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
		return CurrencyUtil.getSymbol(currency);
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

	public boolean isClosed() {
		return 
				status == OrderStatus.ARCHIVED || 
				status == OrderStatus.CANCELED || 
				status == OrderStatus.CLOSED || 
				status == OrderStatus.COMPLETE;
	}
	
	/* invoice methods */
	
	private volatile List<Invoice> invoices;
	
	private volatile boolean invoiceLoaded = false;
	
	public void setInvoices(List<Invoice> invoices) {
		if(invoices == null) {
			this.invoices = null;
			this.invoiceLoaded = false;
		}
		else {
			this.invoices = invoices;
			this.invoiceLoaded = true;
		}
	}
	
	public List<Invoice> getInvoices() throws InvoiceRegistryException {
		
		if(this.id == null) {
			return null;
		}
		
		if(!invoiceLoaded) {
			InvoiceRegistry registry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
			this.invoices = registry.findByOrder(this.id);
			this.invoiceLoaded = true;
		}
		
		return invoices;
	}
	
	/* Shipping methods */
	
	private volatile List<Shipping> shippings;
	
	private volatile boolean shippingsLoaded = false;
	
	public void setShippings(List<Shipping> value) {
		if(value == null) {
			this.shippings = null;
			this.shippingsLoaded = false;
		}
		else {
			this.shippings = value;
			this.shippingsLoaded = true;
		}
	}
	
	public List<Shipping> getShippings() throws ShippingRegistryException {
		
		if(this.id == null) {
			return null;
		}
		
		if(!shippingsLoaded) {
			ShippingRegistry registry = EntityContextPlugin.getEntity(ShippingRegistry.class);
			this.shippings = registry.findByOrder(this.id);
			this.shippingsLoaded = true;
		}
		
		return shippings;
	}

	/* Refund methods */
	
	private volatile List<Refund> refunds;
	
	private volatile boolean refundsLoaded = false;
	
	public void setRefunds(List<Refund> value) {
		if(value == null) {
			this.refunds = null;
			this.refundsLoaded = false;
		}
		else {
			this.refunds = value;
			this.refundsLoaded = true;
		}
	}
	
	public List<Refund> getRefunds() throws RefundRegistryException {
		
		if(this.id == null) {
			return null;
		}
		
		if(!refundsLoaded) {
			RefundRegistry registry = EntityContextPlugin.getEntity(RefundRegistry.class);
			this.refunds = registry.findRefundByOrder(this.id);
			this.refundsLoaded = true;
		}
		
		return refunds;
	}

	/* Order Report methods */
	
	private volatile List<OrderReport> orderReports;
	
	private volatile boolean orderReportsLoaded = false;
	
	public void setOrderReports(List<OrderReport> value) {
		if(value == null) {
			this.orderReports = null;
			this.orderReportsLoaded = false;
		}
		else {
			this.orderReports = value;
			this.orderReportsLoaded = true;
		}
	}
	
	public List<OrderReport> getOrderReport() throws OrderReportRegistryException {
		
		if(this.id == null) {
			return null;
		}
		
		if(!orderReportsLoaded) {
			OrderReportRegistry registry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
			this.orderReports = registry.findByOrder(this.id);
			this.orderReportsLoaded = true;
		}
		
		return orderReports;
	}
	
	public List<ProductResponse> getItensResponse() throws InvoiceRegistryException, ShippingRegistryException, RefundRegistryException, OrderReportRegistryException{
		List<ProductResponse> list = new ArrayList<>();
		
		if(itens != null) {
			for(ProductRequest i: itens) {
				list.add(
					new ProductResponse(
							i, 
							getInvoices().stream().collect(Collectors.toSet()), 
							getShippings().stream().collect(Collectors.toSet()), 
							getRefunds().stream().collect(Collectors.toSet()), 
							getOrderReport().stream().collect(Collectors.toSet())
					)
				);
			}
			
		}
		
		return list;
	}
	
	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public long getDaysAfterCreated() {
		return date == null? 0 : ChronoUnit.DAYS.between(date.toLocalDate(), LocalDate.now());
	}
	
	public BigDecimal getSubtotal(){
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: itens) {
			value = value.add(pr.getSubtotal());
		}
		return value;
	}

	public String getDisplaySubtotal() {
		return CurrencyUtil.toString(currency, getSubtotal());
	}
	
	public BigDecimal getDiscount() {
		
		BigDecimal value = getSubtotal();
		BigDecimal discount = BigDecimal.ZERO;
		
		for(ProductRequest pr: this.itens){
			discount = discount.add(pr.getDiscount());
		}
		
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

	public String getDisplayDiscount() {
		return CurrencyUtil.toString(currency, getDiscount());
	}
	
	public BigDecimal getTax() {
		
		BigDecimal value = getSubtotal();
		BigDecimal tax = BigDecimal.ZERO;
		
		for(ProductRequest pr: this.itens){
			tax = tax.add(pr.getTax());
		}
		
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
	
	public String getDisplayTax() {
		return CurrencyUtil.toString(currency, getTax());
	}
	
	public BigDecimal getTotal(){
		
		BigDecimal value = BigDecimal.ZERO;
		
		for(ProductRequest pr: itens) {
			value = value.add(pr.getTotal());
		}
		
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

	public String getDisplayTotal() {
		return CurrencyUtil.toString(currency, getTotal());
	}
	
}
