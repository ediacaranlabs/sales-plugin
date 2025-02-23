package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataAttributeEntityAccess;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductMetadataAttributeRegistryUtil {

	public static ProductMetadataAttribute getActual(ProductMetadataAttribute entity, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		return get(entity.getId(), entityAccess);
	}

	public static List<ProductMetadataAttribute> getByParent(ProductMetadata parent, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		return entityAccess.getByProductMetadata(parent);
	}
	
	public static void validate(ProductMetadataAttribute entity, Class<?> ... groups) throws ValidationException, EntityAccessException {
		ValidatorBean.validate(entity, groups);
	}
	
	/* - ------- ---------------------------- */
	
	public static void save(ProductMetadataAttribute entity, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.save(entity);	
	}

	public static void update(ProductMetadataAttribute entity, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.update(entity);	
	}
	
	public static void delete(ProductMetadataAttribute entity, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		if(entity != null) {
			entityAccess.delete(entity);
		}
	}

	public static ProductMetadataAttribute get(Serializable id, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		if(id != null) {
			return entityAccess.findById(id);
		}
		return null;
	}

	public static void saveOrUpdate(ProductMetadataAttribute entity, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		if(entity.getId() <= 0){
			update(entity, entityAccess);	
		}
		else{
			save(entity, entityAccess);	
		}
	}

	public static void sendToRepository(ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.flush();
	}
	
}
