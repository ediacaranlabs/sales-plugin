package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategoryFilter;

public class ProductCategorySearchResultEntityFilter {

	private Set<ProductCategory> categories;

	public ProductCategorySearchResultEntityFilter() {
		this.categories = new HashSet<>();		
	}
	
	public Set<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<ProductCategory> categories) {
		this.categories = categories;
	}

	public List<ProductCategoryFilter> toEntity() {
		
		Map<Integer, ProductCategoryFilter> map = new HashMap<>();
		Set<ProductCategoryFilter> result = new HashSet<>();
		
		for(ProductCategory pc: categories) {
			createProductCategoryFilter(pc, result, map);
		}
		
		List<ProductCategoryFilter> list = new ArrayList<>(result);
		Collections.sort(list, (a,b)->a.getTitle().compareTo(b.getTitle()));
		return list;
	}
	
	private void createProductCategoryFilter(ProductCategory productCategory, Set<ProductCategoryFilter> root, Map<Integer, ProductCategoryFilter> cache) {
		
		ProductCategoryFilter pcf = cache.get(productCategory.getId());
		
		if(pcf == null) {
			pcf = new ProductCategoryFilter();
			pcf.setProductCategory(productCategory.getId());
			pcf.setSubcategories(new ArrayList<>());
			pcf.setTitle(productCategory.getName());
			cache.put(productCategory.getId(), pcf);
		}
		
		if(productCategory.getParent2() != null) {
			createProductCategoryFilter(productCategory.getParent2(), root, cache);
			ProductCategoryFilter parent = cache.get(productCategory.getParent2().getId());
			parent.getSubcategories().add(pcf);
		}
		else
		if(productCategory.getParent1() != null){
			createProductCategoryFilter(productCategory.getParent1(), root, cache);
			ProductCategoryFilter parent = cache.get(productCategory.getParent1().getId());
			parent.getSubcategories().add(pcf);
		}
		else {
			root.add(pcf);
		}
	}
	
}
