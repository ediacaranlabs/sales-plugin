package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class InvoicePubEntity extends AbstractPubEntity<Invoice>{

	@Transient
	private static final long serialVersionUID = -8302935462723894018L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String id;

	@NotNull
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String order;
	
	//@NotNull(groups = DataValidation.class)
	//private LocalDateTime date;

	@NotNull(groups = DataValidation.class)
	private Map<String, Integer> itens;
	
	@Constructor
	public InvoicePubEntity(){
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Integer> getItens() {
		return itens;
	}

	public void setItens(Map<String, Integer> itens) {
		this.itens = itens;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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
		InvoiceRegistry invoiceRegistry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		return invoiceRegistry.findById(id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected Invoice createNewInstance() throws Throwable {
		InvoiceRegistry invoiceRegistry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
		Order order = new Order();
		order.setId(this.order);
		return invoiceRegistry.toInvoice(order);
	}

	@Override
	protected void copyTo(Invoice o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		if(this.itens != null) {
			
			for(ProductRequest i: o.getItens()) {
				Integer units = this.itens.get(i.getSerial());

				i.setUnits(units == null? 0 : units.intValue());
				
			}
			
		}
		
	}
	
}
