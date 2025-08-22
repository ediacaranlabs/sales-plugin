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

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderReportMessageEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class OrderReportMessageEntityAccessImp 
	extends AbstractEntityAccess<OrderReportMessage, OrderReportMessageEntity>
	implements OrderReportMessageEntityAccess {

	public OrderReportMessageEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public OrderReportMessageEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public void save(OrderReportMessage value) throws EntityAccessException {
		value.setId(IDGenerator.getUniqueOrderID('O', (int)SystemProperties.currentTimeMillis()));
		super.save(value);
	}
	
	@Override
	protected OrderReportMessageEntity toPersistenceEntity(OrderReportMessage entity)
			throws Throwable {
		return new OrderReportMessageEntity(entity);
	}

	@Override
	protected OrderReportMessage toEntity(OrderReportMessageEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(OrderReportMessage entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(OrderReportMessageEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(OrderReportMessage value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	public List<OrderReportMessage> getByOrderReport(String id, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderReportMessageEntity> criteria = builder.createQuery(OrderReportMessageEntity.class);
		    Root<OrderReportMessageEntity> from = criteria.from(OrderReportMessageEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("orderReportID"), id));
		    
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
	    	
		    TypedQuery<OrderReportMessageEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderReportMessageEntity> list = (List<OrderReportMessageEntity>)typed.getResultList();
		    List<OrderReportMessage> result = new ArrayList<OrderReportMessage>();

		    for(OrderReportMessageEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}

	@Override
	public OrderReportMessage findById(String id) throws EntityAccessException {
		return super.findById(id);
	}

}
