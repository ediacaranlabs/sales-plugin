package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistryException;

@Singleton
public class ClientServiceImp implements ClientService{

	@Inject
	private CountryRegistry countryRegistry;
	
	@Inject
	private ClientRegistry clientRegistry;
	
	public List<Country> getCountries(Locale locale) throws CountryRegistryException {
		return countryRegistry.getAll(locale);
	}
	
	public ClientSearchResult searchClient(ClientSearch value) throws ClientRegistryException{
		return clientRegistry.searchClient(value);
	}
	
	@Transactional
	public void registerClient(Client entity) throws ClientRegistryException{
		registerClient(entity, null, null);
	}

	@Transactional
	public void registerClient(Client entity, 
			List<Address> addAddresses, List<Address> removeAddresses) throws ClientRegistryException{
		
		clientRegistry.registerClient(entity);
		
		if(addAddresses != null) {
			for(Address e: addAddresses) {
				e.setType(null);
				clientRegistry.registerAddress(e, entity);
			}
		}
		
		if(removeAddresses != null) {
			for(Address e: removeAddresses) {
				clientRegistry.removeAddress(e, entity);
			}
		}

		clientRegistry.registerClient(entity);
		
	}
	
	@Transactional
	public void removeClient(Client entity) throws ClientRegistryException{
		clientRegistry.registerClient(entity);
	}
	
	public Address getAddress(Client value, String type) throws ClientRegistryException{
		return clientRegistry.getAddress(value, type);
	}

	public List<Address> getAddresses(Client value, String type) throws ClientRegistryException{
		return clientRegistry.getAddresses(value, type);
	}
	
}
