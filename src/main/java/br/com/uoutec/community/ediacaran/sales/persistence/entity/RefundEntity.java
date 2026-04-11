package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.brandao.brutos.bean.BeanInstance;
import org.brandao.brutos.bean.BeanProperty;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

@Entity
@Table(name="rw_refound")
@EntityListeners(RefoundEntityListener.class)
public class RefundEntity implements Serializable {

	private static transient final long serialVersionUID = -5167928569154696530L;

	private static transient final Set<String> excludeFields;
	
	static{
		excludeFields = new HashSet<String>();
		BeanInstance i = new BeanInstance(null, Shipping.class);
		
		List<BeanProperty> list = i.getProperties();
		
		for(BeanProperty p: list){
			excludeFields.add(p.getName());
		}
	}
	
	@Id
	@Column(name="cod_refund", length=32)
	private String id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cod_order", referencedColumnName = "cod_order" )
	private OrderEntity order;

	@Column(name="cod_client",  updatable = false)
	private Integer client;

	@Column(name="cod_refound_type")
	private String refundType;
	
	@Column(name="dat_created")
	private LocalDateTime date;

	@Column(name="dat_refound")
	private LocalDateTime refundDate;
	
	@OneToMany(mappedBy = "refound")
	private List<ProductRequestRefundEntity> products;
	
	@Lob
	private String addData;

	public RefundEntity(){
	}
	
	public RefundEntity(Refund e){
		
		if(e.getOrder() != null) {
			this.order = new OrderEntity();
			this.order.setId(e.getOrder());
		}
		
		this.id = e.getId();
		this.date = e.getDate();
		this.refundDate = e.getRefundDate();
		this.client = e.getClient() == null? null : e.getClient().getId();
		this.refundType = e.getRefundType();
		if(e.getProducts() != null) {
			this.products = new ArrayList<>();
			for(ProductRequest p: e.getProducts()) {
				this.products.add(new ProductRequestRefundEntity(this, p));
			}
		}
		
		Map<String,String> data = DataUtil.encode(e, excludeFields);

		this.addData = DataUtil.encode(data);
		
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

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
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

	public List<ProductRequestRefundEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequestRefundEntity> products) {
		this.products = products;
	}

	public Refund toEntity(){
		return this.toEntity(null);
	}
	
	public Refund toEntity(Refund e){
		
		try{
			
			if(e == null) {
				if(this.refundType == null) {
					e =  new Refund();
				}
				else{
					EntityInheritanceManager entityInheritanceUtil = 
							EntityContextPlugin.getEntity(EntityInheritanceManager.class);
						
					e = entityInheritanceUtil.getInstance(Refund.class, this.refundType);
					
					if(e == null){
						e = new Refund();
					}
				}
				
			}
			
			if(this.addData != null){
				Map<String,String> data = DataUtil.decode(this.addData);
				DataUtil.decode(data, e);
				e.setAddData(data);
			}
			
			e.setId(this.id);
			e.setRefundDate(this.refundDate);
			e.setRefundType(this.refundType);
			e.setDate(this.date);
			
			if(this.client != null) {
				Client c = new Client();
				c.setId(this.client);
				e.setClient(c);
			}
			
			if(this.order != null) {
				e.setOrder(this.order.getId());
			}

			if(this.products != null) {
				List<ProductRequest> list = new ArrayList<>();
				for(ProductRequestRefundEntity p: this.products) {
					list.add(p.toEntity());
				}
				e.setProducts(list);
			}
			
			return e;
		}
		catch(Throwable ex){
			throw new RuntimeException(ex);
		}
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
		RefundEntity other = (RefundEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
