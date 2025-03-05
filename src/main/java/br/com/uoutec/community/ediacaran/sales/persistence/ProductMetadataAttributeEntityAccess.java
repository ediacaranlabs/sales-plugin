package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductMetadataAttributeEntityAccess {

	void save(ProductMetadataAttribute entity) throws EntityAccessException;
	
	void update(ProductMetadataAttribute entity) throws EntityAccessException;

	void delete(ProductMetadataAttribute entity) throws EntityAccessException;

	ProductMetadataAttribute findById(Serializable id) throws EntityAccessException;

	ProductMetadataAttribute findByCode(String code, ProductMetadata parent) throws EntityAccessException;
	
	List<ProductMetadataAttribute> getByProductMetadata(ProductMetadata productMetadata) throws EntityAccessException;
	
	void flush();
	
}
