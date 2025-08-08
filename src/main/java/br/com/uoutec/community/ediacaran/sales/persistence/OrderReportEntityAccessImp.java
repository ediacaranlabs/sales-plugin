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
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderReportEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestOrderReportEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class OrderReportEntityAccessImp 
	extends AbstractEntityAccess<OrderReport, OrderReportEntity>
	implements OrderReportEntityAccess {

	public OrderReportEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public OrderReportEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	public void save(OrderReport value) throws EntityAccessException {
		try{
			OrderReportEntity pEntity = this.toPersistenceEntity(value);

			if(value.getId() != null){
				throw new EntityAccessException("id");
			}
			
			entityManager.flush();
			
			pEntity.setId(IDGenerator.getUniqueOrderID('O', (int)SystemProperties.currentTimeMillis()));
			
			entityManager.persist(pEntity);

			List<ProductRequestOrderReportEntity> list = pEntity.getProducts();
			
			if(list != null){
				for(ProductRequestOrderReportEntity e: list){
					e.setOrderReport(pEntity);
					entityManager.persist(e);
				}
			}
			
			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public void update(OrderReport value) throws EntityAccessException {
		try{
			OrderReportEntity pEntity = this.toPersistenceEntity(value);
			
			pEntity = (OrderReportEntity)entityManager.merge(pEntity);

			List<ProductRequestOrderReportEntity> list = pEntity.getProducts();
			
			if(list != null){
				for(ProductRequestOrderReportEntity e: list){
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
	protected OrderReportEntity toPersistenceEntity(OrderReport entity)
			throws Throwable {
		return new OrderReportEntity(entity);
	}

	@Override
	protected OrderReport toEntity(OrderReportEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(OrderReport entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(OrderReportEntity value)
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

	@Override
	public List<OrderReport> findByOrder(String order) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<OrderReportEntity> criteria = builder.createQuery(OrderReportEntity.class);
		    Root<OrderReportEntity> from = criteria.from(OrderReportEntity.class);
		    Join<OrderReportEntity, OrderEntity> orderJoin = from.join("order");
		    
		    criteria.select(from);

		    List<Predicate> and = new ArrayList<Predicate>();
	    	and.add(builder.equal(orderJoin.get("id"), order));
		    
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
	    	
		    TypedQuery<OrderReportEntity> typed = entityManager.createQuery(criteria);

		    List<OrderReportEntity> list = (List<OrderReportEntity>)typed.getResultList();
		    List<OrderReport> result = new ArrayList<OrderReport>();
    
		    for(OrderReportEntity e: list) {
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
