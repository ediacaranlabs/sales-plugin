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

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductImageEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.persistence.EntityAccessException;

public class ProductImageEntityAccessImp 
	extends AbstractEntityAccess<ProductImage, ProductImageEntity>
	implements ProductImageEntityAccess{

	public ProductImageEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductImageEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	public void save(ProductImage entity) throws EntityAccessException {
		entity.setId(IDGenerator.getUniqueOrderID('P', (int)SystemProperties.currentTimeMillis()));
		super.save(entity);
	}
	
	public List<ProductImage> getImagesByProduct(Product product) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductImageEntity> criteria = builder.createQuery(ProductImageEntity.class);
		    Root<ProductImageEntity> from = 	criteria.from(ProductImageEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("product"), product.getId()));

		    TypedQuery<ProductImageEntity> typed = entityManager.createQuery(criteria);


		    List<ProductImageEntity> list = (List<ProductImageEntity>)typed.getResultList();
		    List<ProductImage> result = new ArrayList<ProductImage>();
    
		    for(ProductImageEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}
	
	@Override
	protected ProductImageEntity toPersistenceEntity(ProductImage entity)
			throws Throwable {
		return new ProductImageEntity(entity);
	}

	@Override
	protected ProductImage toEntity(ProductImageEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(ProductImage entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductImageEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(ProductImage value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

}
