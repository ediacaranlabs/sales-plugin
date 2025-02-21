package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Product;

public class ProductSimplifiedSearchResultPubEntity extends ProductPubEntity{

	private static final long serialVersionUID = -5240855789107084675L;

	private String thumbnail;
	
	private String publicID;
	
	private String cost;

	private String tags;
	
	@Constructor
	public ProductSimplifiedSearchResultPubEntity(){
	}

	public ProductSimplifiedSearchResultPubEntity(Product e, Locale locale){
		super(e, locale);
		this.thumbnail = e.getPublicThumb();
		this.cost = e.getCostString(locale);
		this.tags = null;
		this.publicID = e.getPublicID();
		super.setShortDescription(super.getShortDescription() == null? null : (super.getShortDescription().length() > 128? super.getShortDescription().substring(0, 128) + " ..." : super.getShortDescription())  );
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
	
	@Transient
	public void setDescription(String value) {
		super.setDescription(value);
	}

	public String getPublicID() {
		return publicID;
	}

	public void setPublicID(String publicID) {
		this.publicID = publicID;
	}
	
}
