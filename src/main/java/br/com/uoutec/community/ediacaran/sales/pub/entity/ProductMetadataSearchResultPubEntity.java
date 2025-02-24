package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;

public class ProductMetadataSearchResultPubEntity extends ProductMetadataPubEntity{

	private static final long serialVersionUID = -5240855789107084675L;

	@Constructor
	public ProductMetadataSearchResultPubEntity(){
	}

	public ProductMetadataSearchResultPubEntity(ProductMetadata e, Locale locale){
		super(e, locale);
	}
	
}
