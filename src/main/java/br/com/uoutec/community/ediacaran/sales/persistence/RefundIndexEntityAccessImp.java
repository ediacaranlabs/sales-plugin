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
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.RefundIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class RefundIndexEntityAccessImp 
	extends AbstractEntityAccess<Refund, RefundIndexEntity>
	implements RefundIndexEntityAccess {

	public RefundIndexEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public RefundIndexEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected RefundIndexEntity toPersistenceEntity(Refund entity)
			throws Throwable {
		return new RefundIndexEntity(entity);
	}

	@Override
	protected Refund toEntity(RefundIndexEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Refund entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(RefundIndexEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Refund value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	public List<Refund> search(RefundSearch value, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<RefundIndexEntity> criteria = builder.createQuery(RefundIndexEntity.class);
		    Root<RefundIndexEntity> from = criteria.from(RefundIndexEntity.class);
		    
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

		    if(value.getStartRefundDate() != null || value.getEndRefundDate() != null) {
		    	
		    	if(value.getStartRefundDate() != null && value.getEndRefundDate() != null) {
				    and.add(builder.between(from.get("refundDate"), value.getStartRefundDate(), value.getEndRefundDate()));
		    	}
		    	else
		    	if(value.getStartRefundDate() != null) {
				    and.add(builder.greaterThanOrEqualTo(from.get("refundDate"), value.getStartRefundDate()));
		    	}
		    	else
		    	if(value.getEndRefundDate() != null) {
				    and.add(builder.lessThanOrEqualTo(from.get("refundDate"), value.getEndRefundDate()));
		    	}
		    	
		    }

		    if(value.getClient() != null) {
			    and.add(builder.equal(from.get("client"), value.getClient()));
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
	    	
		    TypedQuery<RefundIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<RefundIndexEntity> list = (List<RefundIndexEntity>)typed.getResultList();
		    List<Refund> result = new ArrayList<Refund>();

		    for(RefundIndexEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}

	@Override
	public Refund findById(String id) throws EntityAccessException {
		return super.findById(id);
	}

}
