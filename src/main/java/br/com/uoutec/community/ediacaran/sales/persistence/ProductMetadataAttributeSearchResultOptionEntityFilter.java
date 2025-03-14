package br.com.uoutec.community.ediacaran.sales.persistence;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultOptionFilter;

public class ProductMetadataAttributeSearchResultOptionEntityFilter {

	private ProductMetadataAttributeOption option;

	public ProductMetadataAttributeOption getOption() {
		return option;
	}

	public void setOption(ProductMetadataAttributeOption option) {
		this.option = option;
	}

	public ProductAttributeSearchResultOptionFilter toEntity() {
		ProductAttributeSearchResultOptionFilter e = new ProductAttributeSearchResultOptionFilter();
		e.setDescription(option.getDescription());
		e.setOptionId(option.getId());
		e.setValue(option.getValue());
		e.setType(option.getValueType());
		return e;
	}
	
}
