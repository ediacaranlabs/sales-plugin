package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;

public class ProductCategorySearchResultPubEntity extends GenericPubEntity<ProductCategory>{

	private static final long serialVersionUID = -5240855789107084675L;

	private String thumbnail;
	
	private String cost;

	private String tags;
	
	@Constructor
	public ProductCategorySearchResultPubEntity(){
	}

	public ProductCategorySearchResultPubEntity(ProductCategory e, Locale locale){
		this.thumbnail = e.getPublicThumb();
		this.cost = e.getCostString(locale);
		this.tags = e.getTagsString();
	}

	public String getThumbnailPath() {
		return thumbnail;
	}

	@Transient
	public void setThumbnailPath(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getCostString() {
		return cost;
	}

	@Transient
	public void setCostString(String cost) {
		this.cost = cost;
	}

	public String getTagsString() {
		return tags;
	}

	@Transient
	public void setTagsString(String tags) {
		this.tags = tags;
		super.setTagsString(tags);
	}
	
}
