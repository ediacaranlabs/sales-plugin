package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.persistence.EntityAccessException;

public interface AddressEntityAccess {
	
	void save(Address value) throws EntityAccessException;
	
	
	void update(Address entity) throws EntityAccessException;

	void delete(Address entity) throws EntityAccessException;
	
	Address findById(Serializable value) throws EntityAccessException ;
	
	
	List<Address> getList(Client value, String type) throws EntityAccessException;

	void flush();

	
}
