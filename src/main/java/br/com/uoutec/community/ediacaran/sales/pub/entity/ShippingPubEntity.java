package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ShippingPubEntity extends AbstractPubEntity<Shipping> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(min = 10, max = 38, groups = IdValidation.class)
	private String id;

	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = DataValidation.class)
	@Length(min = 10, max = 38, groups = DataValidation.class)
	private String order;
	
	@NotNull(groups = DataValidation.class)
	private LocalDateTime date;
	
	@NotNull(groups = DataValidation.class)
	private String shippingType;

	@NotNull(groups = DataValidation.class)
	private AddressPubEntity origin;
	
	@NotNull(groups = DataValidation.class)
	private AddressPubEntity dest;

	@Min(value=0, groups = DataValidation.class )
	private Float weight;
	
	@Min(value=0, groups = DataValidation.class )
	private Float height;
	
	@Min(value=0, groups = DataValidation.class )
	private Float width;
	
	@Min(value=0, groups = DataValidation.class )
	private Float depth;
	
	@NotNull(groups = DataValidation.class)
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductRequestPubEntity> itens;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private Map<String, String> addData;

	@Constructor
	public ShippingPubEntity() {
	}
	
	public ShippingPubEntity(Shipping e, Locale locale) {
	}

	@Override
	protected boolean isEqualId(Shipping instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(Shipping instance) throws Throwable {
		return false;
	}

	@Override
	protected Shipping reloadEntity() throws Throwable {
		ShippingRegistry shippingRegistry = EntityContextPlugin.getEntity(ShippingRegistry.class);
		return shippingRegistry.findById(id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected Shipping createNewInstance() throws Throwable {
		ShippingRegistry shippingRegistry = EntityContextPlugin.getEntity(ShippingRegistry.class);
		Order order = new Order();
		order.setId(this.order);
		return shippingRegistry.toShipping(order);
	}

	@Override
	protected void copyTo(Shipping o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setAddData(this.addData);
		o.setDepth(this.depth);
		o.setHeight(this.height);
		o.setWeight(this.weight);
		o.setWidth(this.width);
		o.setDate(this.date);
		o.setDest(this.dest == null? null : this.dest.rebuild(true, false, true));
		o.setOrigin(this.origin == null? null : this.origin.rebuild(true, false, true));
		o.setId(this.id);
		o.setOrder(this.order);
		o.setShippingType(this.shippingType);
		
		if(this.itens != null) {
			
			Map<String,Integer> units = new HashMap<>();
			for(ProductRequestPubEntity e: this.itens) {
				ProductRequest p = e.rebuild(true, false, true);
				units.put(p.getSerial(), e.getUnits());
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

	
}
