package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class RefundPubEntity extends GenericPubEntity<Refund> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Size(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Size(max = 38, min = 10, groups = IdValidation.class)
	private String order;

	private String refundType;
	
	private LocalDateTime date;

	private LocalDateTime refundDate;
	
	@NotNull(groups = DataValidation.class)
	private Map<String, Integer> itens;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private Map<String, String> addData;

	@Constructor
	public RefundPubEntity() {
	}
	
	public RefundPubEntity(Refund e, Locale locale) {
		this.addData = e.getAddData();
		this.date = e.getDate();
		this.refundDate = e.getRefundDate();
		this.id = e.getId() == null? null : SecretUtil.toProtectedID(e.getId());
		this.refundType = e.getRefundType();
		
		if(e.getProducts() != null) {
			this.itens = new HashMap<>();
			e.getProducts().forEach((i)->{
				this.itens.put(i.getSerial(), i.getUnits());
			});
		}
		
		this.order = e.getOrder();
	}
	
	@Override
	protected boolean isEqualId(Refund instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(Refund instance) throws Throwable {
		return false;
	}

	@Override
	protected Refund reloadEntity() throws Throwable {
		RefundRegistry shippingRegistry = EntityContextPlugin.getEntity(RefundRegistry.class);
		return shippingRegistry.findRefundById(id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected Refund createNewInstance() throws Throwable {
		RefundRegistry refundRegistry = EntityContextPlugin.getEntity(RefundRegistry.class);
		Order order = new Order();
		order.setId(this.order);
		return refundRegistry.createRefund(order);
	}

	@Override
	protected void copyTo(Refund o, boolean reload, boolean override, boolean validate) throws Throwable {
		
		o.setAddData(this.addData);

		o.setDate(this.date);
		o.setRefundDate(this.refundDate);
		o.setRefundType(id);
		o.setOrder(this.order);
		
		if(this.itens != null) {
			
			for(ProductRequest i: o.getProducts()) {
				Integer units = this.itens.get(i.getSerial());
				i.setUnits(units == null? 0 : units.intValue());
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

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(LocalDateTime refundDate) {
		this.refundDate = refundDate;
	}

	public Map<String, Integer> getItens() {
		return itens;
	}

	public void setItens(Map<String, Integer> itens) {
		this.itens = itens;
	}

	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}

	@Override
	protected String getCodeType() {
		return refundType;
	}

	@Override
	protected Class<?> getGenericType() {
		return RefundPubEntity.class;
	}

	@Override
	protected void loadProperties(GenericPubEntity<Refund> entity) {
		RefundPubEntity e = (RefundPubEntity)entity;
		this.setData(e.getData());
		this.date = e.getDate();
		this.id = e.getId();
		this.order = e.getOrder();
		this.itens = e.getItens();
		this.refundDate = e.getRefundDate();
		this.refundType = e.getRefundType();
	}
	
}
