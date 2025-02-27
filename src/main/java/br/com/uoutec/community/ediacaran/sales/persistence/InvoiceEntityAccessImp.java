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
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.InvoiceEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.InvoiceIndexEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.InvoiceTaxEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.OrderEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestTaxEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.entityaccess.jpa.entity.SystemUserEntity;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class InvoiceEntityAccessImp 
	extends AbstractEntityAccess<Invoice, InvoiceEntity>
	implements InvoiceEntityAccess{

	public InvoiceEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public InvoiceEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	public void save(Invoice value) throws EntityAccessException {
		try{
			InvoiceEntity pEntity = this.toPersistenceEntity(value);

			if(value.getId() != null){
				throw new EntityAccessException("id");
			}
			
			pEntity.setId(IDGenerator.getUniqueOrderID('O', (int)SystemProperties.currentTimeMillis()));
			
			entityManager.persist(pEntity);
			
			List<ProductRequestEntity> list = pEntity.getItens();
			
			if(list != null){
				for(ProductRequestEntity e: list){
					e.setInvoice(pEntity);
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
			
			List<InvoiceTaxEntity> taxes = pEntity.getTaxes();
			
			if(taxes != null) {
				for(InvoiceTaxEntity t: taxes) {
					t.setId(IDGenerator.getUniqueOrderID('T', (int)SystemProperties.currentTimeMillis()));
					t.setInvoice(pEntity);
					entityManager.persist(t);
				}
			}
			
			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public void update(Invoice value) throws EntityAccessException {
		try{
			InvoiceEntity pEntity = this.toPersistenceEntity(value);
			pEntity = (InvoiceEntity)entityManager.merge(pEntity);
			
			List<ProductRequestEntity> list = pEntity.getItens();
			
			if(list != null){
				for(ProductRequestEntity e: list){
					if(e.getId() == null){
						e.setInvoice(pEntity);
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
			
			List<InvoiceTaxEntity> taxes = pEntity.getTaxes();
			
			if(taxes != null) {
				for(InvoiceTaxEntity t: taxes) {
					if(t.getId() == null) {
						t.setId(IDGenerator.getUniqueOrderID('T', (int)SystemProperties.currentTimeMillis()));
						t.setInvoice(pEntity);
						entityManager.persist(t);
					}
					else {
						entityManager.merge(t);
					}
				}
			}
			
			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public void saveIndex(Invoice value, Client client) throws EntityAccessException {
		try{
			InvoiceIndexEntity pEntity = new InvoiceIndexEntity(value, client);
			entityManager.persist(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void updateIndex(Invoice value, Client client) throws EntityAccessException {
		try{
			InvoiceIndexEntity pEntity = new InvoiceIndexEntity(value, client);
			entityManager.merge(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void deleteIndex(Invoice value, Client client) throws EntityAccessException {
		try{
			InvoiceIndexEntity pEntity = new InvoiceIndexEntity(value, client);
			entityManager.remove(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public boolean ifIndexExist(Invoice value) throws EntityAccessException {
		try{
			Object o = entityManager.find(InvoiceIndexEntity.class, value.getId());
			return o != null;
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	@Override
	protected InvoiceEntity toPersistenceEntity(Invoice entity)
			throws Throwable {
		return new InvoiceEntity(entity);
	}

	@Override
	protected Invoice toEntity(InvoiceEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Invoice entity, Serializable id) throws Throwable {
		entity.setId((String) id);
	}

	@Override
	protected Serializable getPersistenceID(InvoiceEntity value)
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

	@Override
	public List<Invoice> findByOrder(String order, SystemUser user) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<InvoiceEntity> criteria = 
		    		builder.createQuery(InvoiceEntity.class);
		    Root<InvoiceEntity> from = criteria.from(InvoiceEntity.class);
		    Join<InvoiceEntity, OrderEntity> orderJoin = from.join("order");
		    Join<InvoiceEntity, SystemUserEntity> userJoin = from.join("client");
		    
		    criteria.select(from);

		    List<Predicate> and = new ArrayList<Predicate>();
	    	and.add(builder.equal(orderJoin.get("id"), order));
		    
	    	if(user != null) {
		    	and.add(builder.equal(userJoin.get("id"), user.getId()));
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
	    	
		    TypedQuery<InvoiceEntity> typed = entityManager.createQuery(criteria);

		    List<InvoiceEntity> list = (List<InvoiceEntity>)typed.getResultList();
		    List<Invoice> result = new ArrayList<Invoice>();
    
		    for(InvoiceEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}
	
	@Override
	public List<Invoice> getList(Integer first, Integer max, SystemUser user)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<InvoiceEntity> criteria = 
		    		builder.createQuery(InvoiceEntity.class);
		    Root<InvoiceEntity> from = criteria.from(InvoiceEntity.class);
		    Join<InvoiceEntity, SystemUserEntity> userJoin = from.join("client");
		    
		    criteria.select(from);

		    List<Predicate> and = new ArrayList<Predicate>();
		    
	    	if(user != null) {
		    	and.add(builder.equal(userJoin.get("id"), user.getId()));
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
	    	
		    TypedQuery<InvoiceEntity> typed = 
		    		entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<InvoiceEntity> list = (List<InvoiceEntity>)typed.getResultList();
		    List<Invoice> result = new ArrayList<Invoice>();
    
		    for(InvoiceEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}

	public List<InvoiceResultSearch> search(InvoiceSearch value, Integer first, Integer max) throws EntityAccessException {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<InvoiceIndexEntity> criteria =	builder.createQuery(InvoiceIndexEntity.class);
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
	    	
		    TypedQuery<InvoiceIndexEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<InvoiceIndexEntity> list = (List<InvoiceIndexEntity>)typed.getResultList();
		    List<InvoiceResultSearch> result = new ArrayList<InvoiceResultSearch>();
    
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
