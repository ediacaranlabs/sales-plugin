package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
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

	public void saveIndex(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			entityManager.persist(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void updateIndex(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			entityManager.merge(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void deleteIndex(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			entityManager.remove(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public boolean ifIndexExist(Product value) throws EntityAccessException {
		try{
			Object o = entityManager.find(ProductIndexEntity.class, value.getId());
			return o != null;
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public List<Product> searchProduct(ProductSearch value, Integer first, Integer max) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductIndexEntity> criteria = builder.createQuery(ProductIndexEntity.class);
		    Root<ProductIndexEntity> from = 	criteria.from(ProductIndexEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(value.getDescription() != null) {
		    	and.add(builder.equal(from.get("description"), StringUtil.toSearch(value.getDescription())));
		    }

		    if(value.getName() != null) {
		    	and.add(builder.equal(from.get("name"), StringUtil.toSearch(value.getName())));
		    }
		    
		    if(value.getMinCost() != null || value.getMaxCost() != null) {
		    	
		    	if(value.getMinCost() != null || value.getMaxCost() != null) {
				    and.add(builder.between(from.get("cost"), value.getMinCost(), value.getMaxCost()));
		    	}
		    	else
		    	if(value.getMinCost() != null) {
				    and.add(builder.greaterThanOrEqualTo(from.get("cost"), value.getMinCost()));
		    	}
		    	else
		    	if(value.getMaxCost() != null) {
				    and.add(builder.lessThanOrEqualTo(from.get("cost"), value.getMaxCost()));
		    	}
		    	
		    }

		    if(value.getProductType() != null) {
			    and.add(builder.equal(from.get("productType"), value.getProductType()));
		    }
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
	    	List<javax.persistence.criteria.Order> orderList = 
	    			new ArrayList<javax.persistence.criteria.Order>();
	    	orderList.add(builder.asc(from.get("name")));
	    	
		    TypedQuery<ProductIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<ProductIndexEntity> list = (List<ProductIndexEntity>)typed.getResultList();
		    List<Product> result = new ArrayList<Product>();
    
		    for(ProductIndexEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
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
