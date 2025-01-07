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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Shipping implements Serializable{

	private static final long serialVersionUID = -8250427147520718020L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Length(min = 10, max = 38, groups = DataValidation.class)
	private String order;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	private LocalDateTime cancelDate;
	
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

	@NotNull(groups = DataValidation.class )
	@Valid
	private List<ProductRequest> products;
	
	private Map<String, String> addData;
	
	public Shipping(){
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

	public List<ProductRequest> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequest> products) {
		this.products = products;
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
				DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
		return date.format(dateTimeFormatter);
	}

	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
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
