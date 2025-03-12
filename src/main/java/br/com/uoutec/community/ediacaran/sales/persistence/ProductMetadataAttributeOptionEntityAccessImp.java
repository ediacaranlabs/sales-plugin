package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntityType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductMetadataAttributeOptionEntity;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ProductMetadataAttributeOptionEntityAccessImp 
	extends AbstractEntityAccess<ProductMetadataAttributeOption, ProductMetadataAttributeOptionEntity>
	implements ProductMetadataAttributeOptionEntityAccess{

	public ProductMetadataAttributeOptionEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductMetadataAttributeOptionEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected ProductMetadataAttributeOptionEntity toPersistenceEntity(ProductMetadataAttributeOption entity)
			throws Throwable {
		return new ProductMetadataAttributeOptionEntity(entity);
	}

	@Override
	protected ProductMetadataAttributeOption toEntity(ProductMetadataAttributeOptionEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(ProductMetadataAttributeOption entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductMetadataAttributeOptionEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(ProductMetadataAttributeOption value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	@Override
	public ProductMetadataAttributeOption findByValue(Object value, ProductMetadataAttribute parent) throws EntityAccessException{
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductMetadataAttributeOptionEntity> criteria = builder.createQuery(ProductMetadataAttributeOptionEntity.class);
		    Root<ProductMetadataAttributeOptionEntity> from = criteria.from(ProductMetadataAttributeOptionEntity.class);
		    Join<ProductMetadataAttribute, ProductMetadataAttributeOptionEntity> join = from.join("productAttribute");
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(join.get("id"), parent.getId()));
	    	
	    	
	    	ProductAttributeValueEntityType type = ProductAttributeValueEntityType.valueOf(parent.getValueType().name());
	    	value = type.toValue(value);
	    	
	    	if(value instanceof Long) {
		    	and.add(builder.equal(from.get("number"), value));
	    	}
	    	else {
	    		and.add(builder.equal(from.get("value"), value));
	    	}
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<ProductMetadataAttributeOptionEntity> typed = entityManager.createQuery(criteria);

		    ProductMetadataAttributeOptionEntity e = (ProductMetadataAttributeOptionEntity)typed.getSingleResult();
		    		
			return e == null? null : e.toEntity();
		}
		catch(NoResultException e) {
			return null;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
	}
	
	@Override
	public List<ProductMetadataAttributeOption> getByProductMetadataAttribute(ProductMetadataAttribute parent)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductMetadataAttributeOptionEntity> criteria = builder.createQuery(ProductMetadataAttributeOptionEntity.class);
		    Root<ProductMetadataAttributeOptionEntity> from = criteria.from(ProductMetadataAttributeOptionEntity.class);
		    Join<ProductMetadataAttribute, ProductMetadataAttributeOptionEntity> join = from.join("productAttribute");
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(join.get("id"), parent.getId()));
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<ProductMetadataAttributeOptionEntity> typed = entityManager.createQuery(criteria);


		    List<ProductMetadataAttributeOptionEntity> list = (List<ProductMetadataAttributeOptionEntity>)typed.getResultList();
		    List<ProductMetadataAttributeOption> result = new ArrayList<ProductMetadataAttributeOption>();
    
		    for(ProductMetadataAttributeOptionEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}	
	}

}
