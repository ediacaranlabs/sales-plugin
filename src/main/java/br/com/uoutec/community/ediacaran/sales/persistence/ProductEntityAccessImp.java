package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.uoutec.persistence.EntityAccessException;
import br.com.uoutec.persistence.hibernate.AbstractEntityAccess;
import br.com.uoutec.portal.entity.Product;
import br.com.uoutec.portal.entity.ProductType;
import br.com.uoutec.services.entityaccess.ServicePlanEntityAccess;
import br.com.uoutec.services.entityaccess.hibernate.entity.ProductHibernateEntity;
import br.com.uoutec.services.entityaccess.hibernate.entity.ProductTypeHibernateEntity;

public class ProductEntityAccessImp 
	extends AbstractEntityAccess<Product, ProductHibernateEntity>
	implements ServicePlanEntityAccess{

	@Inject
	public ProductEntityAccessImp(Session session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProductByType(ProductType productType) throws EntityAccessException{
		try{
			Criteria c = 
					session
						.createCriteria(ProductHibernateEntity.class, "product")
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			c.add(Restrictions.eq("product.productType", new ProductTypeHibernateEntity(productType)));
			
			List<ProductHibernateEntity> list = c.list();
			return list == null? null : this.toCollection(list);
		}
		catch(Throwable e){
			throw new EntityAccessException(e);
		}	
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProductByCode(String code) throws EntityAccessException{
		try{
			Criteria c = 
					session
						.createCriteria(ProductHibernateEntity.class, "product")
						.createAlias("productType", "type", JoinType.INNER_JOIN)
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			c.add(Restrictions.eq("type.code", code));
			
			List<ProductHibernateEntity> list = c.list();
			return list == null? null : this.toCollection(list);
		}
		catch(Throwable e){
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
