package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.Map;
import java.util.Map.Entry;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataEntityAccess;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductRegistryUtil {

	public static void validate(Product product, ProductMetadataEntityAccess productMetadataEntityAccess, Class<?> ... groups) throws ValidationException, EntityAccessException {
		ValidatorBean.validate(product, groups);
		
		ProductMetadata metadata = productMetadataEntityAccess.findById(product.getMetadata());
		
		Map<String,String> attrs = product.getAttributes();
		
		if(attrs != null) {
			Map<String, ProductAttribute> md = metadata.getAttributes();
			for(Entry<String, String> e: attrs.entrySet()) {
				ProductAttribute pa = md.get(e.getKey());
				
				if(pa == null) {
					continue;
				}
				
				pa.validate(e.getValue());
			}
		}
	}
	
}
