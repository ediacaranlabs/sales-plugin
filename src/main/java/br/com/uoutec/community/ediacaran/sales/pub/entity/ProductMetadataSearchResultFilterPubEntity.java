package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResultFilter;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductMetadataSearchResultFilterPubEntity extends AbstractPubEntity<ProductMetadataSearchResultFilter> {

	private static final long serialVersionUID = -6012143443265870176L;

	@Transient
	private Integer id;

	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	private String title;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductMetadataAttributeSearchResultFilterPubEntity> filters;

	@Constructor
	public ProductMetadataSearchResultFilterPubEntity() {
	}
	
	public ProductMetadataSearchResultFilterPubEntity(ProductMetadataSearchResultFilter e, Locale locale) {
		this.id = e.getProductMetadata();
		this.protectedID = SecretUtil.toProtectedID(String.valueOf(e.getProductMetadata()));
		this.title = e.getTitle();
		
		if(e.getFilters() != null) {
			this.filters = new ArrayList<>();
			for(ProductMetadataAttributeSearchResultFilter x: e.getFilters()) {
				this.filters.add(new ProductMetadataAttributeSearchResultFilterPubEntity(x, locale));
			}
		}
	}
	
	@Override
	protected void preRebuild(ProductMetadataSearchResultFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(ProductMetadataSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductMetadataSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductMetadataSearchResultFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductMetadataSearchResultFilter createNewInstance() throws Throwable {
		return new ProductMetadataSearchResultFilter();
	}

	@Override
	protected void copyTo(ProductMetadataSearchResultFilter o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setProductMetadata(this.id);
		o.setTitle(this.title);
		
		if(this.filters != null) {
			List<ProductMetadataAttributeSearchResultFilter> list = new ArrayList<>();
			for(ProductMetadataAttributeSearchResultFilterPubEntity e: this.filters) {
				list.add(e.rebuild(false, true, true));
			}
			o.setFilters(list);
		}
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

	public List<ProductMetadataAttributeSearchResultFilterPubEntity> getFilters() {
		return filters;
	}

	public void setFilters(List<ProductMetadataAttributeSearchResultFilterPubEntity> filters) {
		this.filters = filters;
	}
	
}
