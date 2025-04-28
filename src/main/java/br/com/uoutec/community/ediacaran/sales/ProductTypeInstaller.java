package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public class ProductTypeInstaller {

	public void install() throws Throwable{
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		productTypeRegistry.registerProductType(
				new ProductType(
						"simple-product", 
						"product_types.name", 
						MessageBundle.toPackageID(ProductTypeInstaller.class),
						99, 
						EntityContextPlugin.getEntity(SimpleProductTypeHandler.class), 
						EntityContextPlugin.getEntity(SimpleProductTypeViewHandler.class)
				)
		);
	}
	
	public void uninstall() throws Throwable{
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		productTypeRegistry.removeProductType("simple-product");
	}
	
}
