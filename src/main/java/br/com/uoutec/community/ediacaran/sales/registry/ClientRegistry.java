package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.user.entity.Address;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.persistence.EntityAccessException;

public interface ClientRegistry extends PublicBean{

	void registerClient(Client entity) throws InvoiceRegistryException;
	
	void removeClient(Invoice entity) throws InvoiceRegistryException;
	
	Client findById(String id) throws InvoiceRegistryException;

	List<Client> searchClient(ClientSearch value) throws InvoiceRegistryException;
	
	void registerAddress(Address address, SystemUser value, String type) throws EntityAccessException;

	void removeAddress(Address address, SystemUser value) throws EntityAccessException;
	
	Address getAddressByID(int id) throws EntityAccessException;
	
	List<Address> getAddress(Client value, String type) throws EntityAccessException;
	
}
