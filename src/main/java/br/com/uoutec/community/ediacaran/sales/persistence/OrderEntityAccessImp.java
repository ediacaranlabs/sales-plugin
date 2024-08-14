package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderDiscountHibernateEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderHibernateEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderLogHibernateEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestDiscountHibernateEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestHibernateEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class OrderEntityAccessImp 
	extends AbstractEntityAccess<Order, OrderHibernateEntity>
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
			OrderHibernateEntity pEntity = this.toPersistenceEntity(value);

			if(value.getId() != null){
				throw new EntityAccessException("id");
			}
			
			pEntity.setId(IDGenerator.getUniqueOrderID('O', value.getOwner()));
			
			if(pEntity.getInvoice() != null){
				if(pEntity.getInvoice().getId() == null){
					pEntity.getInvoice().setId(IDGenerator.getUniqueOrderID('I', value.getOwner()));
					entityManager.persist(pEntity.getInvoice());
				}
			}
			
			if(pEntity.getPayment() != null){
				if(pEntity.getPayment().getId() == null){
					pEntity.getPayment().setId(IDGenerator.getUniqueOrderID('P', value.getOwner()));
					entityManager.persist(pEntity.getPayment());
				}
			}
			
			entityManager.persist(pEntity);
			
			List<ProductRequestHibernateEntity> list = 
					pEntity.getItens();
			
			if(list != null){
				for(ProductRequestHibernateEntity e: list){
					e.setId(IDGenerator.getUniqueOrderID('R', value.getOwner()));
					entityManager.persist(e);
					
					List<ProductRequestDiscountHibernateEntity> prdel = e.getDiscounts();
					
					if(prdel != null){
						for(ProductRequestDiscountHibernateEntity k: prdel){
							k.setId(IDGenerator.getUniqueOrderID('D', value.getOwner()));
							entityManager.persist(k);
						}
					}
				}
			}
			
			List<OrderDiscountHibernateEntity> odl = pEntity.getDiscounts();
			
			if(odl != null){
				for(OrderDiscountHibernateEntity k: odl){
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
			OrderHibernateEntity pEntity = this.toPersistenceEntity(value);

			if(pEntity.getInvoice() != null){
				if(pEntity.getInvoice().getId() == null){
					pEntity.getInvoice().setId(IDGenerator.getUniqueOrderID('I', value.getOwner()));
					entityManager.persist(pEntity.getInvoice());
				}
				else{
					entityManager.merge(pEntity.getInvoice());
				}
			}
			
			if(pEntity.getPayment() != null){
				if(pEntity.getPayment().getId() == null){
					pEntity.getPayment().setId(IDGenerator.getUniqueOrderID('P', value.getOwner()));
					entityManager.persist(pEntity.getPayment());
				}
				else{
					entityManager.merge(pEntity.getPayment());
				}
			}
			
			pEntity = (OrderHibernateEntity)entityManager.merge(pEntity);
			
			List<ProductRequestHibernateEntity> list = 
					pEntity.getItens();
			
			if(list != null){
				for(ProductRequestHibernateEntity e: list){
					if(e.getId() == null){
						e.setId(IDGenerator.getUniqueOrderID('R', value.getOwner()));
						entityManager.persist(e);
					}
					else{
						entityManager.merge(e);
					}
					
					List<ProductRequestDiscountHibernateEntity> prdel = e.getDiscounts();
					
					if(prdel != null){
						for(ProductRequestDiscountHibernateEntity k: prdel){
							if(k.getId() == null){
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
			
			List<OrderDiscountHibernateEntity> odl = pEntity.getDiscounts();
			
			if(odl != null){
				for(OrderDiscountHibernateEntity k: odl){
					if(k.getId() == null){
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
	protected OrderHibernateEntity toPersistenceEntity(Order entity)
			throws Throwable {
		return new OrderHibernateEntity(entity);
	}

	@Override
	protected Order toEntity(OrderHibernateEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Order entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(OrderHibernateEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Order value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		throw new UnsupportedOperationException();
	}

	public List<Order> getOrders(Integer owner, Integer first, Integer max)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderHibernateEntity> criteria = 
		    		builder.createQuery(OrderHibernateEntity.class);
		    Root<OrderHibernateEntity> from = 
		    		criteria.from(OrderHibernateEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("owner"), owner)
    				)
    		);
		    
	    	List<javax.persistence.criteria.Order> orderList = 
	    			new ArrayList<javax.persistence.criteria.Order>();
	    	orderList.add(builder.desc(from.get("date")));
	    	
		    TypedQuery<OrderHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderHibernateEntity> list = (List<OrderHibernateEntity>)typed.getResultList();
		    List<Order> result = new ArrayList<Order>();
    
		    for(OrderHibernateEntity e: list) {
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
		    CriteriaQuery<OrderHibernateEntity> criteria = 
		    		builder.createQuery(OrderHibernateEntity.class);
		    Root<OrderHibernateEntity> from = 
		    		criteria.from(OrderHibernateEntity.class);
		    
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
	    	
		    TypedQuery<OrderHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderHibernateEntity> list = (List<OrderHibernateEntity>)typed.getResultList();
		    List<Order> result = new ArrayList<Order>();
    
		    for(OrderHibernateEntity e: list) {
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
		    CriteriaQuery<OrderHibernateEntity> criteria = 
		    		builder.createQuery(OrderHibernateEntity.class);
		    Root<OrderHibernateEntity> from = 
		    		criteria.from(OrderHibernateEntity.class);
		    
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
	    	
		    TypedQuery<OrderHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<OrderHibernateEntity> list = (List<OrderHibernateEntity>)typed.getResultList();
		    List<Order> result = new ArrayList<Order>();
    
		    for(OrderHibernateEntity e: list) {
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
		    CriteriaQuery<ProductRequestHibernateEntity> criteria = 
		    		builder.createQuery(ProductRequestHibernateEntity.class);
		    Root<ProductRequestHibernateEntity> from = 
		    		criteria.from(ProductRequestHibernateEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("id"), id));
		    
		    Join<ProductRequestHibernateEntity, OrderHibernateEntity> orderJoin = from.join("order");
		    and.add(builder.equal(orderJoin.get("id"), orderID));
	    	
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<ProductRequestHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


		    ProductRequestHibernateEntity e = typed.getSingleResult();
		    
		    return e == null? null : e.toEntity();
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
		
	}
	
	public Order findByCartID(String id) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderHibernateEntity> criteria = 
		    		builder.createQuery(OrderHibernateEntity.class);
		    Root<OrderHibernateEntity> from = 
		    		criteria.from(OrderHibernateEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

	    	and.add(builder.equal(from.get("id"), id));
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
		    TypedQuery<OrderHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


		    OrderHibernateEntity e = typed.getSingleResult();
		    
		    return e == null? null : e.toEntity();
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
			
			OrderLogHibernateEntity e = new OrderLogHibernateEntity(log);
			entityManager.persist(e);
			
		}
		catch(Throwable e){
			throw new EntityAccessException(e);
		}
	}

	@Override
	public void updateLog(OrderLog log) throws EntityAccessException {
		try{
			OrderLogHibernateEntity e = new OrderLogHibernateEntity(log);
			entityManager.merge(e);
		}
		catch(Throwable e){
			throw new EntityAccessException(e);
		}
	}

	@Override
	public void deleteLog(OrderLog log) throws EntityAccessException {
		try{
			OrderLogHibernateEntity e = new OrderLogHibernateEntity(log);
			e = (OrderLogHibernateEntity)entityManager.merge(e);
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
		    CriteriaQuery<OrderLogHibernateEntity> criteria = 
		    		builder.createQuery(OrderLogHibernateEntity.class);
		    Root<OrderLogHibernateEntity> from = 
		    		criteria.from(OrderLogHibernateEntity.class);
		    
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
		    
		    TypedQuery<OrderLogHibernateEntity> typed = 
		    		entityManager.createQuery(criteria);


			if(first != null){
				typed.setFirstResult(first);
			}
			
			if(max != null){
				typed.setMaxResults(max);
			}
			
			List<OrderLogHibernateEntity> list = (List<OrderLogHibernateEntity>) typed.getResultList();
			List<OrderLog> result = new ArrayList<OrderLog>(5);
			for(OrderLogHibernateEntity e: list){
				result.add(e.toEntity());
			}
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}
		
	}

}
