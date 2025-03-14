package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResultFilter;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductSearchResultFilterPubEntity 
	extends AbstractPubEntity<ProductSearchResultFilter> {

	private static final long serialVersionUID = -6012143443265870176L;

	@Transient
	private Integer id;

	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	private String title;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductAttributeSearchResultFilterPubEntity> filters;

	@Constructor
	public ProductSearchResultFilterPubEntity() {
	}
	
	public ProductSearchResultFilterPubEntity(ProductSearchResultFilter e, Locale locale) {
		this.id = e.getProductMetadata();
		this.protectedID = SecretUtil.toProtectedID(String.valueOf(e.getProductMetadata()));
		this.title = e.getTitle();
		
		if(e.getFilters() != null) {
			this.filters = new ArrayList<>();
			for(ProductAttributeSearchResultFilter x: e.getFilters()) {
				this.filters.add(new ProductAttributeSearchResultFilterPubEntity(x, locale));
			}
		}
	}
	
	@Override
	protected void preRebuild(ProductSearchResultFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(ProductSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductSearchResultFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductSearchResultFilter createNewInstance() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void copyTo(ProductSearchResultFilter o, boolean reload, boolean override, boolean validate) throws Throwable {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ProductAttributeSearchResultFilterPubEntity> getFilters() {
		return filters;
	}

	public void setFilters(List<ProductAttributeSearchResultFilterPubEntity> filters) {
		this.filters = filters;
	}
	
}
