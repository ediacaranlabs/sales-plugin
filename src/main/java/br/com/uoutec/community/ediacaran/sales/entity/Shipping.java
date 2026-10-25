package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.system.SystemDate;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Shipping implements Serializable{

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

	@NotNull(groups = ShippingCancelValidation.class)
	private LocalDateTime cancelDate;

	@NotNull(groups = ShippingCancelValidation.class)
	@Pattern(regexp=CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Size(min = 5, max = 1024, groups = DataValidation.class)
	private String cancelJustification;
	
	@NotNull(groups = DataValidation.class)
	private String shippingType;

	@Valid
	@NotNull(groups = DataValidation.class)
	private Address origin;
	
	@Valid
	@NotNull(groups = DataValidation.class)
	private Address dest;
	
	@Min(value=0, groups = DataValidation.class )
	private float weight;
	
	@Min(value=0, groups = DataValidation.class )
	private float height;
	
	@Min(value=0, groups = DataValidation.class )
	private float width;
	
	@Min(value=0, groups = DataValidation.class )
	private float depth;

	private Map<String, String> addData;
	
	private LocalDateTime receivedDate;

	private boolean closed;
	
	public Shipping(){
	}

	public Shipping(Shipping e) {
		this.addData = e.addData;
		this.cancelDate = e.cancelDate;
		this.cancelJustification = e.cancelJustification;
		this.client = e.client;
		this.closed = e.closed;
		this.date = e.date;
		this.depth = e.depth;
		this.dest = e.dest;
		this.height = e.height;
		this.id = e.id;
		this.order = e.order;
		this.origin = e.origin;
		this.products = e.products;
		this.productsLoaded = e.productsLoaded;
		this.receivedDate = e.receivedDate;
		this.shippingType = e.shippingType;
		this.weight = e.weight;
		this.width = e.width;
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public Address getOrigin() {
		return origin;
	}

	public void setOrigin(Address origin) {
		this.origin = origin;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Address getDest() {
		return dest;
	}

	public void setDest(Address dest) {
		this.dest = dest;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	@Valid
	@NotNull(groups = DataValidation.class )
	private List<ProductRequest> products;
	
	private volatile boolean productsLoaded = false;
	
	public List<ProductRequest> getProducts() {
		
		if(this.id == null) {
			return products;
		}
		
		if(!productsLoaded) {
			try {
				ShippingRegistry registry = EntityContextPlugin.getEntity(ShippingRegistry.class);
				this.products = registry.getProductRequest(id);
				this.productsLoaded = true;
			}
			catch(Throwable ex) {
				throw new IllegalStateException("products load fail", ex);
			}
		}
		
		return products;
	}

	public void setProducts(List<ProductRequest> products) {
		this.products = products;
		this.productsLoaded = products != null;
	}
	
	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}

	public String getCancelJustification() {
		return cancelJustification;
	}

	public void setCancelJustification(String cancelJustification) {
		this.cancelJustification = cancelJustification;
	}

	public boolean isReceived() {
		return receivedDate != null;
	}
	
	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isCompleted() {
		return receivedDate != null && cancelDate == null;
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

	public String toStringCancelDate(Locale locale) {
		if(cancelDate == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return cancelDate.format(dateTimeFormatter);
	}

	public boolean isCanceled() {
		return cancelDate != null;
	}
	
	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String toStringReceivedDate(Locale locale) {
		if(receivedDate == null) {
			return "";
		}
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return receivedDate.format(dateTimeFormatter);
	}

	public long getDaysAfterCreated() {
		return date == null? 0 : ChronoUnit.DAYS.between(date.toLocalDate(), SystemDate.getSystemDate());
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
		Shipping other = (Shipping) obj;
		return Objects.equals(id, other.id);
	}
	
}
