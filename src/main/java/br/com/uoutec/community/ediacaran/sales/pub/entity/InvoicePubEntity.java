package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class InvoicePubEntity extends AbstractPubEntity<Invoice>{

	@Transient
	private static final long serialVersionUID = -8302935462723894018L;

	@NotNull(groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	
	@Constructor
	public InvoicePubEntity(){
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	protected boolean isEqualId(Invoice instance) throws Throwable {
		return instance.getId() == null? 
				this.id == null :
				this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(Invoice instance) throws Throwable {
		return instance.getId() != null;
	}

	@Override
	protected Invoice reloadEntity() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Invoice createNewInstance() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void copyTo(Invoice o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDate(this.date);
		o.setId(this.id);
	}
	
}
