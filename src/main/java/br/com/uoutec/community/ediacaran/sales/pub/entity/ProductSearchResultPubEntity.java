package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Product;

public class ProductSearchResultPubEntity extends ProductPubEntity{

	private static final long serialVersionUID = -5240855789107084675L;

	private String thumbnail;
	
	private String cost;

	@Constructor
	public ProductSearchResultPubEntity(){
	}

	public ProductSearchResultPubEntity(Product e, Locale locale){
		super(e, locale);
		this.thumbnail = e.getPublicThumb();
		this.cost = e.getCostString(locale);
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
	
}
