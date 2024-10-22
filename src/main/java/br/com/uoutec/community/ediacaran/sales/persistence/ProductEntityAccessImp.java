package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductEntity;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductEntityAccessImp 
	extends AbstractEntityAccess<Product, ProductEntity>
	implements ProductEntityAccess{

	public ProductEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}

	public List<Product> getProductByType(ProductType productType) throws EntityAccessException{
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductEntity> criteria = 
		    		builder.createQuery(ProductEntity.class);
		    Root<ProductEntity> from = 
		    		criteria.from(ProductEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("productType"), productType.getCode())
    				)
    		);
		    
		    TypedQuery<ProductEntity> typed = 
		    		entityManager.createQuery(criteria);


		    List<ProductEntity> list = (List<ProductEntity>)typed.getResultList();
		    List<Product> result = new ArrayList<Product>();
    
		    for(ProductEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}

	public List<Product> getProductByCode(String code) throws EntityAccessException{
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductEntity> criteria = 
		    		builder.createQuery(ProductEntity.class);
		    Root<ProductEntity> from = 
		    		criteria.from(ProductEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("productType"), code)
    				)
    		);
		    
		    TypedQuery<ProductEntity> typed = 
		    		entityManager.createQuery(criteria);


		    List<ProductEntity> list = (List<ProductEntity>)typed.getResultList();
		    List<Product> result = new ArrayList<Product>();
    
		    for(ProductEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}
	
	@Override
	protected ProductEntity toPersistenceEntity(Product entity)
			throws Throwable {
		return new ProductEntity(entity);
	}

	@Override
	protected Product toEntity(ProductEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Product entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductEntity value)
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
