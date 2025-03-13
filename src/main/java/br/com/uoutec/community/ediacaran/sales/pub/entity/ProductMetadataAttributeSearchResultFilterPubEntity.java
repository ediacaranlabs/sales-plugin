package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;

import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeSearchResultOptionFilter;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductMetadataAttributeSearchResultFilterPubEntity extends AbstractPubEntity<ProductMetadataAttributeSearchResultFilter>{

	private static final long serialVersionUID = -8396154286650451055L;

	@Transient
	private int productMetadataAttribute;
	
	private String protectedID;
	
	private String title;
	
	private Boolean multiselect;
	
	private List<ProductMetadataAttributeSearchResultOptionFilterPubEntity> options;
	
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
		o.setProductMetadataAttribute(this.productMetadataAttribute);
		o.setTitle(this.title);
		
		if(this.options != null) {
			List<ProductMetadataAttributeSearchResultOptionFilter> list = new ArrayList<>();
			for(ProductMetadataAttributeSearchResultOptionFilterPubEntity e: this.options) {
				list.add(e.rebuild(false, true, true));
			}
			o.setOptions(list);
		}
	}

	public int getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(int productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
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
