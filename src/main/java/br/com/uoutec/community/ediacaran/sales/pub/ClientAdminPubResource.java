package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.ResultAction;
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
import org.brandao.brutos.web.WebResultAction;
import org.brandao.brutos.web.WebResultActionImp;

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
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.security.RequiresRole;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.community.ediacaran.user.pub.manager.SystemUserManagerPubResourceMessages;
import br.com.uoutec.ediacaran.core.VarParser;
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
	private ClientEntityTypes clientEntityTypes;

	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	@Inject
	private ClientService clientService;

	@Inject
	private VarParser varParser;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/client/index")
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String, Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		try{
			Map<String,Object> vars = new HashMap<>();
			vars.put("countries", clientService.getCountries(locale));
			vars.put("principal", subjectProvider.getSubject().getPrincipal());
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
			ClientSearchResult result = clientService.searchClient(search);
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
	
	@Action({
		"/edit",
		"/edit/{client.protectedID}", 
		"/edit/{client.protectedID}/{type}"
	})
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public ResultAction edit(
			@Basic(bean = "client")
			ClientPubEntity systemUserPubEntity,
			@Basic(bean = "type")
			String type,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Map<String,Object> vars = new HashMap<String, Object>();
		String view             = null;
		boolean resolvedView    = false;
		Throwable exception     = null;
		
		try{
			Client client = (Client)systemUserPubEntity
					.rebuild(systemUserPubEntity.getProtectedID() != null, false, false);
			
			if("simplified".equals(type)){
				view         = clientEntityTypes.getClientEntityView(client);
				resolvedView = true;
			}
			else{
				vars.put("client_data_view", clientEntityTypes.getClientEntityView(client));
				vars.put("addresses",        clientService.getAddresses(client));
				
				view         = "${plugins.ediacaran.sales.template}/admin/client/edit";
				resolvedView = false;
			}
			
			vars.put("type",          "simplified");
			vars.put("client",        client);
			vars.put("countries",     clientService.getCountries(locale));
			vars.put("principal",     subjectProvider.getSubject().getPrincipal());
			vars.put("reloadAddress", varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/edit"));
			
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.edit.error.fail_load, 
							locale);
			
			view         = "${plugins.ediacaran.sales.template}/admin/client/edit";
			resolvedView = false;
			exception    = new InvalidRequestException(error, ex);
		}

		WebResultAction ra  = new WebResultActionImp();
		ra.setView(view, resolvedView);
		ra.add("vars", vars);
		ra.add("exception", exception);
		
		return ra;
	}

	@Action({"/address", "/address/{type}"})
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public ResultAction address(
			@Basic(bean="type")
			String type, 
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Map<String,Object> vars = new HashMap<String, Object>();
		String view             = null;
		boolean resolvedView    = false;
		Throwable exception     = null;
		
		try{
			if("simplified".equals(type)) {
				view = "${plugins.ediacaran.sales.template}/admin/client/address.jsp";
				resolvedView = true;
			}
			else {
				view = "${plugins.ediacaran.sales.template}/admin/client/address_group.jsp";
				resolvedView = true;
			}
			
			vars.put("countries", clientService.getCountries(locale));
			vars.put("principal", subjectProvider.getSubject().getPrincipal());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.edit.error.fail_load, 
							locale);
			view = "${plugins.ediacaran.sales.template}/admin/client/address_group.jsp";
			resolvedView = true;
			exception = new InvalidRequestException(error, ex);
		}

		WebResultAction ra  = new WebResultActionImp();
		ra.setView(view, resolvedView);
		ra.add("vars", vars);
		ra.add("exception", exception);
		
		return ra;
	}

	@Action({
		"/select-address/{client.protectedID}",
		"/select-address/{client.protectedID}/{type}"
	})
	@View("${plugins.ediacaran.sales.template}/admin/client/address_selected")
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String,Object> selectedAddress(
			@Basic(bean = "client")
			ClientPubEntity systemUserPubEntity,
			@Basic(bean = "type")
			String type,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		
		try{
			Client client = (Client) systemUserPubEntity.rebuild(true, false, false);
			Map<String,Object> vars = new HashMap<String, Object>();
			
			vars.put("type",   "simplified");
			vars.put("client", client);
			vars.put("addresses", clientService.getAddresses(client));
			vars.put("principal", subjectProvider.getSubject().getPrincipal());
			
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
			@Basic(bean="client")
			ClientPubEntity clientPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Map<String,Object> vars = new HashMap<String, Object>();
		String view             = null;
		boolean resolvedView    = false;
		Throwable exception     = null;
		
		try{
			Client client = (Client)clientPubEntity.rebuild(clientPubEntity.getProtectedID() != null, true, false);
			
			vars.put("client",    client);
			vars.put("countries", clientService.getCountries(locale));
			vars.put("principal", subjectProvider.getSubject().getPrincipal());
			vars.put("reloadAddress", varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/edit"));

			view = clientEntityTypes.getClientEntityView(client);
			resolvedView = true;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ClientAdminPubResourceMessages.RESOURCE_BUNDLE,
							ClientAdminPubResourceMessages.edit.error.fail_load, 
							locale);
			
			view = "${plugins.ediacaran.sales.template}/admin/client/user_data.jsp";
			resolvedView = true;
			exception = new InvalidRequestException(error, ex);
		}
		
		WebResultAction ra  = new WebResultActionImp();
		ra.setView(view, resolvedView);
		ra.add("vars", vars);
		ra.add("exception", exception);
		
		return ra;
		
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
			@Basic(bean="addresses", mappingType = MappingTypes.OBJECT)
			List<AddressPubEntity> addressesPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Client client;
		List<Address> addresses = new ArrayList<>();
		List<Address> removedAddresses = new ArrayList<>();
		
		try{
			
			if(addressesPubEntity != null) {
				for(AddressPubEntity e: addressesPubEntity) {
					if(e.getDeleted() == null || !e.getDeleted().booleanValue()) {
						addresses.add(e.rebuild(e.getProtectedID() != null, true, true));
					}
					else {
						removedAddresses.add(e.rebuild(e.getProtectedID() != null, true, true));
					}
				}
			}
			
			client = (Client)clientPubEntity
					.rebuild(clientPubEntity.getProtectedID() != null, true, true);
			
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
			clientService.registerClient(client, addresses, removedAddresses);
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
		vars.put("addresses", addresses);
		
		return vars;
	}
	
}
