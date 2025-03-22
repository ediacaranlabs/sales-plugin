package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultOptionFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;

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

	public boolean isMultiselect() {
		return productMetadataAttribute.getType() == ProductAttributeType.MULTISELECT || productMetadataAttribute.getType() == ProductAttributeType.MULTISELECT_LIST;
	}
	
	public ProductAttributeSearchResultFilter toEntity() {
		ProductAttributeSearchResultFilter e = new ProductAttributeSearchResultFilter();
		e.setMultiselect(this.isMultiselect());
		e.setProductMetadataAttribute(productMetadataAttribute.getId());
		e.setTitle(productMetadataAttribute.getName());
		
		List<ProductAttributeSearchResultOptionFilter> list = new ArrayList<>();

		/*
		List<ProductMetadataAttributeOption> optList = productMetadataAttribute.getOptions();
		
		if(optList != null) {
			for(ProductMetadataAttributeOption o: optList) {
				if(options == null || !options.containsKey(o.getValue())) {
					ProductMetadataAttributeSearchResultOptionEntityFilter tmp = new ProductMetadataAttributeSearchResultOptionEntityFilter(o);
					tmp.setSelected(false);
					list.add(tmp.toEntity());
				}
			}
		}
		*/
		
		if(options != null) {
			
			for(ProductMetadataAttributeSearchResultOptionEntityFilter o: options.values()) {
				list.add(o.toEntity());
			}
			
			e.setOptions(list);
		}
		
		return e;
	}
	
}
