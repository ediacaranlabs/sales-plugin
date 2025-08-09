package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;

@Entity
@Table(name="rw_order_report")
@EntityListeners(OrderReportEntityListener.class)
public class OrderReportEntity implements Serializable{

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@Column(name="cod_order_report", length=32)
	private String id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cod_order", referencedColumnName = "cod_order" )
	private OrderEntity order;

	@Column(name="cod_user")
	private Integer user;
	
	@Column(name="dat_created")
	private LocalDateTime date;

	@Column(name="set_status", length=32)
	@Enumerated(EnumType.STRING)
	private OrderReportStatus status;
	
	@OneToMany(mappedBy = "orderReport")
	private List<ProductRequestOrderReportEntity> products;
	
	public OrderReportEntity(){
	}
	
	public OrderReportEntity(OrderReport e){
		
		if(e.getOrder() != null) {
			this.order = new OrderEntity();
			this.order.setId(e.getOrder());
		}
		
		this.id = e.getId();
		this.user = e.getUser() == null? null : e.getUser().getId();
		this.status = e.getStatus();
		this.date = e.getDate();
		
		if(e.getProducts() != null) {
			this.products = new ArrayList<>();
			for(ProductRequest p: e.getProducts()) {
				this.products.add(new ProductRequestOrderReportEntity(this, p));
			}
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public OrderReportStatus getStatus() {
		return status;
	}

	public void setStatus(OrderReportStatus status) {
		this.status = status;
	}

	public List<ProductRequestOrderReportEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequestOrderReportEntity> products) {
		this.products = products;
	}

	public OrderReport toEntity(){
		return this.toEntity(null);
	}
	
	public OrderReport toEntity(OrderReport e){
		if(e == null) {
			e =  new OrderReport();
		}
		
		e.setId(this.id);
		e.setDate(this.date);
		
		if(this.user != null) {
			SystemUser c = new SystemUser();
			c.setId(this.user);
			e.setUser(c);
		}
		
		if(this.order != null) {
			e.setOrder(this.order.getId());
		}

		if(this.products != null) {
			List<ProductRequestReport> list = new ArrayList<>();
			for(ProductRequestOrderReportEntity p: this.products) {
				list.add(p.toEntity());
			}
			e.setProducts(list);
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
		OrderReportEntity other = (OrderReportEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
