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

import br.com.uoutec.community.ediacaran.sales.entity.Discount;
import br.com.uoutec.community.ediacaran.sales.entity.DiscountType;

@Entity
@Table(name = "rw_product_request_discount")
@EntityListeners(ProductRequestDiscountEntityListener.class)
public class ProductRequestDiscountEntity {

	@Id
	@Column(name="cod_product_request_discount")
	private String id;

	@ManyToOne
	@JoinColumn(name = "cod_product_request", referencedColumnName = "cod_product_request", insertable = false, updatable = false)
	private ProductRequestEntity productRequest;

	@Column(name = "dsc_name", length = 128)
	private String name;

	@Column(name = "dsc_description", length = 256)
	private String description;

	@Column(name = "vlr_value", scale = 3, precision = 12)
	private BigDecimal value;

	@Column(name = "set_type", length = 32)
	@Enumerated(EnumType.STRING)
	private DiscountType type;

	@Column(name = "vlr_order", length = 1)
	private byte order;

	public ProductRequestDiscountEntity() {
	}

	public ProductRequestDiscountEntity(
			ProductRequestEntity productRequest, Discount e) {
		this.description = e.getDescription();
		this.name = e.getName();
		this.order = e.getOrder();
		this.productRequest = productRequest;
		this.type = e.getType();
		this.value = e.getValue();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductRequestEntity getProductRequest() {
		return productRequest;
	}

	public void setProductRequest(ProductRequestEntity productRequest) {
		this.productRequest = productRequest;
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

	public DiscountType getType() {
		return type;
	}

	public void setType(DiscountType type) {
		this.type = type;
	}

	public byte getOrder() {
		return order;
	}

	public void setOrder(byte order) {
		this.order = order;
	}

	public Discount toEntity() {
		return this.toEntity(null);
	}

	public Discount toEntity(Discount e) {

		if (e == null) {
			e = new Discount();
		}

		e.setDescription(this.description);
		e.setId(this.id);
		e.setName(this.name);
		e.setOrder(this.order);
		e.setType(this.type);
		e.setValue(this.value);

		return e;
	}

}
