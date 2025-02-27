package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.front.components.Image;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;

public class ProductMetadataSearchResultPubEntity extends ProductMetadataPubEntity {

	private static final long serialVersionUID = -5240855789107084675L;

	@Constructor
	public ProductMetadataSearchResultPubEntity(){
	}

	public ProductMetadataSearchResultPubEntity(ProductMetadata e, Locale locale){
		super(e, locale);
	}
	
	@Transient
	public void setAttributes(List<ProductMetadataAttributePubEntity> attributes) {
		super.setAttributes(attributes);
	}
	
	@Transient
	public void setThumbnail(Image thumbnail) {
		super.setThumbnail(thumbnail);
	}
	
}
