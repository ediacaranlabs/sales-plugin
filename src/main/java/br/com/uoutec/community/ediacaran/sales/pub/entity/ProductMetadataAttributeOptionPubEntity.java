package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductMetadataAttributeOptionPubEntity extends AbstractPubEntity<ProductMetadataAttributeOption>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;
	
	@Transient
	private Integer productMetadataAttribute;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Length(max = 128, groups = DataValidation.class)
	private String value;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Length(max = 256, groups = DataValidation.class)
	private String description;

	private Boolean deleted;
	
	@Constructor
	public ProductMetadataAttributeOptionPubEntity(){
	}

	public ProductMetadataAttributeOptionPubEntity(ProductMetadataAttributeOption e, Locale locale){
		this.description = e.getDescription();
		this.protectedID = e.getId() <= 0? null : SecretUtil.toProtectedID(String.valueOf(e.getId()));
		this.value = e.getValue() == null? null : e.get;
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

	@Override
	protected void preRebuild(ProductMetadataAttributeOption instance, boolean reload, boolean override, boolean validate) {
		
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
	protected ProductMetadataAttributeOption reloadEntity() throws Throwable {
		ProductMetadataRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		return entityRegistry.findProductMetadataAttributeOptionById(this.id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductMetadataAttributeOption createNewInstance() throws Throwable {
		return new ProductMetadataAttributeOption();
	}

	@Override
	protected void copyTo(ProductMetadataAttributeOption o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setValue(this.value);
		o.setProductMetadataAttribute(this.productMetadataAttribute == null? 0 : this.productMetadataAttribute.intValue());
	}

	@Override
	protected boolean isEqualId(ProductMetadataAttributeOption instance) throws Throwable {
		return instance.getId() <= 0? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductMetadataAttributeOption instance) throws Throwable {
		return instance.getId() > 0;
	}
	
}
