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
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultOptionFilter;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductAttributeSearchResultFilterPubEntity 
	extends AbstractPubEntity<ProductAttributeSearchResultFilter>{

	private static final long serialVersionUID = -8396154286650451055L;

	@Transient
	private int id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	private String title;
	
	private Boolean multiselect;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductAttributeSearchResultOptionFilterPubEntity> options;
	
	@Constructor
	public ProductAttributeSearchResultFilterPubEntity() {
	}
	
	public ProductAttributeSearchResultFilterPubEntity(ProductAttributeSearchResultFilter e, Locale locale) {
		this.id = e.getProductMetadataAttribute() <= 0? null : e.getProductMetadataAttribute();
		this.protectedID = SecretUtil.toProtectedID(String.valueOf(e.getProductMetadataAttribute()));
		this.multiselect = e.isMultiselect();
		this.title = e.getTitle();
		
		if(e.getOptions() != null) {
			this.options = new ArrayList<>();
			for(ProductAttributeSearchResultOptionFilter x: e.getOptions()) {
				this.options.add(new ProductAttributeSearchResultOptionFilterPubEntity(x, locale));
			}
		}
	}
	
	@Override
	protected void preRebuild(ProductAttributeSearchResultFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(ProductAttributeSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductAttributeSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductAttributeSearchResultFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductAttributeSearchResultFilter createNewInstance() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void copyTo(ProductAttributeSearchResultFilter o, boolean reload, boolean override, boolean validate) throws Throwable {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
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

	public boolean isMultiselect() {
		return multiselect;
	}

	public void setMultiselect(boolean multiselect) {
		this.multiselect = multiselect;
	}

	public List<ProductAttributeSearchResultOptionFilterPubEntity> getOptions() {
		return options;
	}

	public void setOptions(List<ProductAttributeSearchResultOptionFilterPubEntity> options) {
		this.options = options;
	}
	
}
