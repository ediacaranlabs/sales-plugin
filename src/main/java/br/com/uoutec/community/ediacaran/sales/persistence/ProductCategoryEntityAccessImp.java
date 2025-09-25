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
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductCategoryEntity;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ProductCategoryEntityAccessImp 
	extends AbstractEntityAccess<ProductCategory, ProductCategoryEntity>
	implements ProductCategoryEntityAccess{

	public ProductCategoryEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductCategoryEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected ProductCategoryEntity toPersistenceEntity(ProductCategory entity)
			throws Throwable {
		return new ProductCategoryEntity(entity);
	}

	@Override
	protected ProductCategory toEntity(ProductCategoryEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(ProductCategory entity, Serializable id) throws Throwable {
		entity.setId((int) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductCategoryEntity value)
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
	public List<ProductCategory> getProductByParent(ProductCategory parentCategory) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductCategoryEntity> criteria = builder.createQuery(ProductCategoryEntity.class);
		    Root<ProductCategoryEntity> from = criteria.from(ProductCategoryEntity.class);
		    Join<ProductCategoryEntity, ProductCategoryEntity> parent = from.join("parent");
		    
		    criteria.select(from);

		    List<Predicate> and = new ArrayList<Predicate>();
	    	and.add(builder.equal(parent.get("id"), parentCategory.getId()));
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
	    	
		    TypedQuery<ProductCategoryEntity> typed = entityManager.createQuery(criteria);

		    List<ProductCategoryEntity> list = (List<ProductCategoryEntity>)typed.getResultList();
		    List<ProductCategory> result = new ArrayList<ProductCategory>();
    
		    for(ProductCategoryEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}
	
	@Override
	public ProductCategory findById(int id) throws EntityAccessException {
		return super.findById(id);
	}

}
