package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Discount implements Serializable {

	private static final long serialVersionUID = 7140999176118548327L;

	@NotNull
	private String id;
	
	@NotNull
	@Size(min=1, max=32)
	private String name;
	
	@NotNull
	@Size(min=1, max=128)
	private String description;
	
	@NotNull
	private BigDecimal value;
	
	@NotNull
	private DiscountType type;
	
	private byte order;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public DiscountType getType() {
		return type;
	}

	public void setType(DiscountType type) {
		this.type = type;
	}

	public byte getOrder() {
		return order;
	}

	public void setOrder(byte order) {
		this.order = order;
	}
	
}
