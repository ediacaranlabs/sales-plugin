package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.ResultAction;
import org.brandao.brutos.ResultActionImp;
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

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
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
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller
@ActionStrategy(WebActionStrategyType.DETACHED)
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class CartPaymentPubResource {

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

	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	public CartPaymentPubResource(){
	}
	
	/* payment details */
	
	@Action("/cart/payment-details")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/payment_details")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public Map<String, Object> paymentDetails(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,			
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
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
		
		Map<String, Object> vars = paymentDetails(cart, client, null, locale);
		vars.put("payment_gateway_uri_base", varParser.getValue("${plugins.ediacaran.sales.web_path}/cart/payment-type"));
		return vars;
	}
	
	@Action("${plugins.ediacaran.front.admin_context}/cart/payment-details")
	@View("${plugins.ediacaran.sales.template}/front/panel/cart/payment_details")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.CREATE)
	public Map<String, Object> paymentDetails(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String, Object> vars =  paymentDetails(adminCart.getCart(), adminCart.getClient(), subjectProvider.getSubject().getPrincipal(), locale);
		vars.put("payment_gateway_uri_base", varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/payment-type"));
		return vars;
		
	}
	
	private Map<String, Object> paymentDetails(
			Cart cart,
			Client client,
			Principal principal,
			Locale locale) throws InvalidRequestException{
		
		try{
			Map<String,Object> result = new HashMap<String, Object>();
			
			result.put("user", 					client);
			result.put("payment_gateway_list",	cartService.getPaymentGateways(cart, client));
			result.put("principal",				principal);
			
			return result;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartAdminPubResource.class, "paymentDetails", "load", locale,  ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}
	
	
	/* show payment gateway */
	
	@Action("/cart/payment-type/{code}")
	@RequestMethod({RequestMethodTypes.GET, RequestMethodTypes.POST})
	@ResponseErrors(rendered=false, name="exception")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.CLIENT, BasicRoles.MANAGER})
	public ResultAction paymentType(
			@DetachedName
			AuthenticatedSystemUserPubEntity authenticatedSystemUserPubEntity,			
			@Basic(bean="code")
			String code,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		return paymentType(code, cart, locale);
	}
	
	@Action("${plugins.ediacaran.front.admin_context}/cart/payment-type/{code}")
	@RequestMethod({RequestMethodTypes.GET, RequestMethodTypes.POST})
	@ResponseErrors(rendered=false, name="exception")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.CREATE)
	public ResultAction paymentType(
			@Basic(bean="code")String code,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		return paymentType(code, adminCart.getCart(), locale);
	}
	
	private ResultAction paymentType(String code, Cart cart, Locale locale) throws InvalidRequestException{
			
		try{
			PaymentGateway pg = cartService.getPaymentGateway(code);

			if(pg == null){
				throw new IllegalStateException("não foi encontrado o gateway de pagamento: " + code);
			}
			
			if(cart == null){
				throw new IllegalStateException("carrinho não encontrado");
			}
	
			String view = pg.getView();
			
			ResultAction ra = new ResultActionImp();
			
			if(view != null){
				ra.setView(view, true);
			}
			else{
				ra.setContentType(String.class);
				ra.setContent("");
			}
			
			ra.add("paymentGateway", pg);
			ra.add("cart",           cart);
			return ra;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(CartPubResource.class, "paymentType", "paymentLoad", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}	
}
