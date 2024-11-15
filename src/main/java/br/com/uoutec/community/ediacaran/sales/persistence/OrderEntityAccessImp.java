package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderLogEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderTaxEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.PaymentEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestTaxEntity;
import br.com.uoutec.community.ediacaran.sales.pub.OrderSearch;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.community.ediacaran.user.entityaccess.jpa.entity.SystemUserEntity;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class OrderEntityAccessImp 
	extends AbstractEntityAccess<Order, OrderEntity>
	implements OrderEntityAccess{

	public OrderEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public OrderEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}

	public void save(Order value) throws EntityAccessException {
		try{
			OrderEntity pEntity = this.toPersistenceEntity(value);

			if(value.getId() != null){
				throw new EntityAccessException("id");
			}
			
			pEntity.setId(IDGenerator.getUniqueOrderID('O', value.getOwner()));
			
			/*
			if(pEntity.getInvoice() != null){
				//if(pEntity.getInvoice().getId() == null){
					pEntity.getInvoice().setId(IDGenerator.getUniqueOrderID('I', value.getOwner()));
					entityManager.persist(pEntity.getInvoice());
				//}
			}
			*/
			
			if(pEntity.getPayment() != null){
				if(pEntity.getPayment().getId() == null){
					pEntity.getPayment().setId(IDGenerator.getUniqueOrderID('P', value.getOwner()));
					entityManager.persist(pEntity.getPayment());
				}
			}
			
			entityManager.persist(pEntity);
			
			List<ProductRequestEntity> list = 
					pEntity.getItens();
			
			if(list != null){
				for(ProductRequestEntity e: list){
					e.setOrder(pEntity);
					e.setId(IDGenerator.getUniqueOrderID('R', value.getOwner()));
					entityManager.persist(e);
					
					List<ProductRequestTaxEntity> prdel = e.getTaxes();
					
					if(prdel != null){
						for(ProductRequestTaxEntity k: prdel){
							k.setProductRequest(e);
							k.setId(IDGenerator.getUniqueOrderID('D', value.getOwner()));
							entityManager.persist(k);
						}
					}
				}
			}
			
			List<OrderTaxEntity> odl = pEntity.getTaxes();
			
			if(odl != null){
				for(OrderTaxEntity k: odl){
					k.setOrderEntity(pEntity);
					k.setId(IDGenerator.getUniqueOrderID('D', value.getOwner()));
					entityManager.persist(k);
				}
			}

			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void update(Order value) throws EntityAccessException {
		try{
			OrderEntity pEntity = this.toPersistenceEntity(value);

			/*
			if(pEntity.getInvoice() != null){
				if(pEntity.getInvoice().getId() == null){
					pEntity.getInvoice().setId(IDGenerator.getUniqueOrderID('I', value.getOwner()));
					entityManager.persist(pEntity.getInvoice());
				}
				else{
					entityManager.merge(pEntity.getInvoice());
				}
			}
			*/
			
			if(pEntity.getPayment() != null){
				if(pEntity.getPayment().getId() == null){
					pEntity.getPayment().setId(IDGenerator.getUniqueOrderID('P', value.getOwner()));
					entityManager.persist(pEntity.getPayment());
				}
				else{
					entityManager.merge(pEntity.getPayment());
				}
			}
			
			pEntity = (OrderEntity)entityManager.merge(pEntity);
			
			List<ProductRequestEntity> list = 
					pEntity.getItens();
			
			if(list != null){
				for(ProductRequestEntity e: list){
					if(e.getId() == null){
						e.setOrder(pEntity);
						e.setId(IDGenerator.getUniqueOrderID('R', value.getOwner()));
						entityManager.persist(e);
					}
					else{
						entityManager.merge(e);
					}
					
					List<ProductRequestTaxEntity> prdel = e.getTaxes();
					
					if(prdel != null){
						for(ProductRequestTaxEntity k: prdel){
							if(k.getId() == null){
								k.setProductRequest(e);
								k.setId(IDGenerator.getUniqueOrderID('D', value.getOwner()));
								entityManager.persist(k);
							}
							else{
								entityManager.merge(k);
							}
						}
					}
					
				}
			}
			
			List<OrderTaxEntity> odl = pEntity.getTaxes();
			
			if(odl != null){
				for(OrderTaxEntity k: odl){
					if(k.getId() == null){
						k.setOrderEntity(pEntity);
						k.setId(IDGenerator.getUniqueOrderID('D', value.getOwner()));
						entityManager.persist(k);
					}
					else{
						entityManager.merge(k);
					}
				}
			}

			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	@Override
	protected OrderEntity toPersistenceEntity(Order entity)
			throws Throwable {
		return new OrderEntity(entity);
	}

	@Override
	protected Order toEntity(OrderEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Order entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(OrderEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Order value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	public List<OrderResultSearch> searchOrder(OrderSearch value, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderEntity> criteria = 
		    		builder.createQuery(OrderEntity.class);
		    Root<OrderEntity> from = 
		    		criteria.from(OrderEntity.class);
		    Join<OrderEntity, SystemUserEntity> systemUserJoin = from.join("owner");
		    Join<OrderEntity, PaymentEntity> paymentJoin = from.join("payment");
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(value.getId() != null) {
		    	and.add(builder.equal(from.get("id"), value.getId()));
		    }
		    
		    if(value.getStartDate() != null || value.getEndDate() != null) {
		    	
		    	if(value.getStartDate() != null && value.getEndDate() != null) {
				    and.add(builder.between(systemUserJoin.get("date"), value.getStartDate(), value.getEndDate()));
		    	}
		    	else
		    	if(value.getStartDate() != null) {
				    and.add(builder.greaterThanOrEqualTo(systemUserJoin.get("date"), value.getStartDate()));
		    	}
		    	else
		    	if(value.getEndDate() != null) {
				    and.add(builder.lessThanOrEqualTo(systemUserJoin.get("date"), value.getEndDate()));
		    	}
		    	
		    }

		    if(value.getMinTotal() != null || value.getMaxTotal() != null) {
		    	
		    	if(value.getMinTotal() != null && value.getMaxTotal() != null) {
				    and.add(builder.between(paymentJoin.get("total"), value.getMinTotal(), value.getMaxTotal()));
		    	}
		    	else
		    	if(value.getMinTotal() != null) {
				    and.add(builder.greaterThanOrEqualTo(paymentJoin.get("total"), value.getMinTotal()));
		    	}
		    	else
		    	if(value.getMaxTotal() != null) {
				    and.add(builder.lessThanOrEqualTo(paymentJoin.get("total"), value.getMaxTotal()));
		    	}
		    	
		    }
		    
		    if(value.getOwner() != null) {
			    and.add(builder.equal(systemUserJoin.get("id"), value.getOwner()));
		    }

		    if(value.getStatus() != null) {
			    and.add(builder.equal(from.get("status"), value.getStatus()));
		    }
		    
		    if(value.getOwnerName() != null && !value.getOwnerName().trim().isEmpty()) {
			    and.add(builder.like(systemUserJoin.get("searchName"), "%" + StringUtil.normalize(value.getOwnerName(), "%") + "%" ));
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
	    	
		    TypedQuery<OrderEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderEntity> list = (List<OrderEntity>)typed.getResultList();
		    List<OrderResultSearch> result = new ArrayList<OrderResultSearch>();
    
		    for(OrderEntity e: list) {
		    	result.add(new OrderResultSearch(e.toEntity(), e.getOwner().toEntity()));
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}
	
	public List<Order> getOrders(Integer owner, Integer first, Integer max)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderEntity> criteria = 
		    		builder.createQuery(OrderEntity.class);
		    Root<OrderEntity> from = 
		    		criteria.from(OrderEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("owner"), owner)
    				)
    		);
		    
	    	List<javax.persistence.criteria.Order> orderList = 
	    			new ArrayList<javax.persistence.criteria.Order>();
	    	orderList.add(builder.desc(from.get("date")));
	    	
		    TypedQuery<OrderEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderEntity> list = (List<OrderEntity>)typed.getResultList();
		    List<Order> result = new ArrayList<Order>();
    
		    for(OrderEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}

	public List<Order> getOrders(Integer owner, OrderStatus status,
			Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderEntity> criteria = 
		    		builder.createQuery(OrderEntity.class);
		    Root<OrderEntity> from = 
		    		criteria.from(OrderEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(owner != null) {
		    	and.add(builder.equal(from.get("owner"), owner));
		    }

		    if(status != null) {
		    	and.add(builder.equal(from.get("status"), status));
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
	    	
		    TypedQuery<OrderEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderEntity> list = (List<OrderEntity>)typed.getResultList();
		    List<Order> result = new ArrayList<Order>();
    
		    for(OrderEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
		
	}

	public List<Order> getOrders(OrderStatus status, Integer first, Integer max)
			throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderEntity> criteria = 
		    		builder.createQuery(OrderEntity.class);
		    Root<OrderEntity> from = 
		    		criteria.from(OrderEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(status != null) {
		    	and.add(builder.equal(from.get("status"), status));
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
	    	
		    TypedQuery<OrderEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderEntity> list = (List<OrderEntity>)typed.getResultList();
		    List<Order> result = new ArrayList<Order>();
    
		    for(OrderEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
	}

	public ProductRequest getProductRequest(String orderID, String id) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductRequestEntity> criteria = 
		    		builder.createQuery(ProductRequestEntity.class);
		    Root<ProductRequestEntity> from = 
		    		criteria.from(ProductRequestEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("id"), id));
		    
		    Join<ProductRequestEntity, OrderEntity> orderJoin = from.join("order");
		    and.add(builder.equal(orderJoin.get("id"), orderID));
	    	
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<ProductRequestEntity> typed = 
		    		entityManager.createQuery(criteria);


		    ProductRequestEntity e = typed.getSingleResult();
		    
		    return e == null? null : e.toEntity();
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
		
	}
	
	public Order findByCartID(String id) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderEntity> criteria = 
		    		builder.createQuery(OrderEntity.class);
		    Root<OrderEntity> from = 
		    		criteria.from(OrderEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("cartID"), id));
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<OrderEntity> typed = 
		    		entityManager.createQuery(criteria);


		    OrderEntity e = typed.getSingleResult();
		    
		    return e == null? null : e.toEntity();
		}
		catch(NoResultException e) {
			return null;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
	}

	@Override
	public void registryLog(Order order, String message)
			throws EntityAccessException {
		try{
			OrderLog log = new OrderLog();
			log.setDate(LocalDateTime.now());
			log.setMessage(message);
			log.setOrderId(order.getId());
			log.setOwner(order.getOwner());
			
			OrderLogEntity e = new OrderLogEntity(log);
			entityManager.persist(e);
			
		}
		catch(Throwable e){
			throw new EntityAccessException(e);
		}
	}

	@Override
	public void updateLog(OrderLog log) throws EntityAccessException {
		try{
			OrderLogEntity e = new OrderLogEntity(log);
			entityManager.merge(e);
		}
		catch(Throwable e){
			throw new EntityAccessException(e);
		}
	}

	@Override
	public void deleteLog(OrderLog log) throws EntityAccessException {
		try{
			OrderLogEntity e = new OrderLogEntity(log);
			e = (OrderLogEntity)entityManager.merge(e);
			entityManager.remove(e);
		}
		catch(Throwable e){
			throw new EntityAccessException(e);
		}
	}

	@Override
	public List<OrderLog> getLogs(Order order, Integer first, Integer max)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderLogEntity> criteria = 
		    		builder.createQuery(OrderLogEntity.class);
		    Root<OrderLogEntity> from = 
		    		criteria.from(OrderLogEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("orderId"), order.getId()));
	    	
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<OrderLogEntity> typed = 
		    		entityManager.createQuery(criteria);


			if(first != null){
				typed.setFirstResult(first);
			}
			
			if(max != null){
				typed.setMaxResults(max);
			}
			
			List<OrderLogEntity> list = (List<OrderLogEntity>) typed.getResultList();
			List<OrderLog> result = new ArrayList<OrderLog>(5);
			for(OrderLogEntity e: list){
				result.add(e.toEntity());
			}
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
		
	}

}
