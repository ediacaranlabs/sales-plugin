package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchAttributeFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchFilter;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntityType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueIndexEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductIndexEntity;

public class ProductIndexUtil {

	public static Predicate addFilters(Set<ProductSearchFilter> productFilters, 
			Join<ProductIndexEntity, ProductAttributeValueIndexEntity> productIndex, CriteriaBuilder builder) {
		
		List<Predicate> attributesFilters = new ArrayList<>();
		
		for(ProductSearchFilter filter: productFilters) {
			Predicate predicate = addAttributeFilters(filter.getAttributeFilters(),	productIndex, builder);		
			
			if(predicate != null) {
				attributesFilters.add(predicate);
			}
		}
		
		return attributesFilters.isEmpty()? null : builder.or(attributesFilters.stream().toArray(Predicate[]::new));
	}
	
	public static Predicate addAttributeFilters(Set<ProductSearchAttributeFilter> productAttributeFilters, 
			Join<ProductIndexEntity, ProductAttributeValueIndexEntity> productIndex, CriteriaBuilder builder) {
		
		List<Predicate> attributesFilters = new ArrayList<>();
		
		for(ProductSearchAttributeFilter productAttributeFilter: productAttributeFilters) {
			Predicate predicate = addProductAttributeFilter(productAttributeFilter, productIndex, builder);
			
			if(predicate != null) {
				attributesFilters.add(predicate);
			}
			
		}
		
		return attributesFilters.isEmpty()? null : builder.and(attributesFilters.stream().toArray(Predicate[]::new));
	}

	public static Predicate addProductAttributeFilter(ProductSearchAttributeFilter productAttributeFilter, 
			Join<ProductIndexEntity, ProductAttributeValueIndexEntity> productIndex, CriteriaBuilder builder) {
		
		Set<Object> values = productAttributeFilter.getValue();
		
		if(values != null && !values.isEmpty()) {
			ProductMetadataAttribute productMetadataAttribute = productAttributeFilter.getProductMetadataAttribute();
		    Join<ProductAttributeValueIndexEntity, ProductIndexEntity> attribute = productIndex.joinList("attributes");	
		    return addParameterFilter(values, productMetadataAttribute, attribute, builder);
		}

		return null;
	}
	
	public static Predicate addParameterFilter(Set<Object> values, ProductMetadataAttribute productMetadataAttribute,
			From<ProductAttributeValueIndexEntity, ProductIndexEntity> from, CriteriaBuilder builder) {
		
		if(values.size() == 1 || productMetadataAttribute.isMultiselect()) {
			return addMultiParameterFilter(values, productMetadataAttribute, from, builder);
		}
		else {
			return addSimpleParameterFilter(values, productMetadataAttribute, from, builder);
		}
		
	}
	
	public static Predicate addSimpleParameterFilter(Set<Object> values, ProductMetadataAttribute productMetadataAttribute,
			From<ProductAttributeValueIndexEntity, ProductIndexEntity> from, CriteriaBuilder builder) {
		
		ProductAttributeValueEntityType entityType = ProductAttributeValueEntityType.valueOf(productMetadataAttribute.getValueType().name());
		
		if(entityType == ProductAttributeValueEntityType.TEXT) {
			return
    				builder.and(
	    				builder.equal(from.get("id").get("productMetadataAttributeID"), productMetadataAttribute.getId()),
	    				builder.equal(from.get("id").get("value"), entityType.toValue(values.iterator().next()))
					);
		}
		else {
			return
    				builder.and(
	    				builder.equal(from.get("id").get("productMetadataAttributeID"), productMetadataAttribute.getId()),
	    				builder.equal(from.get("number"), entityType.toValue(values.iterator().next()))
					);
		}
	}
	
	public static Predicate addMultiParameterFilter(Set<Object> values, ProductMetadataAttribute productMetadataAttribute,
			From<ProductAttributeValueIndexEntity, ProductIndexEntity> from, CriteriaBuilder builder) {
		
		ProductAttributeValueEntityType entityType = ProductAttributeValueEntityType.valueOf(productMetadataAttribute.getValueType().name());
		
		In<Object> optionsIn;
		
		if(entityType == ProductAttributeValueEntityType.TEXT) {
			optionsIn = builder.in(from.get("id").get("value"));
		}
		else {
			optionsIn = builder.in(from.get("number"));
		}
		
		for(Object v: values) {
			optionsIn.value(entityType.toValue(v));
			
		}

		return
			builder.and(
				builder.equal(from.get("id").get("productMetadataAttributeID"), productMetadataAttribute.getId()),
				optionsIn
			);
		
	}
	
}
