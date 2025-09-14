package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Constructor;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrderReportMessagePubEntity extends AbstractPubEntity<OrderReportMessage> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Length(min = 10, max = 38, groups = DataValidation.class)
	private String orderReport;
	
	@NotNull(groups = DataValidation.class)
	private Integer user;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;
	
	@Length(min = 1, max = 128, groups = DataValidation.class)
	private String message;
	
	@Constructor
	public OrderReportMessagePubEntity() {
	}
	
	public OrderReportMessagePubEntity(OrderReportMessage e, Locale locale) {
		this.id = e.getId();
		this.orderReport = e.getOrderReport();
		this.message = e.getMessage();
		this.user = e.getUser() == null? null : e.getUser().getId();
		this.date = e.getDate();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderReport() {
		return orderReport;
	}

	public void setOrderReport(String orderReport) {
		this.orderReport = orderReport;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	protected boolean isEqualId(OrderReportMessage instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(OrderReportMessage instance) throws Throwable {
		return false;
	}

	@Override
	protected OrderReportMessage reloadEntity() throws Throwable {
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		OrderReport orderReport = new OrderReport();
		orderReport.setId(this.orderReport);
		return orderReportRegistry.getMessageById(id, orderReport);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected OrderReportMessage createNewInstance() throws Throwable {
		return new OrderReportMessage();
	}

	@Override
	protected void copyTo(OrderReportMessage o, boolean reload, boolean override, boolean validate) throws Throwable {
		
		o.setDate(date);
		o.setMessage(message);
		o.setOrderReport(orderReport);
		
		if(this.user != null) {
			SystemUser user = new SystemUser();
			user.setId(this.user);
			o.setUser(user);
		}
		
	}

}
