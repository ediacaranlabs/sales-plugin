package br.com.uoutec.community.ediacaran.junit.sales;

import java.math.BigDecimal;

import br.com.uoutec.community.ediacaran.junit.sales.entity.ProductMock;
import br.com.uoutec.community.ediacaran.sales.entity.PeriodType;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class SalesJUnitActions extends SalesJUnitConstants {

	public static void registerMockProductType() throws ProductTypeRegistryException {
		ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
		productTypeRegistry.registryProductType(new ProductType("MOCK", "Mock product", 0, EntityContextPlugin.getEntity(ProductTypeHandlerMock.class)));
	}

	public static void registerProductMock() throws ProductRegistryException {
		Product product = ProductMock.builder()
				.withName("Product A")
				.withCost(BigDecimal.ZERO)
				.withCurrency("USD")
				.withDescription("Product A description")
				.withPeriodType(PeriodType.UNDEFINED)
				.withProductType(null)
				.build();
		ProductRegistry productRegistry = EntityContextPlugin.getEntity(ProductRegistry.class);
		productRegistry.registryServicePlan(product);
	}
	
}
