package br.com.uoutec.community.ediacaran.sales.registry;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResult;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductMetadataEntityAccess;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PluginType;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductMetadataRegistryUtil {

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	public static ProductMetadata getActual(ProductMetadata entity, ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		return get(entity.getId(), entityAccess);
	}

	public static void validate(ProductMetadata entity) throws ValidationException, EntityAccessException {
		if(entity.getId() <= 0){
			ValidatorBean.validate(entity, saveValidations);
		}
		else {
			ValidatorBean.validate(entity, updateValidations);
		}
	}
	
	/* - ------- ---------------------------- */

	public static List<ProductMetadata> getAllProductMetadata(ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		return entityAccess.getAll();
	}
	
	public static ProductMetadataSearchResult search(ProductMetadataSearch value, ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		int page = value.getPage() == null? 0 : value.getPage().intValue();
		int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
		
		int firstResult = (page - 1)*maxItens;
		int maxResults = maxItens + 1;
		List<ProductMetadata> list = entityAccess.search(value, firstResult, maxResults);
		return new ProductMetadataSearchResult(list.size() > maxItens, -1, page, list.size() > maxItens? list.subList(0, maxItens -1) : list);
	}
	
	public static void save(ProductMetadata entity, ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.save(entity);	
	}

	public static void update(ProductMetadata entity, ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.update(entity);	
	}
	
	public static void delete(ProductMetadata entity, ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		if(entity != null) {
			entityAccess.delete(entity);
		}
	}

	public static ProductMetadata get(Serializable id, ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		if(id != null) {
			return entityAccess.findById(id);
		}
		return null;
	}

	public static void saveOrUpdate(ProductMetadata entity, ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		if(entity.getId() <= 0){
			save(entity, entityAccess);	
		}
		else{
			update(entity, entityAccess);	
		}
	}

	public static void sendToRepository(ProductMetadataEntityAccess entityAccess) throws EntityAccessException {
		entityAccess.flush();
	}
	
	private static volatile ProductMetadata defaultProductMetadata = null;
	
	private static volatile long nextLoadedDefaultProductMetadata = -1;
	
	static void markToReloadIfDefaultProductMetadata(ProductMetadata entity) {
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			Integer id = pluginType.getConfiguration().getInt("default_product_metadata");
			if(id != null && id == entity.getId()) {
				nextLoadedDefaultProductMetadata = -1;	
			}
		}
		catch(Throwable ex) {
			//supress
		}
	}

	static ProductMetadata getDefaultProductMetadata(ProductMetadataEntityAccess entityAccess) throws ProductRegistryException{
		
		long currentTime = SystemProperties.currentTimeMillis();
		
		if(currentTime > nextLoadedDefaultProductMetadata) {
			loadDefaultProductMetadata(entityAccess);
		}
		
		return defaultProductMetadata;
	}
	
	static synchronized void loadDefaultProductMetadata(ProductMetadataEntityAccess entityAccess) throws ProductRegistryException{
		
		long currentTime = SystemProperties.currentTimeMillis();
		
		if(currentTime <= nextLoadedDefaultProductMetadata) {
			return;
		}
		
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			Integer id = pluginType.getConfiguration().getInt("default_product_metadata");
			if(id != null) {
				defaultProductMetadata = entityAccess.findById(id);
			}
			nextLoadedDefaultProductMetadata = currentTime + 10000; // TODO: update
		}
		catch(Throwable ex) {
			throw new ProductRegistryException(ex);
		}
		
	}
	
}
