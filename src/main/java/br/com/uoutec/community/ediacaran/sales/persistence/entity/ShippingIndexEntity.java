package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

@Entity
@Table(name="rw_shipping_index")
@EntityListeners(ShippingEntityListener.class)
public class ShippingIndexEntity implements Serializable{

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@Column(name="cod_shipping", length=32)
	private String id;
	
	@Column(name="cod_shipping_type")
	private String shippingType;

	@Column(name="cod_order", length=38)
	private String order;

	@Column(name="cod_client",  updatable = false)
	private Integer client;
	
	@Column(name="dsc_client_name", length=255)
	private String clientName;
	
	@Column(name="dat_created")
	private LocalDateTime date;

	@Column(name="dat_cancellation")
	private LocalDateTime cancelDate;
	
	@Column(name = "dsc_dest_address", length=255)
	private String destAddress;

	public ShippingIndexEntity(){
	}
	
	public ShippingIndexEntity(Shipping e, Client client){
		this.order = e.getOrder();
		this.destAddress = e.getDest() == null? null : e.getDest().toString();
		this.destAddress = this.destAddress.length() > 255? this.destAddress.substring(0, 254) : this.destAddress;
		this.id = e.getId();
		this.shippingType = e.getShippingType();
		this.date = e.getDate();
		this.cancelDate = e.getCancelDate();
		this.client = e.getClient() == null? null : e.getClient().getId();
		
		if(client.getFirstName() != null) {
			this.clientName = this.clientName + " " + client.getFirstName();
		}
		
		if(client.getLastName() != null) {
			this.clientName = this.clientName + " " + client.getLastName();
		}
		
		this.clientName = StringUtil.toSearch(this.clientName);
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
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

	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getDestAddress() {
		return destAddress;
	}

	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}

	public Shipping toEntity(){
		return this.toEntity(null);
	}
	
	public Shipping toEntity(Shipping e){
		
		if(e == null) {
			e =  new Shipping();
		}
		
		Shipping s = new Shipping();
		
		s.setId(id);
		
		Client c = new Client();
		c.setId(this.client);
		e.setClient(c);
		
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
		ShippingIndexEntity other = (ShippingIndexEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
