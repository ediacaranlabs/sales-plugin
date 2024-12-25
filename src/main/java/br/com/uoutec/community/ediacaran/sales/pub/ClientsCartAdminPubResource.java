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
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.ResponseType;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.web.MediaTypes;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.ClientEntityTypes;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ClientSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUserSearchResult;
import br.com.uoutec.community.ediacaran.user.pub.entity.SystemUserSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserSearch;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/cart/clients", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ClientsCartAdminPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private VarParser varParser;
	
	@Transient
	@Inject
	private AdminCart adminCart;
	
	@Transient
	@Inject
	private CartService cartService;

	@Transient
	@Inject
	private ClientEntityTypes clientEntityTypes;

	@Transient
	@Inject
	private CountryRegistry countryRegistry;

	@Transient
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Transient
	@Inject
	private ProductTypeRegistry productTypeRegistry;

	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	public ClientsCartAdminPubResource(){
	}

	@Action("/")
	@RequestMethod(RequestMethodTypes.GET)
	public ResultAction emptyClient(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		return client(null, false, false, locale);
	}
	
	@Action("/{client.protectedID}")
	@RequestMethod(RequestMethodTypes.GET)
	public ResultAction editClient(
			@Basic(bean="client")
			ClientPubEntity systemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		return client(systemUserPubEntity, false, false, locale);
	}
	
	@Action("/")
	@RequestMethod(RequestMethodTypes.POST)
	public ResultAction loadClient(
			@Basic(bean="client")
			ClientPubEntity systemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		return client(systemUserPubEntity, true, false, locale);
	}
	
	public ResultAction client(
			ClientPubEntity clientPubEntity,
			boolean override,
			boolean validate, 
			Locale locale) throws InvalidRequestException {
		

		try{
			if(clientPubEntity == null) {
				clientPubEntity = new ClientPubEntity();
			}
			
			Map<String,Object> vars = new HashMap<String, Object>();
			boolean isNew           = clientPubEntity.getProtectedID() == null;
			Client client           = (Client)clientPubEntity.rebuild(!isNew, override, validate);
			List<Country> countries = countryRegistry.getAll(locale);
			String userDataView     = clientEntityTypes.getClientEntityView(client);
			
			vars.put("client",			client);
			vars.put("countries",		countries);
			vars.put("principal",		subjectProvider.getSubject().getPrincipal());
			vars.put("reloadAddress",	varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/clients/"));
			
			//vars.put("user_data_view_updater",	varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/user"));

			ResultAction ra = new ResultActionImp();
			ra.setView(userDataView, true).add("vars", vars);
			return ra;
			
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ClientsCartAdminPubResource.class, "showUser", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action("/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	public SystemUserSearchResultPubEntity searchUsers(
			@DetachedName ClientSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){
		
		SystemUserSearch search;
		
		try {
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(ClientsCartAdminPubResource.class, "searchUsers", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		try {
			SystemUserSearchResult result = systemUserRegistry.searchSystemUser(search);
			return new SystemUserSearchResultPubEntity(result, locale);
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(ClientsCartAdminPubResource.class, "searchUsers", "search", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
	}
	
	public Cart getCart() {
		return adminCart.getCart();
	}
	
}
