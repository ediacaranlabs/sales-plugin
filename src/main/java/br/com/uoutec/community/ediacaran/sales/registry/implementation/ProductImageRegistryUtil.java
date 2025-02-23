package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductImageEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ProductUtil;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductImageRegistryUtil {

	public static List<ProductImage> getImages(Product product, ProductImageEntityAccess entityAccess) throws EntityAccessException {
		return entityAccess.getImagesByProduct(product);
	}
	
	public static void persistImage(ProductImage entity, ObjectsTemplateManager objectsManager) {
		if(entity.getImage() != null) {
			String path = ProductUtil.getThumbPath(entity);
			objectsManager.registerObject(path, null, entity.getImage());
			entity.setImage((Path)objectsManager.getObject(path));
		}
	}

	public static void deleteImage(ProductImage entity, ObjectsTemplateManager objectsManager) {
		
		if(entity == null) {
			return;
		}
		
		String path = ProductUtil.getThumbPath(entity);
		objectsManager.unregisterObject(path, null);
	}

	/* - ------- ---------------------------- */
	
	public static ProductImage getActual(ProductImage productImage, ProductImageEntityAccess imageEntityAccess) throws EntityAccessException {
		return get(productImage.getId(), imageEntityAccess);
	}

	public static void validate(ProductImage product, Class<?> ... groups) throws ValidationException, EntityAccessException {
		ValidatorBean.validate(product, groups);
	}
	
	/* - ------- ---------------------------- */
	
	public static void save(ProductImage entity, ProductImageEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.save(entity);	
	}

	public static void update(ProductImage entity, ProductImageEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.update(entity);	
	}
	
	public static void delete(ProductImage entity, ProductImageEntityAccess entityAccess) throws EntityAccessException {
		if(entity != null) {
			entityAccess.delete(entity);
		}
	}

	public static ProductImage get(Serializable id, ProductImageEntityAccess entityAccess) throws EntityAccessException {
		if(id != null) {
			return entityAccess.findById(id);
		}
		return null;
	}

	public static void saveOrUpdate(ProductImage entity, ProductImageEntityAccess entityAccess) throws EntityAccessException {
		if(entity.getId() != null){
			update(entity, entityAccess);	
		}
		else{
			save(entity, entityAccess);	
		}
	}

	public static void sendToRepository(ProductImageEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.flush();
	}
	
}
