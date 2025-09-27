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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategorySearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductCategoryIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ProductCategoryIndexEntityAccessImp 
	extends AbstractEntityAccess<ProductCategory, ProductCategoryIndexEntity>
	implements ProductCategoryIndexEntityAccess {

	public ProductCategoryIndexEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductCategoryIndexEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected ProductCategoryIndexEntity toPersistenceEntity(ProductCategory entity)
			throws Throwable {
		return new ProductCategoryIndexEntity(entity);
	}

	@Override
	protected ProductCategory toEntity(ProductCategoryIndexEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(ProductCategory entity, Serializable id) throws Throwable {
		entity.setId((int) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductCategoryIndexEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(ProductCategory value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	@Override
	public ProductCategory findById(int id) throws EntityAccessException {
		return super.findById(id);
	}

	@Override
	public List<ProductCategory> searchProduct(ProductCategorySearch value, Integer first, Integer max)
			throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductCategoryIndexEntity> criteria = builder.createQuery(ProductCategoryIndexEntity.class);
		    Root<ProductCategoryIndexEntity> from = criteria.from(ProductCategoryIndexEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(value.getId() != null) {
		    	and.add(builder.equal(from.get("id"), value.getId()));
		    }

		    if(value.getName() != null && !value.getName().trim().isEmpty()) {
			    and.add(builder.like(from.get("name"), "%" + StringUtil.normalize(value.getName(), "%") + "%" ));
		    }
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<ProductCategoryIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<ProductCategoryIndexEntity> list = (List<ProductCategoryIndexEntity>)typed.getResultList();
		    List<ProductCategory> result = new ArrayList<ProductCategory>();

		    for(ProductCategoryIndexEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}
	
}
