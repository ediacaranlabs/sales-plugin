package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResultFilter;

public class ProductMetadataSearchResultEntityFilter {

	private ProductMetadata productMetadata;
	
	private Map<Integer,ProductMetadataAttributeSearchResultEntityFilter> filters;

	public ProductMetadata getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(ProductMetadata productMetadata) {
		this.productMetadata = productMetadata;
	}

	public Map<Integer, ProductMetadataAttributeSearchResultEntityFilter> getFilters() {
		return filters;
	}

	public void setFilters(Map<Integer, ProductMetadataAttributeSearchResultEntityFilter> filters) {
		this.filters = filters;
	}

	public ProductSearchResultFilter toEntity() {
		
		ProductSearchResultFilter e = new ProductSearchResultFilter();
		e.setProductMetadata(productMetadata.getId());
		e.setTitle(productMetadata.getName());
		
		List<ProductAttributeSearchResultFilter> filters = new ArrayList<>();
		
		Map<String, ProductMetadataAttribute> attributes = productMetadata.getAttributes();
		
		for(Entry<String, ProductMetadataAttribute> x: attributes.entrySet()) {
			
			if(this.filters == null || !this.filters.containsKey(x.getValue().getId())) {
				ProductMetadataAttributeSearchResultEntityFilter tmp = new ProductMetadataAttributeSearchResultEntityFilter();
				tmp.setProductMetadataAttribute(x.getValue());
				tmp.setOptions(new HashMap<>());
				
				if(x.getValue().getOptions() != null && !x.getValue().getOptions().isEmpty()) {
					for(ProductMetadataAttributeOption o: x.getValue().getOptions()) {
						tmp.getOptions().put(o.getValue(), new ProductMetadataAttributeSearchResultOptionEntityFilter(o));
					}
					filters.add(tmp.toEntity());
				}
				
			}
			
		}
		
		if(this.filters != null) {
			for(ProductMetadataAttributeSearchResultEntityFilter f: this.filters.values()) {
				filters.add(f.toEntity());
			}
			e.setFilters(filters);
		}
		
		return e;
	}
	
}
