package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultOptionFilter;

public class ProductMetadataAttributeSearchResultEntityFilter {

	private ProductMetadataAttribute productMetadataAttribute;
	
	private Map<Object,ProductMetadataAttributeSearchResultOptionEntityFilter> options;

	public ProductMetadataAttribute getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(ProductMetadataAttribute productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
	}

	public Map<Object, ProductMetadataAttributeSearchResultOptionEntityFilter> getOptions() {
		return options;
	}

	public void setOptions(Map<Object, ProductMetadataAttributeSearchResultOptionEntityFilter> options) {
		this.options = options;
	}


	public ProductAttributeSearchResultFilter toEntity() {
		ProductAttributeSearchResultFilter e = new ProductAttributeSearchResultFilter();
		e.setMultiselect(productMetadataAttribute.getType() == ProductAttributeType.MULTISELECT || productMetadataAttribute.getType() == ProductAttributeType.MULTISELECT_LIST);
		e.setProductMetadataAttribute(productMetadataAttribute.getId());
		e.setTitle(productMetadataAttribute.getName());
		
		if(productMetadataAttribute.getOptions() != null) {
			List<ProductAttributeSearchResultOptionFilter> list = new ArrayList<>();
			for(ProductMetadataAttributeSearchResultOptionEntityFilter o: options.values()) {
				list.add(o.toEntity());
			}
			e.setOptions(list);
		}
		return e;
	}
	
}
