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
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ShippingIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ShippingIndexEntityAccessImp 
	extends AbstractEntityAccess<Shipping, ShippingIndexEntity>
	implements ShippingIndexEntityAccess {

	public ShippingIndexEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ShippingIndexEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected ShippingIndexEntity toPersistenceEntity(Shipping entity)
			throws Throwable {
		return new ShippingIndexEntity(entity);
	}

	@Override
	protected Shipping toEntity(ShippingIndexEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Shipping entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(ShippingIndexEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Shipping value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	public List<Shipping> search(ShippingSearch value, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ShippingIndexEntity> criteria = builder.createQuery(ShippingIndexEntity.class);
		    Root<ShippingIndexEntity> from = criteria.from(ShippingIndexEntity.class);
		    
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

		    if(value.getStartReceivedDate() != null || value.getEndReceivedDate() != null) {
		    	
		    	if(value.getStartReceivedDate() != null && value.getEndReceivedDate() != null) {
				    and.add(builder.between(from.get("receivedDate"), value.getStartReceivedDate(), value.getEndReceivedDate()));
		    	}
		    	else
		    	if(value.getStartReceivedDate() != null) {
				    and.add(builder.greaterThanOrEqualTo(from.get("receivedDate"), value.getStartReceivedDate()));
		    	}
		    	else
		    	if(value.getEndReceivedDate() != null) {
				    and.add(builder.lessThanOrEqualTo(from.get("receivedDate"), value.getEndReceivedDate()));
		    	}
		    	
		    }

		    if(value.getClosed() != null) {
			    and.add(builder.equal(from.get("closed"), value.getClosed()));
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
	    	
		    TypedQuery<ShippingIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<ShippingIndexEntity> list = (List<ShippingIndexEntity>)typed.getResultList();
		    List<Shipping> result = new ArrayList<Shipping>();

		    for(ShippingIndexEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}

	@Override
	public Shipping findById(String id) throws EntityAccessException {
		return super.findById(id);
	}

}
