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
import javax.validation.constraints.Size;

import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class OrderReport implements Serializable {

	private static final long serialVersionUID = -8250427147520718020L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Size(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	private Order order;
	
	@NotNull(groups = DataValidation.class)
	private Client client;
	
	private SystemUser user;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	@NotNull(groups = DataValidation.class )
	private OrderReportStatus status;
	
	public OrderReport(){
	}

	public OrderReport(OrderReport e) {
		this.client = e.client;
		this.date = e.date;
		this.id = e.id;
		this.order = e.order;
		this.products = e.products;
		this.status = e.status;
		this.user = e.user;
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

	@Valid
	@NotNull(groups = DataValidation.class )
	private List<ProductRequestReport> products;
	
	private volatile boolean productsLoaded = false;
	
	public List<ProductRequestReport> getProducts() {
		
		if(this.id == null) {
			return products;
		}
		
		if(!productsLoaded) {
			try {
				OrderReportRegistry registry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
				this.products = registry.getProductRequest(id);
				this.productsLoaded = true;
			}
			catch(Throwable ex) {
				throw new IllegalStateException("products load fail", ex);
			}
		}
		
		return products;
	}

	public void setProducts(List<ProductRequestReport> products) {
		this.products = products;
		this.productsLoaded = products != null;
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
