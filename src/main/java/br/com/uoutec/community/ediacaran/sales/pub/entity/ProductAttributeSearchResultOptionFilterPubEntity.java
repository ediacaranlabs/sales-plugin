package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultOptionFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductAttributeSearchResultOptionFilterPubEntity 
	extends AbstractPubEntity<ProductAttributeSearchResultOptionFilter>{

	private static final long serialVersionUID = 156435810866580706L;

	/*
	@Transient
	private Integer id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	*/
	
	private String value;
	
	private String description;

	@Transient
	private ProductAttributeValueType type;
	
	private Boolean selected;
	
	@Constructor
	public ProductAttributeSearchResultOptionFilterPubEntity() {
	}
	
	public ProductAttributeSearchResultOptionFilterPubEntity(ProductAttributeSearchResultOptionFilter e, Locale locale) {
		this.description = e.getDescription();
		//this.id = e.getOptionId() <= 0? null : e.getOptionId();
		//this.protectedID = SecretUtil.toProtectedID(String.valueOf(e.getOptionId()));
		this.selected = e.isSelected();
		this.type = e.getType();
		this.value = e.getType().toString(e.getValue(), locale);
	}

	/*
	@Override
	protected void preRebuild(ProductAttributeSearchResultOptionFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	*/
	
	@Override
	protected boolean isEqualId(ProductAttributeSearchResultOptionFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductAttributeSearchResultOptionFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductAttributeSearchResultOptionFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductAttributeSearchResultOptionFilter createNewInstance() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void copyTo(ProductAttributeSearchResultOptionFilter o, boolean reload, boolean override, boolean validate) throws Throwable {
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

	public ProductAttributeValueType getType() {
		return type;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	
}
