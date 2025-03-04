package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductMetadataAttributeEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductMetadataAttributeOptionEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductMetadataEntity;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ProductMetadataAttributeEntityAccessImp 
	extends AbstractEntityAccess<ProductMetadataAttribute, ProductMetadataAttributeEntity>
	implements ProductMetadataAttributeEntityAccess{

	public ProductMetadataAttributeEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductMetadataAttributeEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected ProductMetadataAttributeEntity toPersistenceEntity(ProductMetadataAttribute entity)
			throws Throwable {
		return new ProductMetadataAttributeEntity(entity);
	}

	@Override
	protected ProductMetadataAttribute toEntity(ProductMetadataAttributeEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(ProductMetadataAttribute entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductMetadataAttributeEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(ProductMetadataAttribute value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	@Override
	public void save(ProductMetadataAttribute entity) throws EntityAccessException {
		try{
			ProductMetadataAttributeEntity e = new ProductMetadataAttributeEntity(entity);
			entityManager.persist(e);
			entityManager.flush();
			
			if(e.getOptions() != null) {
				
				List<ProductMetadataAttributeOptionEntity> list = e.getOptions();
				List<ProductMetadataAttributeOption> list2 = entity.getOptions();
				
				int i=0;
				for(ProductMetadataAttributeOptionEntity x: list) {
					x.setProductAttribute(e);
					entityManager.persist(x);
					x.toEntity(list2.get(i));
				}
			}
			
			e.toEntity(entity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	@Override
	public void update(ProductMetadataAttribute entity) throws EntityAccessException {
		try{
			ProductMetadataAttributeEntity e = new ProductMetadataAttributeEntity(entity);					
			e = entityManager.merge(e);
					
			if(e.getOptions() != null) {
				List<ProductMetadataAttributeOptionEntity> list2 = e.getOptions();
				
				for(ProductMetadataAttributeOptionEntity x: list2) {
					
					x.setProductAttribute(e);
					
					if(e.getId() == null) {
						entityManager.persist(x);
						entityManager.flush();
					}
					else {
						x = entityManager.merge(x);
					}
				}
			}
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	@Override
	public void delete(ProductMetadataAttribute entity) throws EntityAccessException {
		try{
			ProductMetadataAttributeEntity e = new ProductMetadataAttributeEntity(entity);					
			e = (ProductMetadataAttributeEntity)entityManager.merge(e);

			if(e.getOptions() != null) {
				
				List<ProductMetadataAttributeOptionEntity> list2 = e.getOptions();
				
				for(ProductMetadataAttributeOptionEntity x: list2) {
					x = (ProductMetadataAttributeOptionEntity)entityManager.merge(x);
					entityManager.remove(x);
				}
			}
			
			entityManager.remove(e);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	@Override
	public List<ProductMetadataAttribute> getByProductMetadata(ProductMetadata productMetadata)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductMetadataAttributeEntity> criteria = builder.createQuery(ProductMetadataAttributeEntity.class);
		    Root<ProductMetadataAttributeEntity> from = criteria.from(ProductMetadataAttributeEntity.class);
		    Join<ProductMetadataEntity, ProductMetadataAttributeEntity> join = from.join("productMetadata");
		    
		    criteria.select(from);

		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(join.get("id"), productMetadata.getId()));
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<ProductMetadataAttributeEntity> typed = entityManager.createQuery(criteria);


		    List<ProductMetadataAttributeEntity> list = (List<ProductMetadataAttributeEntity>)typed.getResultList();
		    List<ProductMetadataAttribute> result = new ArrayList<ProductMetadataAttribute>();
    
		    for(ProductMetadataAttributeEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}	
	}

}
