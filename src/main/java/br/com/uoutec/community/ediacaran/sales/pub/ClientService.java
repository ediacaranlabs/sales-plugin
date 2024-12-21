package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.List;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistryException;

public interface ClientService {

	List<Country> getCountries(Locale locale) throws CountryRegistryException;
	
	ClientSearchResult searchClient(ClientSearch value) throws ClientRegistryException;
	
	void registerClient(Client entity) throws ClientRegistryException;

	void registerClient(Client entity, Address billingAddress, 
			List<Address> addShippingAddress) throws ClientRegistryException;

	void registerClient(Client entity, Address billingAddress) throws ClientRegistryException;

	void registerClient(Client entity, List<Address> addShippingAddress) throws ClientRegistryException;
	
	void registerClient(Client entity, Address billingAddress, 
			List<Address> addShippingAddress, List<Address> removeShippingAddress) throws ClientRegistryException;
	
	void removeClient(Client entity) throws ClientRegistryException;
	
	Address getAddress(Client value, String type) throws ClientRegistryException;

	List<Address> getAddresses(Client value, String type) throws ClientRegistryException;
	
}
