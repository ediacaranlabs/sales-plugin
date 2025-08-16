package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Enumerated;
import org.brandao.brutos.annotation.EnumerationType;
import org.brandao.brutos.annotation.MappingTypes;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReportCause;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrderReportPubEntity extends AbstractPubEntity<OrderReport> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Length(min = 10, max = 38, groups = DataValidation.class)
	private String order;

	@Length(min = 1, groups = DataValidation.class)
	private Integer user;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;
	
	@Enumerated(EnumerationType.STRING)
	@NotNull(groups = DataValidation.class )
	private OrderReportStatus status;
	
	@NotNull(groups = DataValidation.class)
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductRequestReportPubEntity> products;
	
	@Constructor
	public OrderReportPubEntity() {
	}
	
	public OrderReportPubEntity(OrderReport e, Locale locale) {
		this.id = e.getId() == null? null : SecretUtil.toProtectedID(e.getId());
		this.order = e.getOrder() == null? null : SecretUtil.toProtectedID(e.getOrder().getId());
		this.user = e.getUser() == null? null : e.getUser().getId();
		this.date = e.getDate();
		this.status = e.getStatus();
		
		if(e.getProducts() != null) {
			this.products = new ArrayList<>();
			for(ProductRequestReport p: e.getProducts()) {
				this.products.add(new ProductRequestReportPubEntity(p, locale));
			}
		}
		
	}
	
	@Override
	protected boolean isEqualId(OrderReport instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(OrderReport instance) throws Throwable {
		return false;
	}

	@Override
	protected OrderReport reloadEntity() throws Throwable {
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		return orderReportRegistry.findById(id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected OrderReport createNewInstance() throws Throwable {
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		Order order = new Order();
		order.setId(this.order);
		return orderReportRegistry.toOrderReport(order);
	}

	@Override
	protected void copyTo(OrderReport o, boolean reload, boolean override, boolean validate) throws Throwable {
		
		if(this.id != null) {
			return;
		}

		if(this.order != null) {
			o.setOrder(new Order());
			o.getOrder().setId(order);
		}

		o.setStatus(this.status);
		o.setDate(this.date);
		
		if(this.products != null) {
			
			Map<String,ProductRequestReportCause> units = new HashMap<>();
			for(ProductRequestReportPubEntity e: this.products) {
				ProductRequestReport p = (ProductRequestReport) e.rebuild(false, true, false);
				if(p.getSerial() != null) {
					units.put(p.getSerial(), e.getCause());
				}
			}
			
			if(o.getProducts() != null) {
				for(ProductRequestReport p: o.getProducts()) {
					ProductRequestReportCause u = units.get(p.getSerial());
					p.setCause(u);
				}
			}
			
		}
		else {
			if(o.getProducts() != null) {
				for(ProductRequestReport p: o.getProducts()) {
					p.setCause(null);
				}
			}
		}
		
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

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public OrderReportStatus getStatus() {
		return status;
	}

	public void setStatus(OrderReportStatus status) {
		this.status = status;
	}

	public List<ProductRequestReportPubEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequestReportPubEntity> products) {
		this.products = products;
	}

}
