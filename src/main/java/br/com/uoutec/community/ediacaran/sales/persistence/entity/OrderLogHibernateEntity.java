package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;

@Entity
@Table(name="rw_order_log")
public class OrderLogHibernateEntity implements Serializable{

	private static final long serialVersionUID = 8764002166887883414L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_order_log", length=11)
	private Integer id;

	@Column(name="cod_order", length=32)
	private String orderId;
	
	@Column(name="cod_owner", length=11)
	private Integer owner;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dat_registry")
	private LocalDateTime date;
	
	@Column(name="dsc_message", length=300)
	private String message;

	public OrderLogHibernateEntity(){
	}
	
	public OrderLogHibernateEntity(OrderLog e){
		this.date = e.getDate();
		this.id   = e.getId();
		this.message = e.getMessage();
		this.orderId = e.getOrderId();
		this.owner = e.getOwner();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
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

	public OrderLog toEntity(){
		return this.toEntity(null);
	}
	
	public OrderLog toEntity(OrderLog e){
		
		if(e == null){
			e = new OrderLog();
		}
		
		e.setDate(this.date);
		e.setId(this.id);
		e.setMessage(this.message);
		e.setOrderId(this.orderId);
		e.setOwner(this.owner);
		
		return e;
	}
}
