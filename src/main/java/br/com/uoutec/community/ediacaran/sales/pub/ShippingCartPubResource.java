package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequiresRole;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="/cart/shipping", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ShippingCartPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private AdminCart adminCart;

	@Transient
	@Inject
	private Cart cart;
	
	@Transient
	@Inject
	private CartService cartService;

	public ShippingCartPubResource(){
	}
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/shipping")
	@RequestMethod(RequestMethodTypes.GET)
	@Result("vars")
	@RequiresRole({BasicRoles.USER, BasicRoles.CLIENT})
	public Map<String,Object> showAddressCart(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		try{
			Map<String,Object> vars = new HashMap<String, Object>();
			vars.put("shippingOptions", cartService.getShippingOptions(cart.getClient(), cart.getShippingAddress(), null, cart));
			return vars;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ShippingCartPubResource.class, "showshipping", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}

	@Action("/admin")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/shipping")
	@RequestMethod(RequestMethodTypes.GET)
	@Result("vars")
	@RequiresRole(BasicRoles.USER)
	public Map<String,Object> showAddressAdminCart(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		try{
			Map<String,Object> vars = new HashMap<String, Object>();
			vars.put("shippingOptions", cartService.getShippingOptions(adminCart.getClient(), adminCart.getShippingAddress(), null, adminCart.getCart()));
			return vars;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ShippingCartPubResource.class, "showshipping", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
}
