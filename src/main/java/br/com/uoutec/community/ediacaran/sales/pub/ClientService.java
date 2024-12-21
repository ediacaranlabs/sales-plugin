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
public class ClientService {

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
		registerClient(entity, null, null, null);
	}

	@Transactional
	public void registerClient(Client entity, Address billingAddress, 
			List<Address> addShippingAddress) throws ClientRegistryException{
		registerClient(entity, billingAddress, addShippingAddress, null);
	}

	@Transactional
	public void registerClient(Client entity, Address billingAddress) throws ClientRegistryException{
		registerClient(entity, billingAddress, null, null);
	}

	@Transactional
	public void registerClient(Client entity, List<Address> addShippingAddress) throws ClientRegistryException{
		registerClient(entity, null, addShippingAddress, null);
	}
	
	@Transactional
	public void registerClient(Client entity, Address billingAddress, 
			List<Address> addShippingAddress, List<Address> removeShippingAddress) throws ClientRegistryException{
		
		clientRegistry.registerClient(entity);
		
		if(addShippingAddress != null) {
			for(Address e: addShippingAddress) {
				e.setType(Client.SHIPPING);
				clientRegistry.registerAddress(e, entity);
			}
		}
		
		if(removeShippingAddress != null) {
			for(Address e: removeShippingAddress) {
				clientRegistry.removeAddress(e, entity);
			}
		}
		
		if(billingAddress != null) {
			billingAddress.setType(Client.BILLING);
			clientRegistry.registerAddress(billingAddress, entity);
		}
		
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
