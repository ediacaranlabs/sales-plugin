package br.com.uoutec.community.ediacaran.sales.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;

public class PaymentRequest {

	private BigDecimal value;
	
	private BigDecimal tax;

	private BigDecimal discount;

	private BigDecimal total;
	
	private String currency;
	
	private LocalDateTime receivedDate;
	
	private Map<String,String> addData;
	
	private Country country;

	private String city;
	
	private String region;
	
	private String zip;

	public PaymentRequest() {
	}
	
	public PaymentRequest(Client client, Payment payment) {
		this.country = client.getCountry();
		this.city = client.getCity();
		this.region = client.getRegion();
		this.zip = client.getZip();
		this.value = payment.getValue();
		this.tax = payment.getTax();
		this.discount = payment.getDiscount();
		this.total = payment.getTotal();
		this.currency = payment.getCurrency();
		this.addData = payment.getAddData();
	}

	public PaymentRequest(Client client, Cart cart) {
		this.country = client.getCountry();
		this.city = client.getCity();
		this.region = client.getRegion();
		this.zip = client.getZip();
		this.value = cart.getSubtotal();
		this.tax = cart.getTotalTax();
		this.discount = cart.getTotalDiscount();
		this.total = cart.getTotal();
		this.currency = cart.getCurrency();
		this.addData = new HashMap<>();
	}
	
	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}
	
}
