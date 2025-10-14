package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductPrice;

public class ProductSimplifiedSearchResultPubEntity extends ProductPubEntity {

	private static final long serialVersionUID = -5240855789107084675L;

	private String thumbnail;
	
	private String publicID;
	
	private String value;

	private String valueWithoutDiscount;
	
	private Boolean hasDiscount;
	
	private ProductPrice productValue;
	
	private ProductPrice productValueWithoutDiscount;
	
	private String tags;
	
	@Constructor
	public ProductSimplifiedSearchResultPubEntity(){
	}

	public ProductSimplifiedSearchResultPubEntity(Product e, Locale locale){
		super(e, locale);
		this.thumbnail = e.getPublicThumb();
		this.hasDiscount = e.hasDiscount();
		this.productValue = e.getProductValue();
		this.productValueWithoutDiscount = e.getProductValue(false);
		this.value = e.getValueString(locale);
		this.valueWithoutDiscount = e.getValueString(locale, false);
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueWithoutDiscount() {
		return valueWithoutDiscount;
	}

	public void setValueWithoutDiscount(String valueWithoutDiscount) {
		this.valueWithoutDiscount = valueWithoutDiscount;
	}

	public Boolean getHasDiscount() {
		return hasDiscount;
	}

	public void setHasDiscount(Boolean hasDiscount) {
		this.hasDiscount = hasDiscount;
	}

	public ProductPrice getProductValue() {
		return productValue;
	}

	public void setProductValue(ProductPrice productValue) {
		this.productValue = productValue;
	}

	public ProductPrice getProductValueWithoutDiscount() {
		return productValueWithoutDiscount;
	}

	public void setProductValueWithoutDiscount(ProductPrice productValueWithoutDiscount) {
		this.productValueWithoutDiscount = productValueWithoutDiscount;
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
