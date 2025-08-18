package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class OrderReport implements Serializable {

	private static final long serialVersionUID = -8250427147520718020L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	private Order order;
	
	@NotNull(groups = DataValidation.class)
	private Client client;
	
	private SystemUser user;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	@NotNull(groups = DataValidation.class )
	@Valid
	private List<ProductRequestReport> products;
	
	@NotNull(groups = DataValidation.class )
	private OrderReportStatus status;
	
	public OrderReport(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public SystemUser getUser() {
		return user;
	}

	public void setUser(SystemUser user) {
		this.user = user;
	}

	public List<ProductRequestReport> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequestReport> products) {
		this.products = products;
	}

	public OrderReportStatus getStatus() {
		return status;
	}

	public void setStatus(OrderReportStatus status) {
		this.status = status;
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
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return date.format(dateTimeFormatter);
	}

	public boolean isClosed() {
		return status == OrderReportStatus.CLOSED;
	}
	
	public long getDaysAfterCreated() {
		return date == null? 0 : ChronoUnit.DAYS.between(date.toLocalDate(), LocalDate.now());
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
		OrderReport other = (OrderReport) obj;
		return Objects.equals(id, other.id);
	}
	
}
