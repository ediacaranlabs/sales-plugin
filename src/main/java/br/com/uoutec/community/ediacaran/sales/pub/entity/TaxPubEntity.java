package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Constructor;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.entity.TaxType;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class TaxPubEntity extends AbstractPubEntity<Tax>{

	private static final long serialVersionUID = -5609423203559560103L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	@Size(min=1, max=32, groups = DataValidation.class)
	private String name;
	
	@NotNull(groups = DataValidation.class)
	@Size(min=1, max=128, groups = DataValidation.class)
	private String description;
	
	@NotNull(groups = DataValidation.class)
	private BigDecimal value;
	
	@NotNull(groups = DataValidation.class)
	private TaxType type;
	
	private boolean discount;
	
	private byte order;

	@Constructor
	public TaxPubEntity() {
	}
	
	public TaxPubEntity(Tax e) {
		this.description = e.getDescription();
		this.discount = e.isDiscount();
		this.id = e.getId();
		this.name = e.getName();
		this.order = e.getOrder();
		this.type = e.getType();
		this.value = e.getValue();
	}
	
	@Override
	protected boolean isEqualId(Tax instance) throws Throwable {
		return instance.getId() == null? 
				this.id == null :
				this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(Tax instance) throws Throwable {
		return instance.getId() != null;
	}

	@Override
	protected Tax reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected Tax createNewInstance() throws Throwable {
		return new Tax();
	}

	@Override
	protected void copyTo(Tax o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setDescription(description);
		o.setDiscount(discount);
		o.setName(name);
		o.setOrder(order);
		o.setType(type);
		o.setValue(value);
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

	public boolean isDiscount() {
		return discount;
	}

	public void setDiscount(boolean discount) {
		this.discount = discount;
	}

	public byte getOrder() {
		return order;
	}

	public void setOrder(byte order) {
		this.order = order;
	}

}
