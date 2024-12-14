package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.security.DoPrivilegedException;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.sales.persistence.AddressEntityAccess;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.entity.SystemUserSearchResult;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.AbstractRegistry;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
public class ClientRegistryImp 
	extends AbstractRegistry 
	implements ClientRegistry{

	@Inject
	private AddressEntityAccess addressEntityAccess;

	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	public ClientRegistryImp(){
	}

	@Override
	public void flush() {
		addressEntityAccess.flush();
	}

	@Override
	public void registerClient(Client entity) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.getRegisterPermission());
		
		try {
			ValidatorBean.validate(entity);
		}
		catch(ValidationException e) {
			throw new ClientRegistryException(e.getMessage(), e);
		}

		Map<String, String> dta = entity.getAddData();
		if(dta == null) {
			dta = new HashMap<>();
			entity.setAddData(dta);
		}
		
		dta.put("useDefaultBillingAddress", String.valueOf(entity.isUseDefaultBillingAddress()));
		dta.put("useDefaultShippingAddress", String.valueOf(entity.isUseDefaultShippingAddress()));
		
		try {
			ContextSystemSecurityCheck.doPrivileged(()->{
				systemUserRegistry.registerUser(entity);
				return null;
			});
			
		}
		catch(DoPrivilegedException ex) {
			throw new ClientRegistryException(ex.getCause());
		}
	}

	@Override
	public void removeClient(Client entity) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.getRemovePermission());
		
		try {
			ContextSystemSecurityCheck.doPrivileged(()->{
				systemUserRegistry.remove(entity);
				return null;
			});
		}
		catch(DoPrivilegedException ex) {
			throw new ClientRegistryException(ex.getCause());
		}
	}

	@Override
	public Client findById(int id) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.getFindPermission());
		
		try {
			SystemUser user =ContextSystemSecurityCheck.doPrivileged(()->{
				return systemUserRegistry.findById(id);
			});
			
			if(user == null) {
				return null;
			}
			
			EntityInheritanceManager entityInheritanceUtil = 
					EntityContextPlugin.getEntity(EntityInheritanceManager.class);
				
			return entityInheritanceUtil.getInstance(Client.class, user.getCountry().getIsoAlpha3(), new Class<?>[] {SystemUser.class}, new Object[] {user});
		}
		catch(DoPrivilegedException ex) {
			throw new ClientRegistryException(ex.getCause());
		}
		catch(Throwable ex) {
			throw new ClientRegistryException(ex.getCause());
		}
	}

	@Override
	public ClientSearchResult searchClient(ClientSearch value) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.getSearchPermission());
		
		try {
			SystemUserSearchResult usr = ContextSystemSecurityCheck.doPrivileged(()->{
				return systemUserRegistry.searchSystemUser(value);
			});
			
			
			EntityInheritanceManager entityInheritanceUtil = 
					EntityContextPlugin.getEntity(EntityInheritanceManager.class);
				
			List<Client> cl = new ArrayList<>();
			
			for(SystemUser e: usr.getItens()) {
				cl.add(entityInheritanceUtil.getInstance(Client.class, e.getCountry().getIsoAlpha3(), new Class<?>[] {SystemUser.class}, new Object[] {e}));
			}

			return new ClientSearchResult(usr.isHasNextPage(), usr.getMaxPages(), usr.getPage(), cl);
		}
		catch(DoPrivilegedException ex) {
			throw new ClientRegistryException(ex.getCause());
		}
		catch(Throwable ex) {
			throw new ClientRegistryException(ex.getCause());
		}
	}

	@Override
	public void registerAddress(Address address, Client client) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.ADDRESS.getRegisterPermission());
		
		try {
			ValidatorBean.validate(address);
		}
		catch(ValidationException e) {
			throw new ClientRegistryException(e.getMessage(), e);
		}
		
		try {
			if(address.getId() != null) {
				
				Address tmp = addressEntityAccess.findById(address.getId());
				
				if(tmp == null) {
					throw new ClientRegistryException("address not found: " + address.getId());
				}
				
				if(tmp.getOwner() != client.getId()) {
					throw new ClientRegistryException("invalid client: " + client.getId());
				}
				
			}
		}
		catch(EntityAccessException e) {
			throw new ClientRegistryException(e.getMessage(), e);
		}
		
		address.setOwner(client.getId());
		
		try{
			
			if(address.getId() == null) {
				addressEntityAccess.save(address);
			}
			else {
				addressEntityAccess.update(address);
			}
			addressEntityAccess.flush();
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

	@Override
	public void removeAddress(Address address, Client client) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.ADDRESS.getRemovePermission());
		
		try {
			if(address.getId() != null) {
				
				Address tmp = addressEntityAccess.findById(address.getId());
				
				if(tmp == null) {
					throw new ClientRegistryException("address not found: " + address.getId());
				}
				
				if(tmp.getOwner() != client.getId()) {
					throw new ClientRegistryException("invalid client: " + client.getId());
				}
				
			}
		}
		catch(EntityAccessException e) {
			throw new ClientRegistryException(e.getMessage(), e);
		}
		
		try{
			addressEntityAccess.delete(address);
			addressEntityAccess.flush();
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

	@Override
	public Address getAddressByID(int id) throws ClientRegistryException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.ADDRESS.getFindPermission());
		
		try{
			return addressEntityAccess.findById(id);
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

	@Override
	public Address getAddress(Client value, String type) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.ADDRESS.getListPermission());
		
		try{
			List<Address> list = addressEntityAccess.getList(value, type);
			return list == null || list.isEmpty()? null : list.get(0);
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}
	
	@Override
	public List<Address> getAddresses(Client value, String type) throws ClientRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.CLIENT_REGISTRY.ADDRESS.getListPermission());
		
		try{
			return addressEntityAccess.getList(value, type);
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

}
