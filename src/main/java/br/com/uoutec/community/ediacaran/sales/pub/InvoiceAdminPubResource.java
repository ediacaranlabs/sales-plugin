package br.com.uoutec.community.ediacaran.sales.pub;

import java.io.Serializable;
import java.util.HashMap;
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
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoicesResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.InvoicePubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.InvoiceRecalcPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.InvoiceSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.InvoiceSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/invoices", defaultActionName="/")
@ResponseErrors(rendered=false)
public class InvoiceAdminPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private InvoiceRegistry invoiceRegistry;
	
	@Transient
	@Inject
	private OrderRegistry orderRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/invoice/index")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.SHOW)
	public Map<String, Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> vars = new HashMap<>();
		try{
			return vars;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.index.error.fail_load, 
							locale);
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.SEARCH)
	public synchronized Serializable search(
			@DetachedName InvoiceSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		InvoiceSearch search;
		try{
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
		try{
			InvoicesResultSearch resultSearch = invoiceRegistry.searchInvoice(search);
			return new InvoiceSearchResultPubEntity(resultSearch, locale);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.search.error.fail_load_entity,
							
							locale);
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		
	}
	
	@Action("/show/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/invoice/details")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.SHOW)
	public Map<String,Object> invoiceDetail(
			@DetachedName
			InvoicePubEntity invoicePubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Invoice invoice;
		try{
			invoice = invoicePubEntity.rebuild(true, false, false);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.details.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("invoice", invoice);
		return map;
	}
	
	@Action("/new/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/invoice/edit")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.CREATE)
	public Map<String,Object> newInvoice(
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
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.new_invoice.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		Invoice invoice;
		try{
			invoice = invoiceRegistry.toInvoice(order);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.new_invoice.error.create_invoice, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("order", order);
		map.put("invoice", invoice);
		return map;
	}

	@Action("/recalc_invoice")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.EDIT)
	public InvoiceRecalcPubEntity recalc(
			@DetachedName
			InvoicePubEntity invoicePubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Invoice invoice;
		try{
			invoice = invoicePubEntity.rebuild(invoicePubEntity.getId() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.edit.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		return new InvoiceRecalcPubEntity(invoice);
	}
	
	@Action("/save")
	@View("${plugins.ediacaran.sales.template}/admin/invoice/result")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.SAVE)
	public Map<String,Object> save(
			@DetachedName
			InvoicePubEntity invoicePubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Invoice invoice;
		try{
			invoice = invoicePubEntity.rebuild(invoicePubEntity.getId() != null, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.edit.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			invoice.setItens(
				invoice.getItens().stream()
					.filter((e)->e.getUnits() > 0)
					.collect(Collectors.toList())
			);
			invoiceRegistry.registerInvoice(invoice);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.save.error.register, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("invoice", invoice);
		return map;
	}

	@Action("/cancel")
	@View("${plugins.ediacaran.sales.template}/admin/invoice/result")
	@Result("vars")
	@RequestMethod("POST")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.INVOICE.CANCEL)
	public Map<String,Object> cancel(
			@Basic(bean = "id")
			String id,
			@Basic(bean = "justification")
			String justification,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Invoice invoice;
		try{
			invoice = invoiceRegistry.findById(id);
			if(invoice == null) {
				throw new NullPointerException(id);
			}
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.cancel.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		try{
			invoiceRegistry.cancelInvoice(invoice, justification);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoiceAdminPubResourceMessages.RESOURCE_BUNDLE,
							InvoiceAdminPubResourceMessages.cancel.error.register, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("invoice", invoice);
		return map;
	}
	
}
