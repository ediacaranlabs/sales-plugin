package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
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
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
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

	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Size(min = 10, max = 38, groups = DataValidation.class)
	private String order;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;

	private LocalDateTime refundDate;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	@NotNull(groups = DataValidation.class )
	@Valid
	private List<ProductRequestPubEntity> products;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private Map<String, String> addData;

	@Constructor
	public RefundPubEntity() {
	}
	
	public RefundPubEntity(Shipping e, Locale locale) {
		this.addData = e.getAddData();
		this.date = e.getDate();
		this.refundDate = e.getReceivedDate();
		this.id = e.getId() == null? null : SecretUtil.toProtectedID(e.getId());
		
		if(e.getProducts() != null) {
			this.products = new ArrayList<>();
			for(ProductRequest p: e.getProducts()) {
				this.products.add(new ProductRequestPubEntity(p, locale));
			}
		}
		
		this.order = e.getOrder();
		this.order = e.getOrder() == null? null : SecretUtil.toProtectedID(e.getOrder());
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

		if(this.id != null) {
			return;
		}

		o.setDepth(this.depth == null? 0f : this.depth.floatValue());
		o.setHeight(this.height == null? 0f : this.height.floatValue());
		o.setWeight(this.weight == null? 0f : this.weight.floatValue());
		o.setWidth(this.width == null? 0f : this.width.floatValue());
		
		o.setOrder(this.order);
		o.setShippingType(this.shippingType);
		
		if(this.products != null) {
			
			Map<String,Integer> units = new HashMap<>();
			for(ProductRequestPubEntity e: this.products) {
				ProductRequest p = e.rebuild(false, true, false);
				if(p.getSerial() != null) {
					units.put(p.getSerial(), e.getUnits());
				}
			}
			
			if(o.getProducts() != null) {
				for(ProductRequest p: o.getProducts()) {
					Integer u = units.get(p.getSerial());
					p.setUnits(u == null? 0 : u.intValue());
				}
			}
			
		}
		else {
			if(o.getProducts() != null) {
				for(ProductRequest p: o.getProducts()) {
					p.setUnits(0);
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public AddressPubEntity getOrigin() {
		return origin;
	}

	public void setOrigin(AddressPubEntity origin) {
		this.origin = origin;
	}

	public AddressPubEntity getDest() {
		return dest;
	}

	public void setDest(AddressPubEntity dest) {
		this.dest = dest;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getDepth() {
		return depth;
	}

	public void setDepth(Float depth) {
		this.depth = depth;
	}

	public List<ProductRequestPubEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequestPubEntity> products) {
		this.products = products;
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

	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}

	@Override
	protected String getCodeType() {
		return shippingType;
	}

	@Override
	protected Class<?> getGenericType() {
		return RefundPubEntity.class;
	}

	@Override
	protected void loadProperties(GenericPubEntity<Shipping> entity) {
		RefundPubEntity e = (RefundPubEntity)entity;
		this.setData(e.getData());
		this.date = e.getDate();
		this.depth = e.getDepth();
		this.dest = e.getDest();
		this.height = e.getHeight();
		this.id = e.getId();
		this.order = e.getOrder();
		this.products = e.getProducts();
		this.shippingType = e.getShippingType();
		this.weight = e.getWeight();
		this.width = e.getWidth();
		this.cancelJustification = e.getCancelJustification();
		this.cancelDate = e.getCancelDate();
	}
	
}
