package br.com.uoutec.community.ediacaran.sales.pub.entity;

import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeSearchResultOptionFilter;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductMetadataAttributeSearchResultOptionFilterPubEntity 
	extends AbstractPubEntity<ProductMetadataAttributeSearchResultOptionFilter>{

	private static final long serialVersionUID = 156435810866580706L;

	@Transient
	private String protectedID;
	
	private Integer optionId;
	
	private Object value;
	
	private String description;

	private ProductAttributeValueType type;
	
	@Override
	protected boolean isEqualId(ProductMetadataAttributeSearchResultOptionFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductMetadataAttributeSearchResultOptionFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductMetadataAttributeSearchResultOptionFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductMetadataAttributeSearchResultOptionFilter createNewInstance() throws Throwable {
		return new ProductMetadataAttributeSearchResultOptionFilter();
	}

	@Override
	protected void copyTo(ProductMetadataAttributeSearchResultOptionFilter o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setOptionId(this.optionId == null? 0 : this.optionId.intValue());
		o.setType(this.type);
		o.setValue(this.value);
	}
	
}
