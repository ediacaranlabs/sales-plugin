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
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearch;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductMetadataAttributeEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductMetadataAttributeOptionEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductMetadataEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ProductMetadataEntityAccessImp 
	extends AbstractEntityAccess<ProductMetadata, ProductMetadataEntity>
	implements ProductMetadataEntityAccess{

	public ProductMetadataEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductMetadataEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	@Override
	protected ProductMetadataEntity toPersistenceEntity(ProductMetadata entity)
			throws Throwable {
		return new ProductMetadataEntity(entity);
	}

	@Override
	protected ProductMetadata toEntity(ProductMetadataEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(ProductMetadata entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductMetadataEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(ProductMetadata value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

	@Override
	public void save(ProductMetadata entity) throws EntityAccessException {
		try{
			ProductMetadataEntity pEntity = new ProductMetadataEntity(entity);
			entityManager.persist(pEntity);
			entityManager.flush();
			
			if(pEntity.getAttributes() != null) {
				
				List<ProductMetadataAttributeEntity> list = pEntity.getAttributes();
				
				for(ProductMetadataAttributeEntity e: list) {
					
					e.setProductMetadata(pEntity);
					entityManager.persist(e);
					entityManager.flush();
					
					if(e.getOptions() != null) {
						
						List<ProductMetadataAttributeOptionEntity> list2 = e.getOptions();
						
						for(ProductMetadataAttributeOptionEntity x: list2) {
							x.setProductAttribute(e);
							entityManager.persist(x);
						}
					}
					
				}
				
			}
			
			entityManager.flush();
			
			pEntity.toEntity(entity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	@Override
	public void update(ProductMetadata entity) throws EntityAccessException {
		try{
			ProductMetadataEntity pEntity = new ProductMetadataEntity(entity);
			
			pEntity = (ProductMetadataEntity)entityManager.merge(pEntity);
			entityManager.flush();
			
			if(pEntity.getAttributes() != null) {
				
				List<ProductMetadataAttributeEntity> list = pEntity.getAttributes();
				
				for(ProductMetadataAttributeEntity e: list) {
					
					e.setProductMetadata(pEntity);
					
					if(e.getId() == null) {
						entityManager.persist(e);
						entityManager.flush();
					}
					else {
						e = entityManager.merge(e);
					}
					
					if(e.getOptions() != null) {
						List<ProductMetadataAttributeOptionEntity> list2 = e.getOptions();
						
						for(ProductMetadataAttributeOptionEntity x: list2) {
							
							x.setProductAttribute(e);
							
							if(e.getId() == null) {
								entityManager.persist(x);
								entityManager.flush();
							}
							else {
								x = entityManager.merge(x);
							}
						}
					}
					
				}
				
				entityManager.flush();
			}
			
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	@Override
	public void delete(ProductMetadata entity) throws EntityAccessException {
		try{
			ProductMetadataEntity pEntity = new ProductMetadataEntity(entity);
			pEntity = (ProductMetadataEntity)entityManager.merge(pEntity);
			
			if(pEntity.getAttributes() != null) {
				
				List<ProductMetadataAttributeEntity> list = pEntity.getAttributes();
				
				for(ProductMetadataAttributeEntity e: list) {
					
					if(e.getOptions() != null) {
						
						List<ProductMetadataAttributeOptionEntity> list2 = e.getOptions();
						
						for(ProductMetadataAttributeOptionEntity x: list2) {
							x = (ProductMetadataAttributeOptionEntity)entityManager.merge(x);
							entityManager.remove(x);
						}
					}
					
					if(e.getId() == null) {
						e = (ProductMetadataAttributeEntity)entityManager.merge(e);
						entityManager.remove(e);
					}
					
					
				}
				
				entityManager.remove(pEntity);
				
				entityManager.flush();
			}
			
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	@Override
	public List<ProductMetadata> search(ProductMetadataSearch value, Integer first, Integer max)
			throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductMetadataEntity> criteria = builder.createQuery(ProductMetadataEntity.class);
		    Root<ProductMetadataEntity> from = 	criteria.from(ProductMetadataEntity.class);
		    
		    criteria.select(from);
		    
		    List<Predicate> and = new ArrayList<Predicate>();

		    if(value.getName() != null) {
		    	and.add(builder.equal(from.get("name"), StringUtil.toSearch(value.getName())));
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
	    	orderList.add(builder.asc(from.get("name")));
	    	
		    TypedQuery<ProductMetadataEntity> typed = entityManager.createQuery(criteria);


		    if(first != null) {
		    	typed.setFirstResult(first);
		    }
		    
		    if(max != null) {
			    typed.setMaxResults(max);		    	
		    }
		    
		    List<ProductMetadataEntity> list = (List<ProductMetadataEntity>)typed.getResultList();
		    List<ProductMetadata> result = new ArrayList<ProductMetadata>();
    
		    for(ProductMetadataEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}	
	}

	@Override
	public List<ProductMetadata> getAll() throws EntityAccessException {
		return super.findAll();
	}	
}
