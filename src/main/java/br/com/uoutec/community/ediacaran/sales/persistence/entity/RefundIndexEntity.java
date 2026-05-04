package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

@Entity
@Table(name="rw_refund_index")
@EntityListeners(RefundIndexEntityListener.class)
public class RefundIndexEntity implements Serializable{

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@Column(name="cod_refund", length=32)
	private String id;
	
	@Column(name="cod_order", length=38)
	private String order;

	@Column(name="cod_client",  updatable = false)
	private Integer client;
	
	@Column(name="dsc_client_name", length=255)
	private String clientName;
	
	@Column(name="cod_refund_type")
	private String refundType;
	
	@Column(name="dat_created")
	private LocalDateTime date;

	@Column(name="dat_refund_date")
	private LocalDateTime refundDate;
	
	public RefundIndexEntity(){
	}
	
	public RefundIndexEntity(Refund e){
		this.order = e.getOrder();
		this.id = e.getId();
		this.date = e.getDate();
		this.refundDate = e.getRefundDate();
		this.refundType = e.getRefundType();
		
		this.client = e.getClient() == null? null : e.getClient().getId();
		
		if(e.getClient().getFirstName() != null) {
			this.clientName = this.clientName + " " + e.getClient().getFirstName();
		}
		
		if(e.getClient().getLastName() != null) {
			this.clientName = this.clientName + " " + e.getClient().getLastName();
		}
		
		this.clientName = StringUtil.toSearch(this.clientName);
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

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(LocalDateTime refundDate) {
		this.refundDate = refundDate;
	}

	public Refund toEntity(){
		return this.toEntity(null);
	}
	
	public Refund toEntity(Refund e){
		
		if(e == null) {
			e =  new Refund();
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
		RefundIndexEntity other = (RefundIndexEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
