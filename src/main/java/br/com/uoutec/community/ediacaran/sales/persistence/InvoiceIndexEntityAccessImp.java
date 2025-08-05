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
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.InvoiceIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class InvoiceIndexEntityAccessImp 
	extends AbstractEntityAccess<Invoice, InvoiceIndexEntity>
	implements InvoiceIndexEntityAccess{

	public InvoiceIndexEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public InvoiceIndexEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected InvoiceIndexEntity toPersistenceEntity(Invoice entity)
			throws Throwable {
		return new InvoiceIndexEntity(entity);
	}

	@Override
	protected Invoice toEntity(InvoiceIndexEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Invoice entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(InvoiceIndexEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Invoice value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}


	public List<Invoice> search(InvoiceSearch value, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<InvoiceIndexEntity> criteria = builder.createQuery(InvoiceIndexEntity.class);
		    Root<InvoiceIndexEntity> from = criteria.from(InvoiceIndexEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(value.getId() != null) {
		    	and.add(builder.equal(from.get("id"), value.getId()));
		    }

		    if(value.getCanceled() != null) {
		    	if(value.getCanceled()) {
			    	and.add(builder.isNotNull(from.get("cancelDate")));
		    	}
		    	else {
			    	and.add(builder.isNull(from.get("cancelDate")));
		    	}
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
		    
	    	List<javax.persistence.criteria.Order> orderList = 
	    			new ArrayList<javax.persistence.criteria.Order>();
	    	orderList.add(builder.desc(from.get("date")));
	    	
	    	criteria.orderBy(orderList);
	    	
		    TypedQuery<InvoiceIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<InvoiceIndexEntity> list = (List<InvoiceIndexEntity>)typed.getResultList();
		    List<Invoice> result = new ArrayList<Invoice>();
    
		    for(InvoiceIndexEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}
	
}
