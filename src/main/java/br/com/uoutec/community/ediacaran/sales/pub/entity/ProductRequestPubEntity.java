package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductRequestPubEntity extends AbstractPubEntity<ProductRequest>{

	@Transient
	private static final long serialVersionUID = -8242938469398581888L;

	@NotNull(groups = IdValidation.class)
	private String id;

	@NotNull(groups = IdValidation.class)
	private String orderId;
	
	@NotNull(groups = DataValidation.class)
	private ProductPubEntity product;
	
	@NotNull(groups = DataValidation.class)
	private Integer units;
	
	@NotNull(groups = DataValidation.class)
	private BigDecimal cost;
	
	@NotNull(groups = DataValidation.class)
	private String currency;

	private List<TaxPubEntity> taxes;
	
	@NotNull(groups = DataValidation.class)
	private Map<String, String> addData;
	
	@Constructor
	public ProductRequestPubEntity(){
	}
	
	public ProductRequestPubEntity(ProductRequest e){
		this.addData = e.getAddData();
		this.cost = e.getCost();
		this.currency = e.getCurrency();
		this.id = e.getId();
		this.product = e.getProduct() == null? null : new ProductPubEntity(e.getProduct());
		this.units = e.getUnits();
		
		this.taxes = new ArrayList<>();
		
		if(e.getTaxes() != null) {
			for(Tax t: e.getTaxes()) {
				this.taxes.add(new TaxPubEntity(t));
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductPubEntity getProduct() {
		return product;
	}

	public void setProduct(ProductPubEntity product) {
		this.product = product;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Map<String, String> getAddData() {
		return addData;
	}

	public void setAddData(Map<String, String> addData) {
		this.addData = addData;
	}

	@Override
	protected boolean isEqualId(ProductRequest instance) throws Throwable {
		return instance.getId() == null? 
				this.id == null :
				this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductRequest instance) throws Throwable {
		return instance.getId() != null;
	}

	@Override
	protected ProductRequest reloadEntity()	throws Throwable {
		OrderRegistry registry = EntityContextPlugin.getEntity(OrderRegistry.class);
		return registry.getProductRequest(this.orderId, this.id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ProductRequest createNewInstance() throws Throwable {
		return new ProductRequest();
	}

	@Override
	protected void copyTo(ProductRequest o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setAddData(this.addData);
		o.setCost(this.cost);
		o.setCurrency(this.currency);
		o.setId(this.id);
		o.setProduct(this.product == null? null : this.product.rebuild(true, false, true));
		o.setUnits(this.units == null? 0 : this.units);
	}
	
}
