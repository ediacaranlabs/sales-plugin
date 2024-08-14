package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductHibernateEntity;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductEntityAccessImp 
	extends AbstractEntityAccess<Product, ProductHibernateEntity>
	implements ProductEntityAccess{

	@Inject
	public ProductEntityAccessImp(Session session) {
		super(session);
	}

	public List<Product> getProductByType(ProductType productType) throws EntityAccessException{
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductHibernateEntity> criteria = 
		    		builder.createQuery(ProductHibernateEntity.class);
		    Root<ProductHibernateEntity> from = 
		    		criteria.from(ProductHibernateEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("productType"), productType.getCode())
    				)
    		);
		    
		    TypedQuery<ProductHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


		    List<ProductHibernateEntity> list = (List<ProductHibernateEntity>)typed.getResultList();
		    List<Product> result = new ArrayList<Product>();
    
		    for(ProductHibernateEntity e: list) {
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
		    CriteriaQuery<ProductHibernateEntity> criteria = 
		    		builder.createQuery(ProductHibernateEntity.class);
		    Root<ProductHibernateEntity> from = 
		    		criteria.from(ProductHibernateEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("productType"), code)
    				)
    		);
		    
		    TypedQuery<ProductHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


		    List<ProductHibernateEntity> list = (List<ProductHibernateEntity>)typed.getResultList();
		    List<Product> result = new ArrayList<Product>();
    
		    for(ProductHibernateEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}
	
	@Override
	protected ProductHibernateEntity toPersistenceEntity(Product entity)
			throws Throwable {
		return new ProductHibernateEntity(entity);
	}

	@Override
	protected Product toEntity(ProductHibernateEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Product entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductHibernateEntity value)
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
