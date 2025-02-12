package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Enumerated;
import org.brandao.brutos.annotation.EnumerationType;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.front.components.Image;
import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;
import br.com.uoutec.community.ediacaran.sales.entity.PeriodType;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductPubEntity extends GenericPubEntity<Product>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	private Image thumbnail;
	
	@NotNull(groups=DataValidation.class)
	@Size(min=3,groups=DataValidation.class)
	@Pattern(regexp=CommonValidation.NAME_FORMAT,groups=DataValidation.class)
	private String name;
	
	@NotNull(groups=DataValidation.class)
	@Size(min=3,groups=DataValidation.class)
	private String description;

	@Enumerated(EnumerationType.STRING)
	@NotNull(groups=DataValidation.class)
	private String productType;
	
	@NotNull(groups=DataValidation.class)
	@Enumerated(EnumerationType.STRING)
	private PeriodType periodType;

	@NotNull(groups=DataValidation.class)
	private BigDecimal cost;
	
	@NotNull(groups=DataValidation.class)
	@Size(min=3,groups=DataValidation.class)
	private String currency;

	private String costString;
	
	@Constructor
	public ProductPubEntity(){
	}

	public ProductPubEntity(Product e, Locale locale){
		this.periodType = e.getPeriodType();
		this.cost = e.getCost();
		this.currency = e.getCurrency();
		this.description = e.getDescription();
		this.protectedID = e.getId() <= 0? null : SecretUtil.toProtectedID(String.valueOf(e.getId()));
		this.name = e.getName();
		this.productType = e.getProductType();
		this.costString = e.getCostString(locale);
	}
	
	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public PeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
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

	public String getCostString() {
		return costString;
	}

	public void setCostString(String costString) {
		this.costString = costString;
	}

	@Override
	protected void preRebuild(Product instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected Product reloadEntity() throws Throwable {
		ProductRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductRegistry.class);
		return entityRegistry.findProductById(this.id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected Product createNewInstance() throws Throwable {
		return new Product();
	}

	@Override
	protected void copyTo(Product o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setCost(this.cost);
		o.setCurrency(this.currency);
		o.setDescription(this.description);
		o.setId(this.id);
		o.setName(this.name);
		o.setPeriodType(this.periodType);
		o.setProductType(this.productType);
	}

	@Override
	protected boolean isEqualId(Product instance) throws Throwable {
		return instance.getId() <= 0? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(Product instance) throws Throwable {
		return instance.getId() > 0;
	}

	@Override
	protected String getCodeType() {
		return productType;
	}

	@Override
	protected Class<?> getGenericType() {
		return ProductPubEntity.class;
	}

	@Override
	protected void loadProperties(GenericPubEntity<Product> e) {
		ProductPubEntity x = (ProductPubEntity)e; 
		this.cost = x.getCost();
		this.currency = x.getCurrency();
		this.description = x.getDescription();
		this.id = x.getId();
		this.name = x.getName();
		this.periodType = x.getPeriodType();
		this.productType = x.getProductType();
		this.protectedID = x.getProtectedID();
	}
	
}
