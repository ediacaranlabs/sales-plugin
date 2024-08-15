package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Enumerated;
import org.brandao.brutos.annotation.EnumerationType;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class OrderPubEntity extends AbstractPubEntity<Order> {

	@Transient
	private static final long serialVersionUID = 7794103768364892856L;

	@NotNull(groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	@NotNull(groups = DataValidation.class)
	@Enumerated(EnumerationType.STRING)
	private OrderStatus status;

	@Valid
	@NotNull(groups = DataValidation.class)
	private PaymentPubEntity payment;

	@Valid
	private InvoicePubEntity invoice;

	@Valid
	@Size(min = 1, groups = DataValidation.class)
	private List<ProductRequestPubEntity> itens;

	@Constructor
	public OrderPubEntity() {
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public PaymentPubEntity getPayment() {
		return payment;
	}

	public void setPayment(PaymentPubEntity payment) {
		this.payment = payment;
	}

	public InvoicePubEntity getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoicePubEntity invoice) {
		this.invoice = invoice;
	}

	public List<ProductRequestPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductRequestPubEntity> itens) {
		this.itens = itens;
	}

	@Override
	protected Order reloadEntity() throws Throwable {
		OrderRegistry r = EntityContextPlugin.getEntity(OrderRegistry.class);
		return r.findById(this.id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected Order createNewInstance() throws Throwable {
		return new Order();
	}

	@Override
	protected void copyTo(Order o, boolean reload, boolean override,
			boolean validate) throws Throwable {

		o.setDate(this.date);
		o.setId(this.id);
		o.setInvoice(this.invoice == null ? 
				null : 
				this.invoice.rebuild(o.getInvoice(), override, validate));


		if(this.itens != null){
			List<ProductRequest> l = new ArrayList<ProductRequest>();
			for(ProductRequestPubEntity i: this.itens){
				l.add(
					i.getId() == null? 
						i.rebuild(false, true, true) : 
						i.rebuild(true, true, true));
			}
		}
		
		o.setPayment(this.payment == null ? null : this.payment.rebuild(
				o.getPayment(), override, validate));
		o.setRemoved(false);
		o.setStatus(this.status);
	}

	@Override
	protected boolean isEqualId(Order instance) throws Throwable {
		return instance.getId() != null?
					this.id == null :
					instance.getId().equals(this.id);
	}

	@Override
	protected boolean hasId(Order instance) throws Throwable {
		return instance.getId() != null;
	}

}
