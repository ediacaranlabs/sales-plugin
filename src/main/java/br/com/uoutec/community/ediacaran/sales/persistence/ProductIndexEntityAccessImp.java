package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntityID;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueIndexEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductIndexEntity;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductIndexEntityAccessImp 
	extends AbstractEntityAccess<Product, ProductIndexEntity>
	implements ProductIndexEntityAccess {

	public ProductIndexEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductIndexEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}

	public void save(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			entityManager.persist(pEntity);
			entityManager.flush();
			
			if(pEntity.getAttributes() != null) {
				for(ProductAttributeValueIndexEntity e: pEntity.getAttributes()) {
					entityManager.persist(e);
				}
			}
			
			entityManager.flush();
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void update(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			
			pEntity = (ProductIndexEntity)entityManager.merge(pEntity);
			
			if(pEntity.getAttributes() != null) {
				
				for(ProductAttributeValueIndexEntity e: pEntity.getAttributes()) {
					if(entityManager.find(ProductAttributeValueIndexEntity.class, e.getId()) == null) {
						entityManager.persist(e);
					}
					else {
						e = entityManager.merge(e);
					}
				}
				
				entityManager.flush();
				
				Map<ProductAttributeValueEntityID, ProductAttributeValueIndexEntity> attrsMap = 
						pEntity.getAttributes().stream()
								.collect(Collectors.toMap((e)->e.getId(), (e)->e));
				
				ProductIndexEntity actualEntity = entityManager.find(ProductIndexEntity.class, value.getId());
				
				for(ProductAttributeValueIndexEntity e: actualEntity.getAttributes()) {
					if(!attrsMap.containsKey(e.getId())) {
						entityManager.remove(e);
					}
				}
			}
			
			entityManager.flush();
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void delete(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = entityManager.find(ProductIndexEntity.class, value.getId());
			
			if(pEntity != null) {
				for(ProductAttributeValueIndexEntity e: pEntity.getAttributes()) {
					entityManager.remove(e);
				}
				
				entityManager.remove(pEntity);
			}
			
			entityManager.flush();
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	@Override
	protected ProductIndexEntity toPersistenceEntity(Product entity)
			throws Throwable {
		return new ProductIndexEntity(entity);
	}

	@Override
	protected Product toEntity(ProductIndexEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Product entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductIndexEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Product value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

}
