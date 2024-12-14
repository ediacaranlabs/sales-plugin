package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ClientRegistry extends PublicBean{

	void registerClient(Client entity) throws ClientRegistryException;
	
	void removeClient(Client entity) throws ClientRegistryException;
	
	Client findById(int id) throws ClientRegistryException;

	ClientSearchResult searchClient(ClientSearch value) throws ClientRegistryException;
	
	void registerAddress(Address address, Client client) throws ClientRegistryException;

	void removeAddress(Address address, Client client) throws ClientRegistryException;
	
	Address getAddressByID(int id) throws ClientRegistryException;
	
	Address getAddress(Client value, String type) throws ClientRegistryException;

	List<Address> getAddresses(Client value, String type) throws ClientRegistryException;
	
}
