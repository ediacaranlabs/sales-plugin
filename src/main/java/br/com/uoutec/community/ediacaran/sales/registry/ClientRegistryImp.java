package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.security.DoPrivilegedException;
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

@Singleton
public class ClientRegistryImp 
	extends AbstractRegistry 
	implements ClientRegistry{

	public static final String basePermission = "app.registry.user.";

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
		try {
			SystemUser user =ContextSystemSecurityCheck.doPrivileged(()->{
				return systemUserRegistry.findById(id);
			});
			
			if(user == null) {
				return null;
			}
			
			EntityInheritanceManager entityInheritanceUtil = 
					EntityContextPlugin.getEntity(EntityInheritanceManager.class);
				
			return entityInheritanceUtil.getInstance(Client.class, user.getCountry().getIsoAlpha3(), user);
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
		
		try {
			SystemUserSearchResult usr = ContextSystemSecurityCheck.doPrivileged(()->{
				return systemUserRegistry.searchSystemUser(value);
			});
			
			
			EntityInheritanceManager entityInheritanceUtil = 
					EntityContextPlugin.getEntity(EntityInheritanceManager.class);
				
			List<Client> cl = new ArrayList<>();
			
			for(SystemUser e: usr.getItens()) {
				cl.add(entityInheritanceUtil.getInstance(Client.class, e.getCountry().getIsoAlpha3(), e));
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
	public void registerAddress(Address address) throws ClientRegistryException {
		try{
			if(address.getId() == null) {
				addressEntityAccess.save(address);
			}
			else {
				addressEntityAccess.update(address);
			}
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

	@Override
	public void removeAddress(Address address) throws ClientRegistryException {
		try{
			addressEntityAccess.delete(address);
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

	@Override
	public Address getAddressByID(int id) throws ClientRegistryException {
		try{
			return addressEntityAccess.findById(id);
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

	@Override
	public List<Address> getAddress(Client value, String type) throws ClientRegistryException {
		try{
			return addressEntityAccess.getList(value, type);
    	}
    	catch(Throwable e){
    		throw new ClientRegistryException(e);
    	}
	}

}
