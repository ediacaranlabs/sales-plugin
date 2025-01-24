package br.com.uoutec.community.ediacaran.sales.pub;

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
import org.brandao.brutos.annotation.ActionStrategy;
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
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.annotation.web.WebActionStrategyType;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.ClientEntityTypes;
import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.entity.SystemUserSearchResult;
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.community.ediacaran.user.pub.entity.SystemUserSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserSearch;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller
@ActionStrategy(WebActionStrategyType.DETACHED)
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class CartClientPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private VarParser varParser;
	
	@Transient
	@Inject
	private ClientEntityTypes clientEntityTypes;

	@Transient
	@Inject
	private CountryRegistry countryRegistry;

	@Transient
	@Inject
	private ClientRegistry clientRegistry;
	
	@Transient
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	@Transient
	@Inject
	private AdminCart adminCart;

	@Transient
	@Inject
	private Cart cart;
	
	@Transient
	@Inject
	private CartService cartService;
	
	public CartClientPubResource(){
	}

	/* search client */
	
	@Action("${plugins.ediacaran.front.admin_context}/cart/client/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SEARCH)
	public SystemUserSearchResultPubEntity searchUsers(
			@DetachedName ClientSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){
		
		SystemUserSearch search;
		
		try {
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartClientPubResource.class, "searchUsers", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		try {
			SystemUserSearchResult result = systemUserRegistry.searchSystemUser(search);
			return new SystemUserSearchResultPubEntity(result, locale);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartClientPubResource.class, "searchUsers", "search", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	/* new client */
	
	@Action("${plugins.ediacaran.front.admin_context}/cart/client")
	@RequestMethod(RequestMethodTypes.GET)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public ResultAction newClient(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Client client = adminCart.getClient();
		if(client == null) {
			client = new Client();
		}
		
		return changeClientView(
				client, 
				"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/client",
				subjectProvider.getSubject().getPrincipal(), 
				locale,
				null);
	}
	
	/* edit client */
	
	@Action("/cart/client")
	@RequestMethod(RequestMethodTypes.GET)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public ResultAction editClient(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Client client = null;
		Throwable exception = null;
		
		try{
			if(authenticatedSystemUserPubEntity == null) {
				authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			}
			client = (Client)authenticatedSystemUserPubEntity.rebuild(true, false, false);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartClientPubResource.class, "showUser", "view", locale, ex);
			exception = new InvalidRequestException(error, ex);
		}
		
		return changeClientView(
				client, 
				"${plugins.ediacaran.sales.web_path}/cart/client", //POST
				null,
				locale,
				exception
		);
	}

	@Action("${plugins.ediacaran.front.admin_context}/cart/client/{client.protectedID}")
	@RequestMethod(RequestMethodTypes.GET)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public ResultAction editClient(
			@Basic(bean="client")
			ClientPubEntity clientPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Client client = null;
		Throwable exception = null;
		
		try{
			if(clientPubEntity == null) {
				clientPubEntity = new ClientPubEntity();
			}
			client = (Client)clientPubEntity.rebuild(clientPubEntity.getProtectedID() != null, false, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartClientPubResource.class, "showUser", "view", locale, ex);
			exception = new InvalidRequestException(error, ex);
		}
		
		return changeClientView(
				client, 
				"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/client", //POST
				subjectProvider.getSubject().getPrincipal(),
				locale,
				exception
		);
	}
	
	/* change form view */
	
	@Action("/cart/client")
	@RequestMethod(RequestMethodTypes.POST)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public ResultAction changeClientView(
			@Basic(bean="client")
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Client client = null;
		Throwable exception = null;
		
		try{
			if(authenticatedSystemUserPubEntity == null) {
				authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			}
			SystemUser user = authenticatedSystemUserPubEntity.rebuild(true, true, false);
			client = clientRegistry.toClient(user);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartClientPubResource.class, "showUser", "view", locale, ex);
			exception = new InvalidRequestException(error, ex);
		}
		
		return changeClientView(
				client, 
				"${plugins.ediacaran.sales.web_path}/cart/client", //POST
				null,
				locale,
				exception
		);
	}

	@Action("${plugins.ediacaran.front.admin_context}/cart/client")
	@RequestMethod(RequestMethodTypes.POST)
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public ResultAction changeClientView(
			@Basic(bean="client")
			ClientPubEntity clientPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Client client = null;
		Throwable exception = null;
		
		try{
			if(clientPubEntity == null) {
				clientPubEntity = new ClientPubEntity();
			}
			client = (Client)clientPubEntity.rebuild(clientPubEntity.getProtectedID() != null, true, false);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartClientPubResource.class, "showUser", "view", locale, ex);
			exception = new InvalidRequestException(error, ex);
		}
		
		return changeClientView(
				client, 
				"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/client", //POST
				subjectProvider.getSubject().getPrincipal(),
				locale,
				exception
		);
	}

	public ResultAction changeClientView(
			Client client,
			String reloadAddress,
			Principal principal,
			Locale locale, 
			Throwable throwable) throws InvalidRequestException {
		

		List<Country> countries = null;
		String view 			= null;
		boolean resolvedView 	= false;
		Throwable exception 	= throwable;
		
		try{
			countries		= countryRegistry.getAll(locale);
			view			= clientEntityTypes.getClientEntityView(client);
			resolvedView	= true;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartClientPubResource.class, "showUser", "view", locale, ex);
			exception = new InvalidRequestException(error, ex);
		}

		Map<String,Object> vars = new HashMap<String, Object>();
		vars.put("client",			client);
		vars.put("countries",		countries);
		vars.put("principal",		principal);
		vars.put("reloadAddress",	varParser.getValue(reloadAddress));
		vars.put("exception", 		exception);
		
		ResultAction ra = new ResultActionImp();
		ra.setView(view, resolvedView);
		ra.add("vars", vars);
		
		return ra;
	}
	
	/* select client */
	
	@Action("/cart/select/client")
	@RequestMethod(RequestMethodTypes.POST)
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_user_result")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public Map<String,Object> selectUser(
			@Basic(bean="client")
			AuthenticatedSystemUserPubEntity systemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Client client = null;
		try{
			if( systemUserPubEntity == null) {
				systemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			}
			SystemUser user = systemUserPubEntity.rebuild(systemUserPubEntity.getProtectedID() != null, true, true);
			client = clientRegistry.toClient(user);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showUser", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		 Map<String,Object> vars = selectClient(client, cart, locale);
		 vars.put("select_address_uri", varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/address/select"));
		 return vars;
		
	}

	@Action("${plugins.ediacaran.front.admin_context}/cart/select/client")
	@RequestMethod(RequestMethodTypes.POST)
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_user_result")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String,Object> selectUser(
			@Basic(bean="client")
			ClientPubEntity systemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Client client = null;
		try{
			client = (Client)systemUserPubEntity.rebuild(systemUserPubEntity.getProtectedID() != null, true, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showUser", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		 Map<String,Object> vars = selectClient(client, adminCart.getCart(), locale);
		 vars.put("select_address_uri", varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/address/select"));
		 return vars;
	}

	private Map<String,Object> selectClient(Client client, Cart cart, Locale locale) {

		try{
			cartService.selectClient(client, cart);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showUser", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		Map<String,Object> vars = new HashMap<String, Object>();
		vars.put("client", client);
		return vars;
		
	}
}
