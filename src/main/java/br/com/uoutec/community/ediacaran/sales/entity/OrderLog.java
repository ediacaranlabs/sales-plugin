package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class OrderLog implements Serializable{

	private static final long serialVersionUID = 2795809142976062486L;

	@Min(value = 0, groups = IdValidation.class)
	private int id;
	
	@Min(value = 0, groups = DataValidation.class)
	private int owner;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Length(max = 38, min = 10, groups = DataValidation.class)
	private String orderId;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;
	
	@NotNull(groups = DataValidation.class)
	@Length(max = 255, min = 1, groups = DataValidation.class)
	private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
