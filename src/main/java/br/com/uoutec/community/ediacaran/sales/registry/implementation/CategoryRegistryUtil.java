package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;

public class CategoryRegistryUtil {

	public static String getPublicThumbPath(ProductCategory category) {
		return category.getThumb() == null? null : ProductCategoryUtil.getPublicThumbPath(category)  + ".png";
	}
	
	public static void loadImage(ProductCategory category, ObjectsTemplateManager objectsManager) {
		String path = ProductCategoryUtil.getThumbPath(category);
		if(path != null) {
			category.setThumb((Path)objectsManager.getObject(path));
		}
	}
	
}
