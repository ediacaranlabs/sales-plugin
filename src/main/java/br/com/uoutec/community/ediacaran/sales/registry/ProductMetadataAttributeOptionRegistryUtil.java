package br.com.uoutec.community.ediacaran.sales.registry;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataAttributeOptionEntityAccess;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductMetadataAttributeOptionRegistryUtil {

	public static ProductMetadataAttributeOption getActual(ProductMetadataAttributeOption entity, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		return get(entity.getId(), entityAccess);
	}

	public static List<ProductMetadataAttributeOption> getByParent(ProductMetadataAttribute parent, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		return entityAccess.getByProductMetadataAttribute(parent);
	}
	
	public static void validate(ProductMetadataAttributeOption entity, Class<?> ... groups) throws ValidationException, EntityAccessException {
		ValidatorBean.validate(entity, groups);
	}
	
	/* - ------- ---------------------------- */
	
	public static void save(ProductMetadataAttributeOption entity, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.save(entity);	
	}

	public static void update(ProductMetadataAttributeOption entity, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.update(entity);	
	}
	
	public static void delete(ProductMetadataAttributeOption entity, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		if(entity != null) {
			entityAccess.delete(entity);
		}
	}

	public static ProductMetadataAttributeOption get(Serializable id, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		if(id != null) {
			return entityAccess.findById(id);
		}
		return null;
	}

	public static void saveOrUpdate(ProductMetadataAttributeOption entity, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		if(entity.getId() <= 0){
			update(entity, entityAccess);	
		}
		else{
			save(entity, entityAccess);	
		}
	}

	public static void sendToRepository(ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.flush();
	}
	
}
