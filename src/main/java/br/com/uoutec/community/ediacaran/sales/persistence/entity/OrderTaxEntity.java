package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.entity.TaxType;

@Entity
@Table(name="rw_order_tax")
@EntityListeners(OrderTaxEntityListener.class)
public class OrderTaxEntity {

	@Id
	@Column(name="cod_order_discount")
	private String id; 
	
	@ManyToOne
	@JoinColumn(name="cod_order",referencedColumnName="cod_order")
	private OrderEntity orderEntity;
	
	@Column(name="dsc_name", length=128)
	private String name;
	
	@Column(name="dsc_description", length=256)
	private String description;
	
	@Column(name="vlr_value", scale=3, precision=12)
	private BigDecimal value;

	@Column(name="bit_discount")
	private Boolean discount;
	
	@Column(name="set_type", length=32)
	@Enumerated(EnumType.STRING)
	private TaxType type;
	
	@Column(name="vlr_order", length=1)
	private byte order;

	public OrderTaxEntity(){
	}
	
	public OrderTaxEntity(
			OrderEntity orderentity, Tax e){
		this.description = e.getDescription();
		this.name = e.getName();
		this.order = e.getOrder();
		this.discount = e.isDiscount();
		this.orderEntity = orderentity;
		this.type = e.getType();
		this.value = e.getValue();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderEntity getOrderEntity() {
		return orderEntity;
	}

	public void setOrderEntity(OrderEntity orderEntity) {
		this.orderEntity = orderEntity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public TaxType getType() {
		return type;
	}

	public void setType(TaxType type) {
		this.type = type;
	}

	public byte getOrder() {
		return order;
	}

	public void setOrder(byte order) {
		this.order = order;
	}
	
	public Tax toEntity(){
		return this.toEntity(null);
	}
	
	public Tax toEntity(Tax e){
		
		if( e == null){
			e = new Tax();
		}
		
		e.setDescription(this.description);
		e.setId(this.id);
		e.setName(this.name);
		e.setOrder(this.order);
		e.setType(this.type);
		e.setValue(this.value);
		e.setDiscount(this.discount == null? false : this.discount.booleanValue());
		
		return e;
	}
	
}
