package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.CurrencyUtil;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ItensCollection;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

@SessionScoped
public class Cart implements Serializable{

	private static final long serialVersionUID = 7487563886270371708L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = CommonValidation.UUID, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	private ItensCollection itens;

	@NotNull(groups = DataValidation.class)
	private List<Tax> taxes;
	
	private Address billingAddress;
	
	private Address shippingAddress;
	
	private Client client;
	
	public Cart(){
		this.id       = UUID.randomUUID().toString();
		this.itens    = new ItensCollection();
		this.taxes    = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductRequest get(String id){
		return this.itens.get(id);
	}

	ItensCollection getItensCollection(){
		return this.itens;
	}
	
	void setItens(ItensCollection itens) {
		this.itens = itens;
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public boolean isNoitems(){
		return this.itens.isNoitems();
	}
	
	public void clear(){
		this.id = UUID.randomUUID().toString();
		this.itens.clear();
		this.taxes.clear();
	}
	
	public Collection<ProductRequest> getItens() {
		return itens.getItens();
	}

	public int getSize(){
		return this.itens.getSize();
	}
	
	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public String getSymbol() {
		String currency = getCurrency();
		return currency == null? null : CurrencyUtil.getSymbol(getCurrency());
	}
	
	public String getCurrency() {
		if(itens.isNoitems()) {
			return null;
		}
		
		for(ProductRequest pr: itens.getItens()) {
			if(pr.getCurrency() != null) {
				return pr.getCurrency();
			}
		}
		
		return null;
	}
	
	public void clearTaxes() {
		this.taxes = new ArrayList<>();
	}
	
	public BigDecimal getTax(){
		return BigDecimal.ZERO;
	}

	public BigDecimal getSubtotal(){
		BigDecimal value = BigDecimal.ZERO;
		
		if(this.itens == null || this.itens.getItens().isEmpty()){
			return value;
		}
		
		for(ProductRequest pr: this.itens.getItens()){
			value = value.add(pr.getSubtotal());
		}
		
		return value;
	}
	
	public String getDisplaySubtotal() {
		return CurrencyUtil.toString(getCurrency(), getSubtotal());
	}
	
	public BigDecimal getTotalDiscount() {
		
		BigDecimal value = this.getSubtotal();
		BigDecimal discount = BigDecimal.ZERO;
		
		for(ProductRequest pr: this.itens.getItens()){
			discount = discount.add(pr.getDiscount());
		}
		
		if(taxes != null) {
			
			List<Tax> tx = new ArrayList<>(taxes);
			
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

	public String getDisplayTotalDiscount() {
		return CurrencyUtil.toString(getCurrency(), getTotalDiscount());
	}
	
	public BigDecimal getTotalTax() {
		
		BigDecimal value = this.getSubtotal();
		BigDecimal tax = BigDecimal.ZERO;
		
		for(ProductRequest pr: this.itens.getItens()){
			tax = tax.add(pr.getTax());
		}
		
		if(taxes != null) {
			
			List<Tax> tx = new ArrayList<>(taxes);
			
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
	
	public String getDisplayTotalTax() {
		return CurrencyUtil.toString(getCurrency(), getTotalTax());
	}
	
	public BigDecimal getTotal(){
		BigDecimal value = BigDecimal.ZERO;
		
		if(this.itens == null || this.itens.getItens().isEmpty()){
			return value;
		}
		
		for(ProductRequest pr: this.itens.getItens()){
			value = value.add(pr.getTotal());
		}
		
		if(taxes != null) {
			
			List<Tax> tx = new ArrayList<>(taxes);
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
			}
			
		}
		
		return value;
	}

	public String getDisplayTotal() {
		return CurrencyUtil.toString(getCurrency(), getTotal());
	}
	
	public int getTotalItens(){
		return this.itens.getTotalItens();
	}

	public Order toOrder() {
		return toOrder(null);
	}
	
	public Order toOrder(PaymentGateway paymentGateway) {
		Address defaultAddress = getDefaultAddress(getClient());
		
		Order order = new Order();
		order.setDate(LocalDateTime.now());
		order.setCartID(getId());
		order.setStatus(OrderStatus.NEW);
		order.setId(null);
		order.setClient(getClient());
		order.setItens(new ArrayList<ProductRequest>(getItens()));
		order.setTaxes(getTaxes());
		order.setPaymentType(paymentGateway == null? null : paymentGateway.getId());
		order.setCurrency(order.getItens().get(0).getCurrency());
		order.setBillingAddress(getBillingAddress(getBillingAddress(), defaultAddress));
		order.setShippingAddress(getShippingAddress(getShippingAddress(), order.getBillingAddress(), defaultAddress, getBillingAddress() == getShippingAddress()));
		return order;
	}
	
	private Address getDefaultAddress(Client client) {
		Address address = new Address();
		
		address.setAddressLine1(client.getAddressLine1());
		address.setAddressLine2(client.getAddressLine2());
		address.setCity(client.getCity());
		address.setCountry(client.getCountry());
		address.setFirstName(client.getFirstName());
		address.setLastName(client.getLastName());
		address.setRegion(client.getRegion());
		address.setZip(client.getZip());
		
		return address;
	}
	
	private Address getAddress(Address value) {
		Address address = new Address();
		
		address.setAddressLine1(value.getAddressLine1());
		address.setAddressLine2(value.getAddressLine2());
		address.setCity(value.getCity());
		address.setCountry(value.getCountry());
		address.setFirstName(value.getFirstName());
		address.setLastName(value.getLastName());
		address.setRegion(value.getRegion());
		address.setZip(value.getZip());
		
		return address;
	}
	
	private Address getBillingAddress(Address billingAddress, Address defaultAddress) {
		if(billingAddress == null) {
			return defaultAddress;
		}
		else {
			return getAddress(billingAddress);
		}		
	}
	
	private Address getShippingAddress(Address shippingAddress, Address billingAddress, Address defaultAddress, boolean useBillingAddress) {
		if(shippingAddress == null) {
			return defaultAddress;
		}
		
		return useBillingAddress? billingAddress : getAddress(shippingAddress);
	}
	
}
