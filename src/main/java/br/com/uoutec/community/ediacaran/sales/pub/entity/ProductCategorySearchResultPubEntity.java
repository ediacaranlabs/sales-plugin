package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductCategorySearchResultPubEntity extends AbstractPubEntity<ProductCategory>{

	private static final long serialVersionUID = -5240855789107084675L;

	private String id;
	
	private String thumbnail;
	
	private String name;

	private String description;
	
	@Constructor
	public ProductCategorySearchResultPubEntity(){
	}

	public ProductCategorySearchResultPubEntity(ProductCategory e, Locale locale){
		this.id          = e.getProtectedID();
		this.thumbnail   = e.getPublicThumb();
		this.name        = e.getName();
		this.description = e.getDescription();
	}

	@Override
	protected boolean isEqualId(ProductCategory instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductCategory instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductCategory reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected ProductCategory createNewInstance() throws Throwable {
		return null;
	}
	
	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void copyTo(ProductCategory o, boolean reload, boolean override, boolean validate) throws Throwable {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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

}
