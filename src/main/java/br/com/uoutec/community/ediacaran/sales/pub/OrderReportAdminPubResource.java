package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessageResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderReportMessagePubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderReportMessageSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderReportPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderReportSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderReportSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/orders/report", defaultActionName="/")
@ResponseErrors(rendered=false)
public class OrderReportAdminPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private OrderReportRegistry orderReportRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/order/report/index")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER,BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.REPORT.SHOW)
	public Map<String,Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("statusList", Arrays.asList(OrderReportStatus.values()));
		return map;
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@RequireAnyRole({BasicRoles.USER,BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.REPORT.SEARCH)
	@Result(mappingType = MappingTypes.OBJECT)
	public OrderReportSearchResultPubEntity search(
			@DetachedName OrderReportSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		OrderReportSearch search;
		try{
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportAdminPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
		try{
			OrderReportResultSearch result = orderReportRegistry.searchOrderReport(search);
			return result == null? null : new OrderReportSearchResultPubEntity(result, locale);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportAdminPubResourceMessages.search.error.fail_load_entity,
							
							locale);
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
	}
	
	@Action("/edit/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/order/report/edit")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER,BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.REPORT.SHOW)
	public Map<String,Object> detailedEntity(
			@DetachedName
			OrderReportPubEntity orderReportPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		OrderReport orderReport;
		try{
			orderReport = orderReportPubEntity.rebuild(true, false, false);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportAdminPubResourceMessages.details.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderReport", orderReport);
		map.put("statusList", Arrays.asList(OrderReportStatus.values()));
		return map;
	}
	
	@Action("/save")
	@View("${plugins.ediacaran.sales.template}/admin/order/report/result")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole({BasicRoles.USER,BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.REPORT.SAVE)
	public Map<String,Object> save(
			@DetachedName
			OrderReportPubEntity orderReportPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		OrderReport orderReport;
		try{
			orderReport = orderReportPubEntity.rebuild(orderReportPubEntity.getId() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportAdminPubResourceMessages.edit.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			orderReportRegistry.registerOrderReport(orderReport);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportAdminPubResourceMessages.save.error.register, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderReport", orderReport);
		return map;
	}

	@Action(value="/messages")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@RequireAnyRole({BasicRoles.USER,BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.REPORT.MESSAGE)
	@Result(mappingType = MappingTypes.OBJECT)
	public OrderReportMessageSearchResultPubEntity getMessages(
			@DetachedName OrderReportPubEntity orderReportPubEntity,
			@Basic(bean="page") Integer page,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){
		
		OrderReport orderReport;
		try{
			orderReport = orderReportPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportAdminPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
		try{
			OrderReportMessageResultSearch result = orderReportRegistry.getMessages(orderReport, page, null);
			return result == null? null : new OrderReportMessageSearchResultPubEntity(result, orderReport, locale);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportAdminPubResourceMessages.search.error.fail_load_entity,
							
							locale);
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
	}

	@Action("/sendMessage")
	@View("${plugins.ediacaran.sales.template}/admin/order/report/result_message")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER})
	@RequiresPermissions(SalesUserPermissions.ORDER.REPORT.MESSAGE)
	public Map<String,Object> sendMessage(
			@DetachedName
			OrderReportMessagePubEntity orderReportMessagePubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		OrderReportMessage orderReportMessage;
		try{
			orderReportMessage = orderReportMessagePubEntity.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportPanelPubResourceMessages.edit.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			OrderReport or = new OrderReport();
			or.setId(orderReportMessage.getOrderReport());
			orderReportRegistry.sendMessage(or, orderReportMessage.getMessage(), orderReportMessage.getUser());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportPanelPubResourceMessages.save.error.register, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderReportMessage", orderReportMessage);
		return map;
	}
	
}
