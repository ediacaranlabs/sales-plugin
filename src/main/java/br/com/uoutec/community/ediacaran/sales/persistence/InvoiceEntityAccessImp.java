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
import javax.persistence.criteria.Root;

import br.com.uoutec.application.SystemProperties;
import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.InvoiceEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductRequestTaxEntity;
import br.com.uoutec.community.ediacaran.system.util.IDGenerator;
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
			
			pEntity.toEntity(value);
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
	public List<Invoice> getList(Integer first, Integer max)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<InvoiceEntity> criteria = 
		    		builder.createQuery(InvoiceEntity.class);
		    Root<InvoiceEntity> from = 
		    		criteria.from(InvoiceEntity.class);
		    
		    criteria.select(from);
		    
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

}
