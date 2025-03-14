package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeSearchResultOptionFilter;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductMetadataAttributeSearchResultFilterPubEntity 
	extends AbstractPubEntity<ProductMetadataAttributeSearchResultFilter>{

	private static final long serialVersionUID = -8396154286650451055L;

	@Transient
	private int id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	private String title;
	
	private Boolean multiselect;
	
	private List<ProductMetadataAttributeSearchResultOptionFilterPubEntity> options;
	
	@Constructor
	public ProductMetadataAttributeSearchResultFilterPubEntity() {
	}
	
	public ProductMetadataAttributeSearchResultFilterPubEntity(ProductMetadataAttributeSearchResultFilter e, Locale locale) {
		this.id = e.getProductMetadataAttribute() <= 0? null : e.getProductMetadataAttribute();
		this.protectedID = SecretUtil.toProtectedID(String.valueOf(e.getProductMetadataAttribute()));
		this.multiselect = e.isMultiselect();
		this.title = e.getTitle();
		
		if(e.getOptions() != null) {
			this.options = new ArrayList<>();
			for(ProductMetadataAttributeSearchResultOptionFilter x: e.getOptions()) {
				this.options.add(new ProductMetadataAttributeSearchResultOptionFilterPubEntity(x, locale));
			}
		}
	}
	
	@Override
	protected void preRebuild(ProductMetadataAttributeSearchResultFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(ProductMetadataAttributeSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductMetadataAttributeSearchResultFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductMetadataAttributeSearchResultFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductMetadataAttributeSearchResultFilter createNewInstance() throws Throwable {
		return new ProductMetadataAttributeSearchResultFilter();
	}

	@Override
	protected void copyTo(ProductMetadataAttributeSearchResultFilter o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setMultiselect(this.multiselect == null? false : this.multiselect.booleanValue());
		o.setProductMetadataAttribute(this.id);
		o.setTitle(this.title);
		
		if(this.options != null) {
			List<ProductMetadataAttributeSearchResultOptionFilter> list = new ArrayList<>();
			for(ProductMetadataAttributeSearchResultOptionFilterPubEntity e: this.options) {
				list.add(e.rebuild(false, true, true));
			}
			o.setOptions(list);
		}
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

	public List<ProductMetadataAttributeSearchResultOptionFilterPubEntity> getOptions() {
		return options;
	}

	public void setOptions(List<ProductMetadataAttributeSearchResultOptionFilterPubEntity> options) {
		this.options = options;
	}
	
}
