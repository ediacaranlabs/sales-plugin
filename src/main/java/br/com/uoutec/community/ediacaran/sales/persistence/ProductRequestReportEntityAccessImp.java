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
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderIndexEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestReportEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
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

	public List<Order> searchOrder(OrderSearch value, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderIndexEntity> criteria = builder.createQuery(OrderIndexEntity.class);
		    Root<OrderIndexEntity> from = criteria.from(OrderIndexEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(value.getId() != null) {
		    	and.add(builder.equal(from.get("id"), value.getId()));
		    }
		    
		    if(value.getStartDate() != null || value.getEndDate() != null) {
		    	
		    	if(value.getStartDate() != null && value.getEndDate() != null) {
				    and.add(builder.between(from.get("date"), value.getStartDate(), value.getEndDate()));
		    	}
		    	else
		    	if(value.getStartDate() != null) {
				    and.add(builder.greaterThanOrEqualTo(from.get("date"), value.getStartDate()));
		    	}
		    	else
		    	if(value.getEndDate() != null) {
				    and.add(builder.lessThanOrEqualTo(from.get("date"), value.getEndDate()));
		    	}
		    	
		    }

		    if(value.getMinTotal() != null || value.getMaxTotal() != null) {
		    	
		    	if(value.getMinTotal() != null && value.getMaxTotal() != null) {
				    and.add(builder.between(from.get("total"), value.getMinTotal(), value.getMaxTotal()));
		    	}
		    	else
		    	if(value.getMinTotal() != null) {
				    and.add(builder.greaterThanOrEqualTo(from.get("total"), value.getMinTotal()));
		    	}
		    	else
		    	if(value.getMaxTotal() != null) {
				    and.add(builder.lessThanOrEqualTo(from.get("total"), value.getMaxTotal()));
		    	}
		    	
		    }
		    
		    if(value.getOwner() != null) {
			    and.add(builder.equal(from.get("client"), value.getOwner()));
		    }

		    if(value.getStatus() != null) {
			    and.add(builder.equal(from.get("status"), value.getStatus()));
		    }
		    
		    if(value.getOwnerName() != null && !value.getOwnerName().trim().isEmpty()) {
			    and.add(builder.like(from.get("clientName"), "%" + StringUtil.normalize(value.getOwnerName(), "%") + "%" ));
		    }
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
	    	List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
	    	
	    	orderList.add(builder.desc(from.get("date")));
	    	
	    	criteria.orderBy(orderList);
	    	
		    TypedQuery<OrderIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderIndexEntity> list = (List<OrderIndexEntity>)typed.getResultList();
		    List<Order> result = new ArrayList<Order>();
    
		    for(OrderIndexEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}

}
