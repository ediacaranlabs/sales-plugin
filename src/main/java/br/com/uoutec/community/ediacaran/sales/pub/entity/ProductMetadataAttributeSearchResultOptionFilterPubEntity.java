package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeSearchResultOptionFilter;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductMetadataAttributeSearchResultOptionFilterPubEntity 
	extends AbstractPubEntity<ProductMetadataAttributeSearchResultOptionFilter>{

	private static final long serialVersionUID = 156435810866580706L;

	@Transient
	private Integer id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	private Object value;
	
	private String description;

	private ProductAttributeValueType type;
	
	@Constructor
	public ProductMetadataAttributeSearchResultOptionFilterPubEntity() {
	}
	
	public ProductMetadataAttributeSearchResultOptionFilterPubEntity(ProductMetadataAttributeSearchResultOptionFilter e, Locale locale) {
		this.description = e.getDescription();
		this.id = e.getOptionId() <= 0? null : e.getOptionId();
		this.protectedID = SecretUtil.toProtectedID(String.valueOf(e.getOptionId()));
		this.type = e.getType();
		this.value = e.getType().toString(e.getValue(), locale);
	}

	@Override
	protected void preRebuild(ProductMetadataAttributeSearchResultOptionFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(ProductMetadataAttributeSearchResultOptionFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductMetadataAttributeSearchResultOptionFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductMetadataAttributeSearchResultOptionFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductMetadataAttributeSearchResultOptionFilter createNewInstance() throws Throwable {
		return new ProductMetadataAttributeSearchResultOptionFilter();
	}

	@Override
	protected void copyTo(ProductMetadataAttributeSearchResultOptionFilter o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setOptionId(this.id == null? 0 : this.id.intValue());
		o.setType(this.type);
		o.setValue(this.value);
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

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductAttributeValueType getType() {
		return type;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
	}
	
}
