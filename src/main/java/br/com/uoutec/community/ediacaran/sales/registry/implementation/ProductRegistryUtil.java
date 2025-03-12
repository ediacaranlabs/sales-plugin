package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.Map;
import java.util.Map.Entry;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValue;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.ProductUtil;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductRegistryUtil {

	public static void validate(Product product, ProductMetadataEntityAccess productMetadataEntityAccess, Class<?> ... groups) throws ValidationException, EntityAccessException {
		ValidatorBean.validate(product, groups);
		
		ProductMetadata metadata = productMetadataEntityAccess.findById(product.getMetadata());
		
		Map<String,ProductAttributeValue> attrs = product.getAttributes();
		
		if(attrs != null) {
			Map<String, ProductMetadataAttribute> md = metadata.getAttributes();
			for(Entry<String, ProductAttributeValue> e: attrs.entrySet()) {
				ProductMetadataAttribute pa = md.get(e.getKey());
				
				if(pa == null) {
					continue;
				}
				
				pa.validate(e.getValue().getValue());
			}
		}
	}

	public static Product getActualProduct(Product product, ProductEntityAccess entityAccess, ObjectsTemplateManager objectsManager) throws EntityAccessException {
		return getProduct(product.getId(), entityAccess, objectsManager);
	}

	public static Product getProduct(int id, ProductEntityAccess entityAccess, ObjectsTemplateManager objectsManager) throws EntityAccessException {
		if(id > 0) {
			return entityAccess.findById(id);
		}
		return null;
	}
	
	public static void persistProductImage(Product product, ObjectsTemplateManager objectsManager) {
		if(product.getThumb() != null) {
			String path = ProductUtil.getThumbPath(product);
			objectsManager.registerObject(path, null, product.getThumb());
			product.setThumb((Path)objectsManager.getObject(path));
		}
	}

	public static void loadProductImage(Product product, ObjectsTemplateManager objectsManager) {
		String path = ProductUtil.getThumbPath(product);
		product.setThumb((Path)objectsManager.getObject(path));
	}

	public static void loadProductImage(ProductImage image, ObjectsTemplateManager objectsManager) {
		String path = ProductUtil.getThumbPath(image);
		image.setImage((Path)objectsManager.getObject(path));
	}
	
	public static void deleteProductImage(Product product, ObjectsTemplateManager objectsManager) {
		
		if(product == null) {
			return;
		}
		
		String path = ProductUtil.getThumbPath(product);
		objectsManager.unregisterObject(path, null);
	}
	
	public static void saveOrUpdate(Product product, ProductEntityAccess entityAccess) throws EntityAccessException {
		if(product.getId() > 0){
			ProductRegistryUtil.update(product, entityAccess);	
		}
		else{
			ProductRegistryUtil.save(product, entityAccess);	
		}
	}

	public static void sendToRepository(ProductEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.flush();
	}

	public static void save(Product product, ProductEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.save(product);	
	}

	public static void update(Product product, ProductEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.update(product);	
	}
	
	public static void delete(Product product, ProductEntityAccess entityAccess) throws EntityAccessException {
		if(product != null) {
			entityAccess.delete(product);
		}
	}
	
	public static void updateIndex(Product product, ProductIndexEntityAccess entityAccess) throws EntityAccessException {
		
		if(entityAccess.findById(product.getId()) != null) {
			entityAccess.update(product);
		}
		else {
			entityAccess.save(product);
		}
		
	}

	public static void deleteIndex(Product product, ProductIndexEntityAccess entityAccess) throws EntityAccessException {
		
		if(entityAccess.findById(product.getId()) != null) {
			entityAccess.delete(product);
		}
		
	}
	
}
