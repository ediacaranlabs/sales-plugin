package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.ResponseErrors;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingPanelPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethod;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethodRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingRateRequest;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.panel_context}/shippings")
@ResponseErrors(rendered=false)
public class ShippingPanelPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private ShippingRegistry shippingRegistry;

	@Transient
	@Inject
	private OrderRegistry orderRegistry;
	
	@Transient
	@Inject
	private ShippingMethodRegistry shippingMethodRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Action("/show/{id}")
	@View("${plugins.ediacaran.sales.template}/front/panel/shipping/show")
	@Result("vars")
	@RequireAnyRole({BasicRoles.CLIENT,BasicRoles.MANAGER,BasicRoles.USER})
	public Map<String,Object> entityDetail(
			@DetachedName
			ShippingPanelPubEntity shippingPanelPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Shipping shipping;
		List<ShippingMethod> shippingMethods = null;
		ShippingMethod selectedShippingMethod = null;
		try{
			shipping = shippingPanelPubEntity.rebuild(true, false, false);
			shippingMethods = shippingMethodRegistry.getShippingMethods(new ShippingRateRequest(shipping));
			selectedShippingMethod = shippingMethodRegistry.getShippingMethod(shipping.getShippingType());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.details.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shipping", shipping);
		map.put("shippingMethods", shippingMethods);
		map.put("selectedShippingMethod", selectedShippingMethod);
		return map;
	}
	
	@Action("/confirm")
	@View("${plugins.ediacaran.sales.template}/front/panel/shipping/result")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole({BasicRoles.CLIENT,BasicRoles.MANAGER,BasicRoles.USER})
	public Map<String,Object> save(
			@DetachedName
			ShippingPanelPubEntity shippingPanelPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Shipping shipping;
		try{
			shipping = shippingPanelPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.edit.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			shippingRegistry.confirmShipping(shipping);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.save.error.register, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shipping", shipping);
		return map;
	}
	
}
