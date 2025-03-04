package br.com.uoutec.community.ediacaran.sales.registry;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataAttributeOptionEntityAccess;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductMetadataAttributeOptionRegistryUtil {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	public static ProductMetadataAttributeOption getActual(ProductMetadataAttributeOption entity, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		return get(entity.getId(), entityAccess);
	}

	public static List<ProductMetadataAttributeOption> getByParent(ProductMetadataAttribute parent, ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		return entityAccess.getByProductMetadataAttribute(parent);
	}
	
	public static void validate(ProductMetadataAttributeOption entity) throws ValidationException, EntityAccessException {
		if(entity.getId() <= 0){
			ValidatorBean.validate(entity, saveValidations);
		}
		else {
			ValidatorBean.validate(entity, updateValidations);
		}
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
			save(entity, entityAccess);	
		}
		else{
			update(entity, entityAccess);	
		}
	}

	public static void sendToRepository(ProductMetadataAttributeOptionEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.flush();
	}
	
}
