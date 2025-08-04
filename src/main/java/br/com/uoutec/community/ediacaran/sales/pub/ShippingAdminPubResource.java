package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingResultSearch;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.CancelationShippingPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethod;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethodRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingRateRequest;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/shippings", defaultActionName="/")
@ResponseErrors(rendered=false)
public class ShippingAdminPubResource {

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
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/shipping/index")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.SHIPPING.SHOW)
	public void index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@RequireAnyRole({BasicRoles.USER,BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.SHIPPING.SEARCH)
	@Result(mappingType = MappingTypes.OBJECT)
	public ShippingSearchResultPubEntity search(
			@DetachedName ShippingSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		ShippingSearch search;
		try{
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
		try{
			ShippingResultSearch result = shippingRegistry.searchShipping(search);
			return result == null? null : new ShippingSearchResultPubEntity(result, locale);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.search.error.fail_load_entity,
							
							locale);
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
	}
	
	@Action("/edit/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/shipping/edit")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.SHOW)
	public Map<String,Object> invoiceDetail(
			@DetachedName
			ShippingPubEntity shippingPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Shipping shipping;
		List<ShippingMethod> shippingMethods = null;
		ShippingMethod selectedShippingMethod = null;
		try{
			shipping = shippingPubEntity.rebuild(true, false, false);
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
	
	@Action("/new/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/shipping/edit")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.SHIPPING.CREATE)
	public Map<String,Object> newShipping(
			@DetachedName
			OrderPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Order order;
		
		try{
			order = orderPubEntity.rebuild(true, false, false);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.new_shipping.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		Shipping shipping = null;
		List<ShippingMethod> shippingMethods = null;
		ShippingMethod selectedShippingMethod = null;
		try{
			shipping = shippingRegistry.toShipping(order);
			shippingMethods = shippingMethodRegistry.getShippingMethods(new ShippingRateRequest(shipping));
			selectedShippingMethod = shipping.getShippingType() == null? null : shippingMethodRegistry.getShippingMethod(shipping.getShippingType());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.new_shipping.error.create_shipping, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		//map.put("order", order);
		map.put("shipping", shipping);
		map.put("shippingMethods", shippingMethods);
		map.put("selectedShippingMethod", selectedShippingMethod);
		return map;
	}

	@Action("/shippingtype/select")
	@RequestMethod(RequestMethodTypes.POST)
	public ResultAction selectShippingType(
			@DetachedName
			ShippingPubEntity shippingPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		String view 					= null;
		boolean resolvedView 			= false;
		ShippingMethod shippingMethod	= null;
		Shipping shipping				= null;
		Throwable exception 			= null;
		
		try{
			shipping = shippingPubEntity.rebuild(shippingPubEntity.getId() != null, true, false);
			shippingMethod	= shippingMethodRegistry.getShippingMethod(shipping.getShippingType());
			view 			= shippingMethod.getView(shipping);
			resolvedView 	= true;
		}
		catch(Throwable ex){
			ex.printStackTrace();
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.select_shipping_type.error.fail_load_entity, 
							locale);
			exception    = new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		ResultAction ra = new ResultActionImp();
		
		if(view != null){
			ra.setView(view, resolvedView);
		}
		else{
			ra.setContentType(String.class);
			ra.setContent("");
		}
		
		ra.add("exception",			exception);
		ra.add("shipping",			shipping);
		ra.add("shippingMethod",	shippingMethod);
		
		return ra;
	}
	
	@Action("/save")
	@View("${plugins.ediacaran.sales.template}/admin/shipping/result")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.SHIPPING.SAVE)
	public Map<String,Object> save(
			@DetachedName
			ShippingPubEntity shippingPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Shipping shipping;
		try{
			shipping = shippingPubEntity.rebuild(shippingPubEntity.getId() != null, true, true);
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
			shipping.setProducts(
				shipping.getProducts().stream()
					.filter((e)->e.getUnits() > 0)
					.collect(Collectors.toList())
			);
			shippingRegistry.registerShipping(shipping);
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

	@Action("/cancel")
	@View("${plugins.ediacaran.sales.template}/admin/shipping/result")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.SHIPPING.CANCEL)
	public Map<String,Object> cancel(
			@DetachedName
			CancelationShippingPubEntity shippingPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Shipping shipping;
		try{
			shipping = shippingPubEntity.rebuild(true, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.cancel.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			shippingRegistry.cancelShipping(shipping, shippingPubEntity.getCancelJustification());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ShippingAdminPubResourceMessages.RESOURCE_BUNDLE,
							ShippingAdminPubResourceMessages.cancel.error.register, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shipping", shipping);
		return map;
	}
	
}
