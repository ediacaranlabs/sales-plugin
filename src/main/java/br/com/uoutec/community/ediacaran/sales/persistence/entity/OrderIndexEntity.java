package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

@Entity
@Table(name="rw_order_index")
@EntityListeners(OrderEntityListener.class)
public class OrderIndexEntity implements Serializable {

	private static final long serialVersionUID = 8764002166887883414L;

	@Id
	@Column(name="cod_order", length=38)
	private String id;

	@Column(name="dsc_client")
	private Integer client;
	
	@Column(name="dsc_client_name", length=255)
	private String clientName;
	
	@Column(name="dsc_cartid", length=128)
	private String cartID;
	
	@Column(name="dat_registry")
	private LocalDateTime date;
	
	@Column(name="set_status", length=28)
	private String status;
	
	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;
	
	public OrderIndexEntity(){
	}
	
	public OrderIndexEntity(Order e){
		this.date = e.getDate();
		this.cartID = e.getCartID();
		this.id = e.getId();
		this.total = e.getTotal();
		this.status = e.getStatus() == null? null : e.getStatus().getCode();
		this.client = e.getClient().getId();
		this.clientName = "";
		
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

	public String getCartID() {
		return cartID;
	}

	public void setCartID(String cartID) {
		this.cartID = cartID;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Order toEntity() {
		return toEntity(null);
	}
	
	public Order toEntity(Order e) {
		
		if(e == null) {
			e = new Order();
		}
		
		Client client = new Client();
		client.setId(this.client);
		
		e.setClient(client);
		e.setId(this.id);
		
		return e;
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
		OrderIndexEntity other = (OrderIndexEntity) obj;
		return Objects.equals(id, other.id);
	}
	
}
