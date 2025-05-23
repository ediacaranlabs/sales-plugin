package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ClientRegistry extends PublicBean{

	void registerClient(Client entity) throws ClientRegistryException;
	
	void removeClient(Client entity) throws ClientRegistryException;
	
	Client findClientById(int id) throws ClientRegistryException;

	Client getClientBySystemID(String value) throws ClientRegistryException;
	
	ClientSearchResult searchClient(ClientSearch value) throws ClientRegistryException;
	
	Client toClient(SystemUser user) throws ClientRegistryException;
	
	void registerAddress(Address address, Client client) throws ClientRegistryException;

	void removeAddress(Address address, Client client) throws ClientRegistryException;

	void removeAddress(Client value) throws ClientRegistryException;
	
	Address getAddressByID(int id) throws ClientRegistryException;
	
	Address getAddressByID(int id, Client client) throws ClientRegistryException;
	
	List<Address> getAddresses(Client value) throws ClientRegistryException;
	
}
