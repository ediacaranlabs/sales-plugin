package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataAttributeEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.AttributeCodeDuplicatedProductRegistryException;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductMetadataAttributeRegistryUtil {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	public static ProductMetadataAttribute getActual(ProductMetadataAttribute entity, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		return get(entity.getId(), entityAccess);
	}

	public static List<ProductMetadataAttribute> getByParent(ProductMetadata parent, ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		return entityAccess.getByProductMetadata(parent);
	}

	public static void checkDuplicationCode(List<ProductMetadataAttribute> list, 
			ProductMetadata parent, ProductMetadataAttributeEntityAccess entityAccess) throws ValidationException, EntityAccessException, AttributeCodeDuplicatedProductRegistryException {
		
		Set<String> codes = new HashSet<>();
		
		for(ProductMetadataAttribute e: list) {
			
			String code = e.getCode().toLowerCase();
			
			if(codes.contains(code)) {
				throw new AttributeCodeDuplicatedProductRegistryException(code);
			}
			
			codes.add(code);
		}
		
		
	}
	
	public static void validate(ProductMetadataAttribute entity, ProductMetadata parent, ProductMetadataAttributeEntityAccess entityAccess) throws ValidationException, EntityAccessException, AttributeCodeDuplicatedProductRegistryException {
		
		if(parent != null && entity.getProductMetadata() != parent.getId()) {
			throw new ValidationException("invalid product metadata id: " + entity.getProductMetadata() + " != " + parent.getId());
		}
		
		if(entity.getId() <= 0){
			ValidatorBean.validate(entity, saveValidations);
		}
		else {
			ValidatorBean.validate(entity, updateValidations);
		}

		if(entityAccess != null) {
			ProductMetadataAttribute current = entityAccess.findByCode(entity.getCode(), parent);
			
			if(current != null && entity.getId() != current.getId()) {
				throw new AttributeCodeDuplicatedProductRegistryException(entity.getCode());
			}
		}
		
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
			save(entity, entityAccess);	
		}
		else{
			update(entity, entityAccess);	
		}
	}

	public static void sendToRepository(ProductMetadataAttributeEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.flush();
	}
	
}
