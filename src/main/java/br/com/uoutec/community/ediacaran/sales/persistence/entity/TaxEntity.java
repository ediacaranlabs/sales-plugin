package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.entity.TaxType;

@Entity
@Table(name = "rw_tax")
@EntityListeners(TaxEntityListener.class)
public class TaxEntity {

	@Id
	@Column(name = "cod_discount", length = 38)
	private String id;

	@Column(name = "dsc_name", length = 128)
	private String name;

	@Column(name = "dsc_description", length = 256)
	private String description;

	@Column(name = "vlr_value", scale = 3, precision = 12)
	private BigDecimal value;

	@Column(name = "set_type", length = 32)
	@Enumerated(EnumType.STRING)
	private TaxType type;

	@Column(name = "dsc_group", length = 32)
	private String group;
	
	@Column(name = "bit_discount")
	private Boolean discount;
	
	@Column(name = "vlr_order", length = 1)
	private byte order;

	public TaxEntity() {
	}

	public TaxEntity(Tax e) {
		this.description = e.getDescription();
		this.id = e.getId();
		this.name = e.getName();
		this.order = e.getOrder();
		this.type = e.getType();
		this.value = e.getValue();
		this.discount = e.isDiscount();
		this.group = e.getGroup();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Boolean getDiscount() {
		return discount;
	}

	public void setDiscount(Boolean discount) {
		this.discount = discount;
	}

	public Tax toEntity() {
		return this.toEntity(null);
	}

	public Tax toEntity(Tax e) {

		if (e == null) {
			e = new Tax();
		}

		e.setDescription(this.description);
		e.setId(this.id);
		e.setName(this.name);
		e.setOrder(this.order);
		e.setType(this.type);
		e.setValue(this.value);
		e.setDiscount(this.discount == null? false : this.discount.booleanValue());
		e.setGroup(this.group);
		
		return e;
	}

}
