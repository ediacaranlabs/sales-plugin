package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.ResultAction;
import org.brandao.brutos.ResultActionImp;
import org.brandao.brutos.annotation.AcceptRequestType;
import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.ResponseType;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.MediaTypes;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.ResponseErrors;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.ClientEntityTypes;
import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.sales.pub.entity.AddressPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.security.RequiresRole;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.manager.SystemUserManagerPubResourceMessages;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/clients", defaultActionName="/")
@ResponseErrors(rendered=false)
public class ClientAdminPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private ClientRegistry clientRegistry;
	
	@Transient
	@Inject
	private CountryRegistry countryRegistry;
	
	@Transient
	@Inject
	private ClientEntityTypes clientEntityTypes;

	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/client/index")
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String, Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> vars = new HashMap<>();
		try{
			vars.put("countries", countryRegistry.getAll(locale));
			return vars;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.index.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SEARCH)
	public synchronized ClientSearchResultPubEntity search(
			@DetachedName ClientSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		ClientSearch search;
		try{
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		
		try{
			ClientSearchResult result = clientRegistry.searchClient(search);
			return new ClientSearchResultPubEntity(result, locale);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.search.error.fail_load_entity,
							
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		
	}
	
	@Action({"/edit/{protectedID}", "/edit"})
	@View(value="${plugins.ediacaran.sales.template}/admin/client/edit")
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String,Object> edit(
			@DetachedName ClientPubEntity systemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		try{
			Map<String,Object> vars = new HashMap<String, Object>();
			boolean isNew = systemUserPubEntity.getProtectedID() == null;
			SystemUser systemUser   = systemUserPubEntity.rebuild(!isNew, false, false);
			
			vars.put("client",			systemUser);
			vars.put("countries",      countryRegistry.getAll(locale));
			vars.put("client_data_view", clientEntityTypes.getClientEntityView(systemUser));
			vars.put("billing_address", clientRegistry.getAddress((Client)systemUser, Client.BILLING));
			vars.put("shipping_addresses", clientRegistry.getAddresses((Client)systemUser, Client.SHIPPING));
			return vars;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.edit.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action({"/address", "/address/{type}"})
	@View(value="${plugins.ediacaran.sales.template}/admin/client/address")
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String,Object> address(
			@Basic(bean="type")
			String type, 
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		try{
			Map<String,Object> vars = new HashMap<String, Object>();
			vars.put("countries", countryRegistry.getAll(locale));
			return vars;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.edit.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	@Action("/edit")
	@RequestMethod("POST")
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public ResultAction editNewView(
			@DetachedName
			ClientPubEntity clientPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		try{
			Map<String,Object> vars = new HashMap<String, Object>();
			boolean isNew           = clientPubEntity.getProtectedID() == null;
			SystemUser systemUser   = clientPubEntity.rebuild(!isNew, true, false);
			
			vars.put("client",           systemUser);
			vars.put("countries",      countryRegistry.getAll(locale));
			vars.put("subject",        subjectProvider.getSubject());

			ResultAction ra = new ResultActionImp();
			ra.setView(clientEntityTypes.getClientEntityView(systemUser), true).add("vars", vars);
			return ra;
			
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.edit.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	@Action({"/save"})
	@RequestMethod("POST")
	@View("${plugins.ediacaran.sales.template}/admin/client/result")
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SAVE)
	public Map<String,Object> save(
			@Basic(bean="client")
			ClientPubEntity clientPubEntity,
			@Basic(bean="shippingAddress")
			List<AddressPubEntity> shippingAddresses,
			@Basic(bean="billingAddress")
			AddressPubEntity billingAddresses,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Client client;
		List<Address> shippingAddress = new ArrayList<>();
		List<Address> removedShippingAddress = new ArrayList<>();
		Address billingAddress = null;
		
		boolean isNew = true;
		try{
			isNew = clientPubEntity.getProtectedID() == null;
			client = (Client)clientPubEntity.rebuild(!isNew, true, true);
			
			if(shippingAddresses != null) {
				for(AddressPubEntity e: shippingAddresses) {
					if(e.getDeleted() == null || !e.getDeleted().booleanValue()) {
						shippingAddress.add(e.rebuild(e.getProtectedID() != null, true, true));
					}
					else {
						removedShippingAddress.add(e.rebuild(e.getProtectedID() != null, true, true));
					}
				}
			}
			
			if(billingAddresses != null) {
				billingAddress = billingAddresses.rebuild(billingAddresses.getProtectedID() != null, true, true);
			}
			
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							SystemUserManagerPubResourceMessages.RESOURCE_BUNDLE,
							SystemUserManagerPubResourceMessages.save.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		try{
			clientRegistry.registerClient(client);
			for(Address e: shippingAddress) {
				e.setType(Client.SHIPPING);
				clientRegistry.registerAddress(e, client);
			}
			for(Address e: removedShippingAddress) {
				clientRegistry.removeAddress(e, client);
			}
			
			if(billingAddress != null) {
				billingAddress.setType(Client.BILLING);
				clientRegistry.registerAddress(billingAddress, client);
			}
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.save.error.register, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> vars = new HashMap<>();
		vars.put("client", client);
		vars.put("shippingAddress", removedShippingAddress);
		vars.put("billingAddress", billingAddress);
		
		return vars;
	}
	
}
