package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

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

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.RefundPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.RefundSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.RefundSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.RefundRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/refunds", defaultActionName="/")
@ResponseErrors(rendered=false)
public class RefundAdminPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private RefundRegistry refundRegistry;

	@Transient
	@Inject
	private OrderRegistry orderRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/refund/index")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.REFUND.SHOW)
	public void index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@RequireAnyRole({BasicRoles.USER,BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.REFUND.SEARCH)
	@Result(mappingType = MappingTypes.OBJECT)
	public RefundSearchResultPubEntity search(
			@DetachedName RefundSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		RefundSearch search;
		try{
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							RefundAdminPubResourceMessages.RESOURCE_BUNDLE,
							RefundAdminPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
		try{
			RefundResultSearch result = refundRegistry.searchRefund(search);
			return result == null? null : new RefundSearchResultPubEntity(result, locale);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							RefundAdminPubResourceMessages.RESOURCE_BUNDLE,
							RefundAdminPubResourceMessages.search.error.fail_load_entity,
							
							locale);
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
	}
	
	@Action("/edit/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/refund/edit")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.REFUND.SHOW)
	public Map<String,Object> invoiceDetail(
			@DetachedName
			RefundPubEntity refundPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Refund refund;
		List<PaymentGateway> paymentGatewayList = null;
		PaymentGateway selectedPaymentGateway = null;
		
		try{
			refund = refundPubEntity.rebuild(true, false, false);
			selectedPaymentGateway = paymentGatewayRegistry.getPaymentGateway(refund.getRefundType());
			paymentGatewayList = Arrays.asList(selectedPaymentGateway);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							RefundAdminPubResourceMessages.RESOURCE_BUNDLE,
							RefundAdminPubResourceMessages.details.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("refund", refund);
		map.put("paymentGatewayList", paymentGatewayList);
		map.put("selectedPaymentGateway", selectedPaymentGateway);
		return map;
	}
	
	@Action("/new/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/refund/edit")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.REFUND.CREATE)
	public Map<String,Object> newRefund(
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
							RefundAdminPubResourceMessages.RESOURCE_BUNDLE,
							RefundAdminPubResourceMessages.new_shipping.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		Refund refund;
		List<PaymentGateway> paymentGatewayList = null;
		PaymentGateway selectedPaymentGateway = null;
		
		try{
			refund = refundRegistry.createRefund(order);
			selectedPaymentGateway = paymentGatewayRegistry.getPaymentGateway(refund.getRefundType());
			paymentGatewayList = Arrays.asList(selectedPaymentGateway);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							RefundAdminPubResourceMessages.RESOURCE_BUNDLE,
							RefundAdminPubResourceMessages.new_shipping.error.create_shipping, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();

		map.put("entity", refund);
		map.put("paymentGatewayList", paymentGatewayList);
		map.put("selectedPaymentGateway", selectedPaymentGateway);
		return map;
	}
	
	@Action("/save")
	@View("${plugins.ediacaran.sales.template}/admin/refund/result")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.REFUND.SAVE)
	public Map<String,Object> save(
			@DetachedName
			RefundPubEntity refundPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Refund refund;
		try{
			refund = refundPubEntity.rebuild(refundPubEntity.getId() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							RefundAdminPubResourceMessages.RESOURCE_BUNDLE,
							RefundAdminPubResourceMessages.edit.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			refund.setProducts(
				refund.getProducts().stream()
					.filter((e)->e.getUnits() > 0)
					.collect(Collectors.toList())
			);
			refundRegistry.registerRefund(refund);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							RefundAdminPubResourceMessages.RESOURCE_BUNDLE,
							RefundAdminPubResourceMessages.save.error.register, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("refund", refund);
		return map;
	}
	
}
