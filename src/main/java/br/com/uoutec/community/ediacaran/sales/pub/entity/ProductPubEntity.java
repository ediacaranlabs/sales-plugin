package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Enumerated;
import org.brandao.brutos.annotation.EnumerationType;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.front.components.Image;
import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;
import br.com.uoutec.community.ediacaran.sales.SalesPluginConstants;
import br.com.uoutec.community.ediacaran.sales.entity.MeasurementUnit;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
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

	@NotNull(groups=DataValidation.class)
	@Size(min=3, max = 256,groups=DataValidation.class)
	private String shortDescription;
	
	@Enumerated(EnumerationType.STRING)
	@NotNull(groups=DataValidation.class)
	private String productType;

	@NotNull(groups=DataValidation.class)
	private String productMetadata;

	private Integer productMetadataID;
	
	@NotNull(groups=DataValidation.class)
	@Enumerated(EnumerationType.STRING)
	private MeasurementUnit measurementUnit;

	@NotNull(groups=DataValidation.class)
	private BigDecimal cost;
	
	@NotNull(groups=DataValidation.class)
	@Size(min=3,groups=DataValidation.class)
	private String currency;

	@Size(max = 600)
	@Pattern(regexp="^([^\"\',]+)(,([^\"\',]+))*$")
	private String tagsString;

	@NotNull(groups = DataValidation.class)
	private Set<String> tags;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductImagePubEntity> images;
	
	private Map<String, String> attributes;
	
	@Transient
	@NotNull(groups=DataValidation.class)
	private Locale locale;
	
	@Constructor
	public ProductPubEntity(){
		setTags(new HashSet<>());
	}

	public ProductPubEntity(Product e, Locale locale){
		this.measurementUnit = e.getMeasurementUnit();
		this.cost = e.getCost();
		this.currency = e.getCurrency();
		this.description = e.getDescription();
		this.protectedID = e.getId() <= 0? null : SecretUtil.toProtectedID(String.valueOf(e.getId()));
		this.name = e.getName();
		this.productType = e.getProductType();
		this.tags = e.getTags();
		this.shortDescription = e.getShortDescription();
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

	public String getTagsString() {
		return tagsString;
	}

	public void setTagsString(String tagsString) {
		this.tagsString = tagsString;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
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

	public List<ProductImagePubEntity> getImages() {
		return images;
	}

	public void setImages(List<ProductImagePubEntity> images) {
		this.images = images;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(String productMetadata) {
		this.productMetadata = productMetadata;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Integer getProductMetadataID() {
		return productMetadataID;
	}

	public void setProductMetadataID(Integer productMetadataID) {
		this.productMetadataID = productMetadataID;
	}

	@Override
	protected void preRebuild(Product instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
		
		try {
			this.productMetadataID = Integer.parseInt(SecretUtil.toID(this.productMetadata));
		}
		catch(Throwable ex){
			this.productMetadataID = 0;
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
		o.setMeasurementUnit(this.measurementUnit);
		o.setProductType(this.productType);
		o.setThumb(thumbnail == null? null : thumbnail.save(SalesPluginConstants.WIDTH_PRODUCT_IMAGE, SalesPluginConstants.HEIGHT_PRODUCT_IMAGE));
		o.setTags(this.tagsString != null? StringUtil.toSet(this.tagsString, ",") : tags);
		o.setShortDescription(this.shortDescription);
		o.setMetadata(this.productMetadataID == null? 0 : this.productMetadataID.intValue());
		
		if(this.attributes != null) {
			
			ProductMetadataRegistry productMetadataRegistry = EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
			ProductMetadata productMetadata = productMetadataRegistry.findProductMetadataById(this.productMetadataID);
			
			List<ProductMetadataAttribute> metadataAttributes = productMetadata.getAttributeList();

			for(ProductMetadataAttribute attr: metadataAttributes) {
				String value = this.attributes.get(attr.getCode());
				o.setAttribute(attr.getCode(), value, locale);
			}
			
		}
		
		
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
		this.measurementUnit = x.getMeasurementUnit();
		this.productType = x.getProductType();
		this.protectedID = x.getProtectedID();
		this.thumbnail = x.thumbnail;
		this.shortDescription = x.shortDescription;
		this.tags = x.tags;
		this.tagsString = x.tagsString;
		this.productMetadata = x.getProductMetadata();
		this.locale = x.getLocale();
	}
	
}
