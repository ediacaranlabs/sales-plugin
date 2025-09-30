package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ProductCategoryFilter implements Serializable {

	private static final long serialVersionUID = -1447420279267594109L;

	private String title;
	
	private int productCategory;

	private List<ProductCategoryFilter> subcategories;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(int productCategory) {
		this.productCategory = productCategory;
	}

	public List<ProductCategoryFilter> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<ProductCategoryFilter> subcategories) {
		this.subcategories = subcategories;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productCategory);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductCategoryFilter other = (ProductCategoryFilter) obj;
		return productCategory == other.productCategory;
	}
	
}
