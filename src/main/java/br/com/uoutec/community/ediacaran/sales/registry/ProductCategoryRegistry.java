package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategoryResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategorySearch;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ProductCategoryRegistry extends PublicBean {

	void registerProductCategory(ProductCategory entity) throws  ProductCategoryRegistryException;
	
	void removeProductCategory(ProductCategory entity) throws ProductCategoryRegistryException;
	
	ProductCategory findById(int id) throws ProductCategoryRegistryException;

	List<ProductCategory> findByParent(ProductCategory parent) throws ProductCategoryRegistryException;
	
	ProductCategoryResultSearch searchProductCategory(ProductCategorySearch value) throws ProductCategoryRegistryException;
	
}
