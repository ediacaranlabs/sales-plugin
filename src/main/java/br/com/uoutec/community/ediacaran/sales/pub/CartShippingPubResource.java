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
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequiresRole;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller
@ActionStrategy(WebActionStrategyType.DETACHED)
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class CartShippingPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private AdminCart adminCart;

	@Transient
	@Inject
	private ClientRegistry clientRegistry;

	@Transient
	@Inject
	private VarParser varParser;
	
	@Transient
	@Inject
	private Cart cart;
	
	@Transient
	@Inject
	private CartService cartService;

	public CartShippingPubResource(){
	}
	
	/* show shipping */
	@Action("/cart/shipping")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/shipping")
	@RequestMethod(RequestMethodTypes.GET)
	@Result("vars")
	@RequiresRole({BasicRoles.USER, BasicRoles.CLIENT})
	public Map<String,Object> showShipping(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		Client client = cart.getClient();
		try{
			if(client == null) {
				if(authenticatedSystemUserPubEntity == null) {
					authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
				}
				
				SystemUser user = authenticatedSystemUserPubEntity.rebuild(true, false, false);
				client = clientRegistry.findClientById(user.getId());
			}
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showShipping", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		return showShipping(client, cart, cart.getShippingAddress(), locale);
	}

	@Action("${plugins.ediacaran.front.admin_context}/cart/shipping")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/shipping")
	@RequestMethod(RequestMethodTypes.GET)
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	public Map<String,Object> showShipping(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		return showShipping(adminCart.getClient(), adminCart.getCart(), adminCart.getShippingAddress(), locale);
	}

	private Map<String,Object> showShipping(Client client, Cart cart, Address shippingAddress, Locale locale) throws InvalidRequestException {

		try{
			Map<String,Object> vars = new HashMap<String, Object>();
			vars.put("shippingOptions", cartService.getShippingOptions(client, shippingAddress, null, cart));
			return vars;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartShippingPubResource.class, "showshipping", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
	/* select shipping */
	
	@Action("/cart/shipping/select")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_shipping_result")
	@RequestMethod(RequestMethodTypes.POST)
	@Result("vars")
	public Map<String,Object> selectShipping(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,			
			@Basic(bean = "shipping_method")
			String shippingMethod, 
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		Client client;
		
		try{
			if(authenticatedSystemUserPubEntity == null) {
				authenticatedSystemUserPubEntity = new AuthenticatedSystemUserPubEntity();
			}
			
			SystemUser user = authenticatedSystemUserPubEntity.rebuild(true, false, false);
			client = clientRegistry.findClientById(user.getId());
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showShipping", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		setShipping(shippingMethod, client, cart, locale);
		
		Map<String,Object> result = new HashMap<>();
		result.put("cart_widgets_address", varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/widgets"));
		result.put("cart_payment_details", varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/payment-details"));
		return result;
		
	}		
	
	@Action("${plugins.ediacaran.front.admin_context}/cart/shipping/select")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/select_shipping_result")
	@RequestMethod(RequestMethodTypes.POST)
	@Result("vars")
	public Map<String,Object> selectShipping(
			@Basic(bean = "shipping_method")
			String shippingMethod, 
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		setShipping(shippingMethod, adminCart.getClient(), adminCart.getCart(), locale);
		
		Map<String,Object> result = new HashMap<>();
		result.put("cart_widgets_address", varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/widgets"));
		result.put("cart_payment_details", varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/payment-details"));
		return result;
	}
	
	private void setShipping(
			String shippingMethod,
			Client client,
			Cart cart,
			Locale locale) throws InvalidRequestException {

		try{
			cartService.selectShippingOption(shippingMethod, client, cart);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "showShipping", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}		
	
}
