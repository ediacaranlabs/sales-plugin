package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.uoutec.community.ediacaran.sales.CurrencyUtil;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Refund implements Serializable {

	private static final long serialVersionUID = -8250427147520718020L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Size(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Size(min = 10, max = 38, groups = DataValidation.class)
	private String order;
	
	@NotNull(groups = DataValidation.class)
	private Client client;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	@NotNull(groups = DataValidation.class)
	private String refundType;
	
	private LocalDateTime refundDate;
	
	@NotNull(groups = DataValidation.class )
	@Valid
	private List<ProductRequest> products;
	
	private Map<String, String> addData;
	
	public Refund(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}

	public List<ProductRequest> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequest> products) {
		this.products = products;
	}

	public void set(String property, String value) {
		if(addData == null) {
			addData = new HashMap<>();
		}
		addData.put(property, value);
	}
	
	public String get(String property) {
		return addData == null? null : addData.get(property);
	}
	
	public String toStringDate(Locale locale) {
		if(date == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return date.format(dateTimeFormatter);
	}

	public String toStringRefundDate(Locale locale) {
		if(refundDate == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return refundDate.format(dateTimeFormatter);
	}
	
	public boolean isCompleted() {
		return refundDate != null;
	}
	
	public LocalDateTime getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(LocalDateTime refoundDate) {
		this.refundDate = refoundDate;
	}

	public BigDecimal getSubtotal(){
		
		if(products.isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: products) {
			value = value.add(pr.getSubtotal());
		}
		return value;
	}

	public String getDisplaySubtotal() {
		return CurrencyUtil.toString(products == null || products.isEmpty()? "" : products.get(0).getCurrency(), getSubtotal());
	}
	
	public BigDecimal getDiscount() {

		if(products.isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: products) {
			value = value.add(pr.getDiscount());
		}
		
		return value;
	}

	public String getDisplayDiscount() {
		return CurrencyUtil.toString(products == null || products.isEmpty()? "" : products.get(0).getCurrency(), getDiscount());
	}
	
	public BigDecimal getTax() {
		
		if(products.isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: products) {
			value = value.add(pr.getTax());
		}
		
		return value;
	}

	public String getDisplayTax() {
		return CurrencyUtil.toString(products == null || products.isEmpty()? "" : products.get(0).getCurrency(), getTax());
	}
	
	public BigDecimal getTotal(){
		
		if(products.isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		
		for(ProductRequest pr: products) {
			value = value.add(pr.getTotal());
		}
		
		return value;
	}

	public String getDisplayTotal() {
		return CurrencyUtil.toString(products == null || products.isEmpty()? "" : products.get(0).getCurrency(), getTotal());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Refund other = (Refund) obj;
		return Objects.equals(id, other.id);
	}
	
}
