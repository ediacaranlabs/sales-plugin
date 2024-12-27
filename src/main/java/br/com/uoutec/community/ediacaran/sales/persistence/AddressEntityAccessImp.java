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

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientAddress;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.AddressEntity;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class AddressEntityAccessImp 
	extends AbstractEntityAccess<Address, AddressEntity>
	implements AddressEntityAccess{

	public AddressEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public AddressEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	public Address getAddress(int id, Client value) throws EntityAccessException{
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<AddressEntity> criteria = builder.createQuery(AddressEntity.class);
		    Root<AddressEntity> from = criteria.from(AddressEntity.class);
		    
			    criteria.where(
		    		builder.and(
						builder.equal(from.get("systemUserID"), value.getId()),
						builder.equal(from.get("id"), id)
				));
		    
		    TypedQuery<AddressEntity> typed = entityManager.createQuery(criteria);			
	
		    AddressEntity e = (AddressEntity)typed.getSingleResult();
			return e == null? null : e.toEntity();
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}
	
	public List<Address> getList(Client value) throws EntityAccessException{
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<AddressEntity> criteria = builder.createQuery(AddressEntity.class);
		    Root<AddressEntity> from = criteria.from(AddressEntity.class);
		    
			    criteria.where(
		    		builder.and(
						builder.equal(from.get("systemUserID"), value.getId())
				));
		    
		    TypedQuery<AddressEntity> typed = entityManager.createQuery(criteria);			
	
		    List<AddressEntity> list = (List<AddressEntity>)typed.getResultList();
		    List<Address> result = new ArrayList<Address>();
		    
		    for(AddressEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}
	
	@Override
	protected AddressEntity toPersistenceEntity(Address entity)
			throws Throwable {
		if(entity instanceof ClientAddress) {
			Client c = new Client();
			c.setId(((ClientAddress)entity).getClient());
			return new AddressEntity(entity, c);
		}
		else {
			return new AddressEntity(entity, null);
		}
	}

	@Override
	protected Address toEntity(AddressEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Address entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(AddressEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Address value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

}
