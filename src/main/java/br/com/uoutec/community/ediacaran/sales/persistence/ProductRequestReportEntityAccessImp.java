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
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestReportEntity;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ProductRequestReportEntityAccessImp 
	extends AbstractEntityAccess<ProductRequestReport, ProductRequestReportEntity>
	implements ProductRequestReportEntityAccess {

	public ProductRequestReportEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductRequestReportEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected ProductRequestReportEntity toPersistenceEntity(ProductRequestReport entity)
			throws Throwable {
		return new ProductRequestReportEntity(entity);
	}

	@Override
	protected ProductRequestReport toEntity(ProductRequestReportEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(ProductRequestReport entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductRequestReportEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(ProductRequestReport value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	public List<ProductRequestReport> getByOrderReport(String id, Integer firstResult, Integer max)  throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductRequestReportEntity> criteria = builder.createQuery(ProductRequestReportEntity.class);
		    Root<ProductRequestReportEntity> from = criteria.from(ProductRequestReportEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("id").get("orderReportID"), id));
		    
	    	List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
	    	
	    	orderList.add(builder.desc(from.get("date")));
	    	
	    	criteria.orderBy(orderList);
	    	
		    TypedQuery<ProductRequestReportEntity> typed = entityManager.createQuery(criteria);


		    if(firstResult != null) {
		    	typed.setFirstResult(firstResult);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<ProductRequestReportEntity> list = (List<ProductRequestReportEntity>)typed.getResultList();
		    List<ProductRequestReport> result = new ArrayList<ProductRequestReport>();
    
		    for(ProductRequestReportEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}

	@Override
	public ProductRequestReport findById(String id) throws EntityAccessException {
		return super.findById(id);
	}

}
