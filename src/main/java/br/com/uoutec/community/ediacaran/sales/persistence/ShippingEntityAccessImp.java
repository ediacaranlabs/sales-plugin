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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestTaxEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ShippingEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ShippingIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ShippingEntityAccessImp 
	extends AbstractEntityAccess<Shipping, ShippingEntity>
	implements ShippingEntityAccess{

	public ShippingEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ShippingEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	public void save(Shipping value) throws EntityAccessException {
		try{
			ShippingEntity pEntity = this.toPersistenceEntity(value);

			if(value.getId() != null){
				throw new EntityAccessException("id");
			}
			
			if(pEntity.getOrigin() != null) {
				entityManager.persist(pEntity.getOrigin());
			}

			if(pEntity.getDest() != null) {
				entityManager.persist(pEntity.getDest());
			}
			
			entityManager.flush();
			
			pEntity.setId(IDGenerator.getUniqueOrderID('O', (int)SystemProperties.currentTimeMillis()));
			
			entityManager.persist(pEntity);

			List<ProductRequestEntity> list = pEntity.getProducts();
			
			if(list != null){
				
				for(ProductRequestEntity e: list){
					e.setShipping(pEntity);
					e.setId(IDGenerator.getUniqueOrderID('R', (int)SystemProperties.currentTimeMillis()));
					entityManager.persist(e);
					
					List<ProductRequestTaxEntity> prdel = e.getTaxes();
					
					if(prdel != null){
						for(ProductRequestTaxEntity k: prdel){
							k.setProductRequest(e);
							k.setId(IDGenerator.getUniqueOrderID('D', (int)SystemProperties.currentTimeMillis()));
							entityManager.persist(k);
						}
					}
				}
				
			}
			
			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public void update(Shipping value) throws EntityAccessException {
		try{
			ShippingEntity pEntity = this.toPersistenceEntity(value);
			
			if(pEntity.getOrigin() != null) {
				if(pEntity.getOrigin().getId() == null){
					entityManager.persist(pEntity.getOrigin());
				}
				else{
					pEntity.setOrigin(entityManager.merge(pEntity.getOrigin()));
				}
			}

			if(pEntity.getDest() != null) {
				if(pEntity.getDest().getId() == null){
					entityManager.persist(pEntity.getDest());
				}
				else{
					pEntity.setDest(entityManager.merge(pEntity.getDest()));
				}
			}
			
			entityManager.flush();
			
			pEntity = (ShippingEntity)entityManager.merge(pEntity);

			List<ProductRequestEntity> list = pEntity.getProducts();
			
			if(list != null){
				for(ProductRequestEntity e: list){
					if(e.getId() == null){
						e.setShipping(pEntity);
						e.setId(IDGenerator.getUniqueOrderID('R', (int)SystemProperties.currentTimeMillis()));
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
								k.setId(IDGenerator.getUniqueOrderID('D', (int)SystemProperties.currentTimeMillis()));
								entityManager.persist(k);
							}
							else{
								entityManager.merge(k);
							}
						}
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
	protected ShippingEntity toPersistenceEntity(Shipping entity)
			throws Throwable {
		return new ShippingEntity(entity);
	}

	@Override
	protected Shipping toEntity(ShippingEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Shipping entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(ShippingEntity value)
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

	@Override
	public List<Shipping> findByOrder(String order, Client user) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ShippingEntity> criteria = 
		    		builder.createQuery(ShippingEntity.class);
		    Root<ShippingEntity> from = criteria.from(ShippingEntity.class);
		    Join<ShippingEntity, OrderEntity> orderJoin = from.join("order");
		    
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
	    	
		    TypedQuery<ShippingEntity> typed = entityManager.createQuery(criteria);

		    List<ShippingEntity> list = (List<ShippingEntity>)typed.getResultList();
		    List<Shipping> result = new ArrayList<Shipping>();
    
		    for(ShippingEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}
	
	@Override
	public List<Shipping> getList(Integer first, Integer max, Client user)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ShippingEntity> criteria = 
		    		builder.createQuery(ShippingEntity.class);
		    Root<ShippingEntity> from = criteria.from(ShippingEntity.class);
		    
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
	    	
		    TypedQuery<ShippingEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<ShippingEntity> list = (List<ShippingEntity>)typed.getResultList();
		    List<Shipping> result = new ArrayList<Shipping>();
    
		    for(ShippingEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

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

	@Override
	public Shipping findById(String id) throws EntityAccessException {
		return super.findById(id);
	}

}
