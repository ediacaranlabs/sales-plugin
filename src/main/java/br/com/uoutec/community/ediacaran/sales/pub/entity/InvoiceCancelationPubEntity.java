package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class InvoiceCancelationPubEntity extends AbstractPubEntity<Invoice> {

	private static final long serialVersionUID = 1647504574319126033L;
	
	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Size(min = 10, max = 38, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime cancelDate;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp=CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Size(min = 5, max = 1024, groups = DataValidation.class)
	private String cancelJustification;
	
	public InvoiceCancelationPubEntity() {
		super();
		this.cancelDate = LocalDateTime.now();
	}
	
	public InvoiceCancelationPubEntity(Invoice e, Locale locale) {
		this.cancelJustification = e.getCancelJustification();
		this.cancelDate = e.getCancelDate();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	protected boolean isEqualId(Invoice instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(Invoice instance) throws Throwable {
		return false;
	}

	@Override
	protected Invoice reloadEntity() throws Throwable {
		InvoiceRegistry registry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		return registry.findById(id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected Invoice createNewInstance() throws Throwable {
		throw new UnsupportedOperationException();
	}
	
	protected void copyTo(Invoice o, boolean reload, boolean override, boolean validate) throws Throwable {
		if(o.getCancelJustification() == null) {
			o.setCancelJustification(this.cancelJustification);
		}
	}
	
	
}
