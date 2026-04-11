package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
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

import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Refound implements Serializable {

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

	private LocalDateTime refoundDate;
	
	@Valid
	@NotNull(groups = DataValidation.class)
	private Payment payment;

	@NotNull(groups = DataValidation.class )
	@Valid
	private List<ProductRequest> products;
	
	private Map<String, String> addData;
	
	public Refound(){
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

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
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

	public String toStringRefoundDate(Locale locale) {
		if(refoundDate == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return refoundDate.format(dateTimeFormatter);
	}
	
	public LocalDateTime getRefoundDate() {
		return refoundDate;
	}

	public void setRefoundDate(LocalDateTime refoundDate) {
		this.refoundDate = refoundDate;
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
		Refound other = (Refound) obj;
		return Objects.equals(id, other.id);
	}
	
}
