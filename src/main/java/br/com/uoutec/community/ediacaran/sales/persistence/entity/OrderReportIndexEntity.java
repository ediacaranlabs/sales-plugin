package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

@Entity
@Table(name="rw_order_report_index")
@EntityListeners(ShippingEntityListener.class)
public class OrderReportIndexEntity implements Serializable{

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@Column(name="cod_order_report", length=32)
	private String id;
	
	@Column(name="cod_order", length=38)
	private String order;

	@Column(name="cod_system_user", length=11)
	private Integer user;
	
	@Column(name="dsc_user_name", length=255)
	private String userName;
	
	@Column(name="cod_client")
	private Integer client;
	
	@Column(name="dsc_client_name", length=255)
	private String clientName;
	
	@Column(name="dat_created")
	private LocalDateTime date;

	@Column(name="set_status", length=32)
	@Enumerated(EnumType.STRING)
	private OrderReportStatus status;
	
	public OrderReportIndexEntity(){
	}
	
	public OrderReportIndexEntity(OrderReport e){
		this.order = e.getOrder();
		this.id = e.getId();
		this.date = e.getDate();
		this.user = e.getUser() == null? null : e.getUser().getId();
		
		this.client = e.getClient() == null? null : e.getClient().getId();
		if(e.getClient().getFirstName() != null) {
			this.clientName = this.clientName + " " + e.getClient().getFirstName();
		}
		if(e.getClient().getLastName() != null) {
			this.clientName = this.clientName + " " + e.getClient().getLastName();
		}
		this.clientName = StringUtil.toSearch(this.clientName);

		this.user = e.getUser() == null? null : e.getUser().getId();
		if(e.getUser().getFirstName() != null) {
			this.userName = this.userName + " " + e.getUser().getFirstName();
		}
		if(e.getUser().getLastName() != null) {
			this.userName = this.userName + " " + e.getUser().getLastName();
		}
		this.userName = StringUtil.toSearch(this.userName);
		
		this.status = e.getStatus();
		
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

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public OrderReportStatus getStatus() {
		return status;
	}

	public void setStatus(OrderReportStatus status) {
		this.status = status;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public OrderReport toEntity(){
		return this.toEntity(null);
	}
	
	public OrderReport toEntity(OrderReport e){
		
		if(e == null) {
			e =  new OrderReport();
		}
		
		e.setId(id);
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
		OrderReportIndexEntity other = (OrderReportIndexEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
