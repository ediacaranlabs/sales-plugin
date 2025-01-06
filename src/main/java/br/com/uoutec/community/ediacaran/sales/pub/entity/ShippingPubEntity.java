package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingsResultSearch;
import br.com.uoutec.community.ediacaran.sales.shipping.ProductPackage;
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

	@Valid
	@NotNull(groups = DataValidation.class)
	private AddressPubEntity origin;
	
	@Valid
	@NotNull(groups = DataValidation.class)
	private AddressPubEntity dest;
	
	@Valid
	@NotNull(groups = DataValidation.class)
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductPackage> itens;
	
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
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected Shipping createNewInstance() throws Throwable {
		return new Shipping();
	}

	@Override
	protected void copyTo(Shipping o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setAddData(this.addData);
		o.setDate(this.date);
		o.setDest(this.dest == null? null : this.dest.rebuild(true, false, true));
		o.setOrigin(this.origin == null? null : this.origin.rebuild(true, false, true));
		o.setId(this.id);
		o.setOrder(this.order);
		o.setShippingType(this.shippingType);
		
	}

	
}
