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
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

@Entity
@Table(name="rw_shipping")
@EntityListeners(ShippingEntityListener.class)
public class ShippingEntity implements Serializable{

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
	@Column(name="cod_shipping", length=32)
	private String id;
	
	@Column(name="cod_shipping_type")
	private String shippingType;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cod_order", referencedColumnName = "cod_order" )
	private OrderEntity order;

	@Column(name="cod_client",  updatable = false)
	private Integer client;
	
	@Column(name="dat_created")
	private LocalDateTime date;

	@Column(name="dat_cancellation")
	private LocalDateTime cancelDate;
	
	@Lob
	@Column(name="dsc_cancellation")
	private String cancelJustification;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cod_origin_address", referencedColumnName = "cod_address" )
	private AddressEntity origin;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cod_dest_address", referencedColumnName = "cod_address" )
	private AddressEntity dest;

	@Column(name="dat_received")
	private LocalDateTime receivedDate;

	@Column(name="bit_closed")
	private Boolean closed;
	
	@Column(name="vlr_weight")
	private Float weight;
	
	@Column(name="vlr_height")
	private Float height;
	
	@Column(name="vlr_width")
	private Float width;
	
	@Column(name="vlr_depth")
	private Float depth;
	
	@OneToMany(mappedBy = "shipping")
	private List<ProductRequestShippingEntity> products;
	
	@Lob
	private String addData;

	public ShippingEntity(){
	}
	
	public ShippingEntity(Shipping e){
		
		if(e.getOrder() != null) {
			this.order = new OrderEntity();
			this.order.setId(e.getOrder());
		}
		
		this.dest = e.getDest() == null? null : new AddressEntity(e.getDest(), null);
		this.id = e.getId();
		this.origin = e.getOrigin() == null? null : new AddressEntity(e.getOrigin(), null);
		this.shippingType = e.getShippingType();
		//this.addData = DataUtil.encode(e.getAddData());
		this.date = e.getDate();
		this.cancelDate = e.getCancelDate();
		this.cancelJustification = e.getCancelJustification();
		this.client = e.getClient() == null? null : e.getClient().getId();
		
		this.depth = e.getDepth();
		this.height = e.getHeight();
		this.id = e.getId();
		this.weight = e.getWeight();
		this.width = e.getWidth();
		
		if(e.getProducts() != null) {
			this.products = new ArrayList<>();
			for(ProductRequest p: e.getProducts()) {
				this.products.add(new ProductRequestShippingEntity(this, p));
			}
		}
		
		this.receivedDate = e.getReceivedDate();
		this.closed = e.isClosed();
		
		Map<String,String> data = DataUtil.encode(e, excludeFields);

		//if(actualData == null){
		//	actualData = data;
		//}
		//else{
		//	actualData.putAll(data);
		//}
		
		this.addData = DataUtil.encode(data);
		
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

	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelJustification() {
		return cancelJustification;
	}

	public void setCancelJustification(String cancelJustification) {
		this.cancelJustification = cancelJustification;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public AddressEntity getOrigin() {
		return origin;
	}

	public void setOrigin(AddressEntity origin) {
		this.origin = origin;
	}

	public AddressEntity getDest() {
		return dest;
	}

	public void setDest(AddressEntity dest) {
		this.dest = dest;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public String getAddData() {
		return addData;
	}

	public void setAddData(String addData) {
		this.addData = addData;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getDepth() {
		return depth;
	}

	public void setDepth(Float depth) {
		this.depth = depth;
	}

	public List<ProductRequestShippingEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequestShippingEntity> products) {
		this.products = products;
	}

	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public Shipping toEntity(){
		return this.toEntity(null);
	}
	
	public Shipping toEntity(Shipping e){
		
		try{
			
			if(e == null) {
				if(this.shippingType == null) {
					e =  new Shipping();
				}
				else{
					EntityInheritanceManager entityInheritanceUtil = 
							EntityContextPlugin.getEntity(EntityInheritanceManager.class);
						
					e = entityInheritanceUtil.getInstance(Shipping.class, this.shippingType);
					
					if(e == null){
						e = new Shipping();
					}
				}
				
			}
			
			if(this.addData != null){
				Map<String,String> data = DataUtil.decode(this.addData);
				DataUtil.decode(data, e);
				e.setAddData(data);
			}
			
			e.setDest(this.dest == null? null : this.dest.toEntity());
			e.setId(this.id);
			e.setOrigin(this.origin == null? null : origin.toEntity());
			e.setShippingType(this.shippingType);
			e.setCancelDate(this.cancelDate);
			e.setCancelJustification(this.cancelJustification);
			e.setDate(this.date);
			
			if(this.client != null) {
				Client c = new Client();
				c.setId(this.client);
				e.setClient(c);
			}
			
			if(this.order != null) {
				e.setOrder(this.order.getId());
			}

			e.setReceivedDate(this.receivedDate);
			e.setClosed(this.closed == null? false : this.closed.booleanValue());
			
			e.setDepth(this.depth == null? 0f : this.depth.floatValue());
			e.setHeight(this.height == null? 0f : this.height.floatValue());
			e.setId(this.id);
			
			e.setWeight(this.weight == null? 0f : this.height.floatValue());
			e.setWidth(this.width == null? 0f : this.width.floatValue());

			if(this.products != null) {
				List<ProductRequest> list = new ArrayList<>();
				for(ProductRequestShippingEntity p: this.products) {
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
		ShippingEntity other = (ShippingEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
