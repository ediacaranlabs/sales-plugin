package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultFilter;
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
		
		if(filters != null) {
			List<ProductAttributeSearchResultFilter> filters = new ArrayList<>();
			for(ProductMetadataAttributeSearchResultEntityFilter f: this.filters.values()) {
				filters.add(f.toEntity());
			}
			e.setFilters(filters);
		}
		
		return e;
	}
	
}
