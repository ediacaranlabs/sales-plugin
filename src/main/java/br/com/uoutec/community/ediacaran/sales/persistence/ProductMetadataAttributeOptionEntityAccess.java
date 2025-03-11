package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductMetadataAttributeOptionEntityAccess {

	void save(ProductMetadataAttributeOption entity) throws EntityAccessException;
	
	void update(ProductMetadataAttributeOption entity) throws EntityAccessException;

	void delete(ProductMetadataAttributeOption entity) throws EntityAccessException;

	ProductMetadataAttributeOption findById(Serializable id) throws EntityAccessException;

	ProductMetadataAttributeOption findByValue(Object value, ProductMetadataAttribute parent) throws EntityAccessException;
	
	List<ProductMetadataAttributeOption> getByProductMetadataAttribute(ProductMetadataAttribute productMetadataAttribute) throws EntityAccessException;
	
	void flush();
	
}
