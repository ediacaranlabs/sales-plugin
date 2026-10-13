package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.CurrencyUtil;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Invoice implements Serializable{

	private static final long serialVersionUID = -58736758314320975L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Size(max = 38, min = 10, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Size(max = 38, min = 10, groups = DataValidation.class)
	private String order;
	
	@NotNull(groups = DataValidation.class)
	private Client client;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	private LocalDateTime cancelDate;
	
	private String cancelJustification;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.CURRENCY)
	private String currency;
	
	public Invoice() {
	}
	
	public Invoice(Invoice e) {
		this.cancelDate = e.cancelDate;
		this.cancelJustification = e.cancelJustification;
		this.client = e.client;
		this.currency = e.currency;
		this.date = e.date;
		this.id = e.id;
		this.itens = e.itens;
		this.itensLoaded = e.itensLoaded;
		this.order = e.order;
		this.taxes = e.taxes;
		this.taxesLoaded = e.taxesLoaded;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client owner) {
		this.client = owner;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return CurrencyUtil.getSymbol(currency);
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

	public String toStringDate(Locale locale) {
		if(date == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return date.format(dateTimeFormatter);
	}
	
	public String toStringCancelDate(Locale locale) {
		if(cancelDate == null) {
			return "";
		}
		//DateTimeFormatter.withLocale(locale).
		
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		return cancelDate.format(dateTimeFormatter);
	}

	@Valid
	private List<Tax> taxes;
	
	private volatile boolean taxesLoaded = false;
	
	public List<Tax> getTaxes() {
		
		if(this.id == null) {
			return taxes;
		}
		
		if(!taxesLoaded) {
			try {
				InvoiceRegistry registry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
				this.taxes = registry.getTaxes(id);
				this.taxesLoaded = true;
			}
			catch(Throwable ex) {
				throw new IllegalStateException("order load fail", ex);
			}
		}
		
		return taxes;
	}
	
	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
		this.taxesLoaded = taxes != null;
	}
	
	@Valid
	@Size(min = 1, groups = DataValidation.class)
	private List<ProductRequest> itens;
	
	private volatile boolean itensLoaded = false;
	
	public List<ProductRequest> getItens() {
		
		if(this.id == null) {
			return itens;
		}
		
		if(!itensLoaded) {
			try {
				InvoiceRegistry registry = EntityContextPlugin.getEntity(InvoiceRegistry.class);
				this.itens = registry.getProductRequest(id);
				this.itensLoaded = true;
			}
			catch(Throwable ex) {
				throw new IllegalStateException("order load fail", ex);
			}
		}
		
		return itens;
	}

	public void setItens(List<ProductRequest> itens) {
		this.itens = itens;
		this.itensLoaded = itens != null;
	}

	public BigDecimal getSubtotal(){
		
		if(getItens() == null || getItens().isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: getItens()) {
			value = value.add(pr.getSubtotal());
		}
		return value;
	}

	public String getDisplaySubtotal() {
		return CurrencyUtil.toString(currency, getSubtotal());
	}
	
	public BigDecimal getDiscount() {

		if(getItens() == null || getItens().isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: getItens()) {
			value = value.add(pr.getDiscount());
		}
		
		return value.add(getDiscountBySubtotal());
	}

	private BigDecimal getDiscountBySubtotal() {
		
		if(getItens() == null || getItens().isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = getSubtotal();
		BigDecimal discount = BigDecimal.ZERO;
		
		if(getTaxes() != null) {
			
			List<Tax> tx = getTaxes();
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
				if(t.isDiscount()) {
					discount = discount.add(taxUnit);
				}
			}
			
		}
		
		return discount;
	}
	
	public String getDisplayDiscount() {
		return CurrencyUtil.toString(currency, getDiscount());
	}
	
	public BigDecimal getTax() {
		
		if(getItens() == null || getItens().isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		for(ProductRequest pr: getItens()) {
			value = value.add(pr.getTax());
		}
		return value.add(getTaxBySubtotal());
	}

	public String getDisplayTax() {
		return CurrencyUtil.toString(currency, getTax());
	}
	
	private BigDecimal getTaxBySubtotal() {
		
		if(getItens() == null || getItens().isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = getSubtotal();
		BigDecimal tax = BigDecimal.ZERO;
		
		if(getTaxes() != null) {
			
			List<Tax> tx = getTaxes();
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
				if(!t.isDiscount()) {
					tax = tax.add(taxUnit);
				}
			}
			
		}
		
		return tax;
	}
	
	public BigDecimal getTotal(){
		
		if(getItens() == null || getItens().isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal value = BigDecimal.ZERO;
		
		for(ProductRequest pr: getItens()) {
			value = value.add(pr.getTotal());
		}
		
		if(getTaxes() != null) {
			
			List<Tax> tx = getTaxes();
			
			Collections.sort(tx, (a,b)->a.getOrder() - b.getOrder());
			
			for(Tax t: tx) {
				BigDecimal taxUnit = t.getType().apply(value, t.getValue());
				value = t.isDiscount()? value.subtract(taxUnit) : value.add(taxUnit); 
			}
			
		}
		
		return value;
	}

	public String getDisplayTotal() {
		return CurrencyUtil.toString(currency, getTotal());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		return Objects.equals(id, other.id);
	}
	
}
