package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestRefundEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.RefundEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class RefundEntityAccessImp 
	extends AbstractEntityAccess<Refund, RefundEntity>
	implements RefundEntityAccess {

	public RefundEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public RefundEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	public void save(Refund value) throws EntityAccessException {
		try{
			RefundEntity pEntity = this.toPersistenceEntity(value);

			if(value.getId() != null){
				throw new EntityAccessException("id");
			}
			
			entityManager.flush();
			
			pEntity.setId(IDGenerator.getUniqueOrderID('O', (int)SystemProperties.currentTimeMillis()));
			
			entityManager.persist(pEntity);

			List<ProductRequestRefundEntity> list = pEntity.getProducts();
			
			if(list != null){
				for(ProductRequestRefundEntity e: list){
					e.setRefund(pEntity);
					entityManager.persist(e);
					
					ProductRequestEntity pr = entityManager.find(ProductRequestEntity.class, e.getId().getProductRequestID(), LockModeType.PESSIMISTIC_WRITE);
					
					if(pr != null) {
						pr.setRefund(pEntity);
						pr = entityManager.merge(pr);
					}
					
				}
			}
			
			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public void update(Refund value) throws EntityAccessException {
		try{
			RefundEntity pEntity = this.toPersistenceEntity(value);
			
			entityManager.flush();
			
			pEntity = (RefundEntity)entityManager.merge(pEntity);

			List<ProductRequestRefundEntity> list = pEntity.getProducts();
			
			if(list != null){
				for(ProductRequestRefundEntity e: list){
					e = entityManager.merge(e);
				}
			}
			
			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	@Override
	protected RefundEntity toPersistenceEntity(Refund entity)
			throws Throwable {
		return new RefundEntity(entity);
	}

	@Override
	protected Refund toEntity(RefundEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Refund entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(RefundEntity value)
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

	@Override
	public List<Refund> findByOrder(String order, Client user) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<RefundEntity> criteria = 
		    		builder.createQuery(RefundEntity.class);
		    Root<RefundEntity> from = criteria.from(RefundEntity.class);
		    Join<RefundEntity, OrderEntity> orderJoin = from.join("order");
		    
		    criteria.select(from);

		    List<Predicate> and = new ArrayList<Predicate>();
	    	and.add(builder.equal(orderJoin.get("id"), order));
		    
	    	if(user != null) {
		    	and.add(builder.equal(from.get("client"), user.getId()));
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
	    	
		    TypedQuery<RefundEntity> typed = entityManager.createQuery(criteria);

		    List<RefundEntity> list = (List<RefundEntity>)typed.getResultList();
		    List<Refund> result = new ArrayList<Refund>();
    
		    for(RefundEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}
	
	@Override
	public List<Refund> getList(Integer first, Integer max, Client user)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<RefundEntity> criteria = 
		    		builder.createQuery(RefundEntity.class);
		    Root<RefundEntity> from = criteria.from(RefundEntity.class);
		    
		    criteria.select(from);

		    List<Predicate> and = new ArrayList<Predicate>();
		    
	    	if(user != null) {
		    	and.add(builder.equal(from.get("client"), user.getId()));
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
	    	
		    TypedQuery<RefundEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<RefundEntity> list = (List<RefundEntity>)typed.getResultList();
		    List<Refund> result = new ArrayList<Refund>();
    
		    for(RefundEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}

	/*
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
    */
	
	@Override
	public Refund findById(String id) throws EntityAccessException {
		return super.findById(id);
	}

}
