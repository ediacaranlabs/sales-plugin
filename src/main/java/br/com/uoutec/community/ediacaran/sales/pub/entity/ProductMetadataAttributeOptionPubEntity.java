package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductMetadataAttributeOptionPubEntity extends AbstractPubEntity<ProductMetadataAttributeOptionUpdate>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;
	
	@Transient
	private Integer productMetadataAttribute;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	@NotNull(groups = DataValidation.class)
	//@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Length(max = 128, groups = DataValidation.class)
	private String value;

	@NotNull
	@Transient
	private ProductAttributeValueType valueType;

	@NotNull(groups = DataValidation.class)
	//@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Length(max = 256, groups = DataValidation.class)
	private String description;

	private Boolean deleted;

	@Transient
	@NotNull(groups=DataValidation.class)
	private Locale locale;
	
	@Constructor
	public ProductMetadataAttributeOptionPubEntity(){
	}

	public ProductMetadataAttributeOptionPubEntity(ProductMetadataAttributeOption e, Locale locale){
		this.description = e.getDescription();
		this.protectedID = e.getId() <= 0? null : SecretUtil.toProtectedID(String.valueOf(e.getId()));
		this.value = e.getValue() == null? null : e.getValueType().toString(e.getValue(), locale);
		this.valueType = e.getValueType();
		this.id = e.getId();
		this.productMetadataAttribute = e.getProductMetadataAttribute();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(Integer productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
	}

	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public ProductAttributeValueType getValueType() {
		return valueType;
	}

	public void setValueType(ProductAttributeValueType valueType) {
		this.valueType = valueType;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	protected void preRebuild(ProductMetadataAttributeOptionUpdate instance, boolean reload, boolean override, boolean validate) {
		
		try {
			String str = SecretUtil.toID(this.protectedID);
			String[] parts = str.split("\\-");
			if(parts.length != 2) {
				throw new IllegalStateException();
			}
			this.id = Integer.parseInt(parts[0]);
			this.productMetadataAttribute = Integer.parseInt(parts[1]);
		}
		catch(Throwable ex){
			this.id = 0;
			this.productMetadataAttribute = 0;
		}
	}
	
	@Override
	protected ProductMetadataAttributeOptionUpdate reloadEntity() throws Throwable {
		ProductMetadataRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		ProductMetadataAttributeOption e = entityRegistry.findProductMetadataAttributeOptionById(this.id);
		return e == null? null : new ProductMetadataAttributeOptionUpdate(e);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductMetadataAttributeOptionUpdate createNewInstance() throws Throwable {
		return new ProductMetadataAttributeOptionUpdate();
	}

	@Override
	protected void copyTo(ProductMetadataAttributeOptionUpdate o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setValue(this.value == null? null : valueType.parse(this.value, this.locale));
		o.setProductMetadataAttribute(this.productMetadataAttribute == null? 0 : this.productMetadataAttribute.intValue());
		o.setValueType(this.valueType);
	}

	@Override
	protected boolean isEqualId(ProductMetadataAttributeOptionUpdate instance) throws Throwable {
		return instance.getId() <= 0? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductMetadataAttributeOptionUpdate instance) throws Throwable {
		return instance.getId() > 0;
	}
	
}
