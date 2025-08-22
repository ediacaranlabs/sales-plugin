package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

@Entity
@Table(name="rw_order_report_msg")
@EntityListeners(OrderReportMessageEntityListener.class)
public class OrderReportMessageEntity implements Serializable {

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@Column(name="cod_order_report_msg", length=38)
	private String id;
	
	@Column(name="cod_order_report")
	private String orderReportID;
	
	@Column(name="dat_created")
	private LocalDateTime date;

	@Column(name="dsc_message", length = 128)
	private String message;
	
	@Column(name="cod_system_user", length=11)
	private Integer user;
	
	public OrderReportMessageEntity(){
	}
	
	public OrderReportMessageEntity(OrderReportMessage e){
		this.id = e.getId();
		this.date = e.getDate();
		this.orderReportID = e.getOrderReport();
		this.message = e.getMessage();
		this.user = e.getUser() == null? null : e.getUser().getId();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getorderReportID() {
		return orderReportID;
	}

	public void setOrderReport(String orderReportID) {
		this.orderReportID = orderReportID;
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

	public OrderReportMessage toEntity(){
		return this.toEntity(null);
	}
	
	public OrderReportMessage toEntity(OrderReportMessage e){
		
		if(e == null) {
			e =  new OrderReportMessage();
		}
		
		e.setId(id);
		e.setDate(this.date);
		e.setMessage(this.message);
		e.setOrderReport(this.orderReportID);

		if(this.user != null) {
			SystemUser user = new SystemUser();
			user.setId(this.user);
			e.setUser(user);
		}
		
		return e;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderReportMessageEntity other = (OrderReportMessageEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
