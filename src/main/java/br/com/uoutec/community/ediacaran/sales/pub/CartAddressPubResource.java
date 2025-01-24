package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.ActionStrategy;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.annotation.web.WebActionStrategyType;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.ClientEntityTypes;
import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.pub.entity.AddressPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller
@ActionStrategy(WebActionStrategyType.DETACHED)
@ResponseErrors(rendered=false)
public class CartAddressPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private ClientEntityTypes clientEntityTypes;

	@Transient
	@Inject
	private ClientRegistry clientRegistry;

	@Transient
	@Inject
	public VarParser varParser; 
	
	@Transient
	@Inject
	private Cart cart;

	@Transient
	@Inject
	private AdminCart adminCart;

	@Transient
	@Inject
	private CartService cartService;
	
	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	@Inject
	private ClientService clientService;

	/* show address form */
	
	@Action("/cart/address")
	@View("${plugins.ediacaran.sales.template}/front/panel/client/address")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public Map<String,Object> showAddress(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException, CountryRegistryException {
		
		return showAddress((Principal)null, locale);
	}

	@Action("${plugins.ediacaran.front.admin_context}/cart/address")
	@View("${plugins.ediacaran.sales.template}/front/panel/client/address")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String,Object> address(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException, CountryRegistryException {
		
		return showAddress(subjectProvider.getSubject().getPrincipal(), locale);
	}
	
	public Map<String,Object> showAddress(
			Principal principal,
			Locale locale) throws InvalidRequestException, CountryRegistryException {
		
		Map<String,Object> vars = new HashMap<String, Object>();
		vars.put("countries", clientService.getCountries(locale));
		vars.put("principal", principal);

		return vars;
	}
	
	/* select address for shipping */
	
	@Action("/cart/address/select")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_address")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public Map<String,Object> selectAddress(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Client client = cart.getClient();
		
		if(client == null) {
			try {
				if(authenticatedSystemUserPubEntity == null) {
					authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
				}
				SystemUser user = authenticatedSystemUserPubEntity.rebuild(true, false, false);
				client = clientRegistry.findClientById(user.getId());
				cart.setClient(client);
			}
			catch(Throwable ex) {
				String error = this.errorMappingProvider.getError(CartPubResource.class, "paymentDetails", "load", locale, ex);
				throw new InvalidRequestException(error, ex);
			}
			
		}
		
		return selectAddress(client, null, locale);
	}
	
	@Action("${plugins.ediacaran.front.admin_context}/cart/address/select")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_address")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String,Object> selectAddress(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		return selectAddress(adminCart.getClient(), subjectProvider.getSubject().getPrincipal(), locale);
	}

	public Map<String,Object> selectAddress(
			Client client,
			Principal principal,
			Locale locale) throws InvalidRequestException {
		
		try{
			Map<String,Object> vars = new HashMap<String, Object>();
			vars.put("client", client);
			vars.put("addresses", clientService.getAddresses(client));
			vars.put("principal", principal);
			vars.put("address_form", 
					principal == null? 
							varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/address") : 
							varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/address")
			);
			
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

	@Action("/cart/address/select")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_address_result")
	@RequestMethod(RequestMethodTypes.POST)
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public Map<String,Object> setAddress(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,
			@Basic(bean = "client.selectedBillingAddress")
			AddressPubEntity selectedBillingAddress, 
			@Basic(bean = "billingAddress")
			AddressPubEntity billingAddressPubEntity,
			@Basic(bean = "client.selectedShippingAddress")
			AddressPubEntity selectedShippingAddress, 
			@Basic(bean = "shippingAddress")
			AddressPubEntity shippingAddressPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		setAddress(selectedBillingAddress, billingAddressPubEntity, selectedShippingAddress, shippingAddressPubEntity, locale, cart);
		
		Map<String,Object> result = new HashMap<>();
		result.put("shipping_address", varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/shipping"));
		return result;
	}

	@Action("${plugins.ediacaran.front.admin_context}/cart/address/select")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_address_result")
	@RequestMethod(RequestMethodTypes.POST)
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.CLIENT.SHOW)
	public Map<String,Object> setAddress(
			@Basic(bean = "client.selectedBillingAddress")
			AddressPubEntity selectedBillingAddress, 
			@Basic(bean = "billingAddress")
			AddressPubEntity billingAddressPubEntity,
			@Basic(bean = "client.selectedShippingAddress")
			AddressPubEntity selectedShippingAddress, 
			@Basic(bean = "shippingAddress")
			AddressPubEntity shippingAddressPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		setAddress(selectedBillingAddress, billingAddressPubEntity, selectedShippingAddress, shippingAddressPubEntity, locale, adminCart.getCart());
		
		Map<String,Object> result = new HashMap<>();
		result.put("shipping_address", varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/shipping"));
		return result;
		
	}
	
	private void setAddress(
			AddressPubEntity selectedBillingAddress, 
			AddressPubEntity billingAddressPubEntity,
			AddressPubEntity selectedShippingAddress, 
			AddressPubEntity shippingAddressPubEntity,
			Locale locale,
			Cart cart) throws InvalidRequestException {

		Address shippingAddress = null;
		Address billingAddress = null;
		
		try {
			if(selectedBillingAddress != null) {
				billingAddress =
						"new".equals(selectedBillingAddress.getProtectedID())?
							billingAddressPubEntity.rebuild(false, true, true) :
							selectedBillingAddress.rebuild(true, false, false);	
			}

			if(selectedShippingAddress != null) {
				if("new".equals(selectedShippingAddress.getProtectedID())) {
					shippingAddress = shippingAddressPubEntity.rebuild(false, true, true);
				}
				else
				if("billing".equals(selectedShippingAddress.getProtectedID())) {
					shippingAddress = billingAddress;
				}
				else {
					shippingAddress = selectedShippingAddress.rebuild(true, false, false);
				}
			}
		}
		catch(Throwable ex) {
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "selectAddress", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		try{
			setAddress(billingAddress, shippingAddress, cart);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showAddress", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
		
	private void setAddress(
			Address billingAddress,
			Address shippingAddress,
			Cart cart) throws InvalidRequestException {

		cartService.selectAddress(billingAddress, shippingAddress, cart);
		
	}
		
}
