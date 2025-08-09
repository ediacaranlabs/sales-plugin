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
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderReportIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class OrderReportIndexEntityAccessImp 
	extends AbstractEntityAccess<OrderReport, OrderReportIndexEntity>
	implements OrderReportIndexEntityAccess {

	public OrderReportIndexEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public OrderReportIndexEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected OrderReportIndexEntity toPersistenceEntity(OrderReport entity)
			throws Throwable {
		return new OrderReportIndexEntity(entity);
	}

	@Override
	protected OrderReport toEntity(OrderReportIndexEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(OrderReport entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(OrderReportIndexEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(OrderReport value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	public List<OrderReport> search(OrderReportSearch value, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderReportIndexEntity> criteria = builder.createQuery(OrderReportIndexEntity.class);
		    Root<OrderReportIndexEntity> from = criteria.from(OrderReportIndexEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(value.getId() != null) {
		    	and.add(builder.equal(from.get("id"), value.getId()));
		    }

		    if(value.getOrder() != null) {
		    	and.add(builder.equal(from.get("order"), value.getOrder()));
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
		    
		    if(value.getClient() != null) {
			    and.add(builder.equal(from.get("client"), value.getClient()));
		    }

		    if(value.getStatus() != null) {
			    and.add(builder.equal(from.get("status"), value.getStatus()));
		    }
		    
		    if(value.getClientName() != null && !value.getClientName().trim().isEmpty()) {
			    and.add(builder.like(from.get("clientName"), "%" + StringUtil.normalize(value.getClientName(), "%") + "%" ));
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
	    	orderList.add(builder.desc(from.get("date")));
	    	
	    	criteria.orderBy(orderList);
	    	
		    TypedQuery<OrderReportIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderReportIndexEntity> list = (List<OrderReportIndexEntity>)typed.getResultList();
		    List<OrderReport> result = new ArrayList<OrderReport>();

		    for(OrderReportIndexEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}

	@Override
	public OrderReport findById(String id) throws EntityAccessException {
		return super.findById(id);
	}

}
