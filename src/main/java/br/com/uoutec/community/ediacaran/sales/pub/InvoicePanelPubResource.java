package br.com.uoutec.community.ediacaran.sales.pub;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceEntitySearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.InvoicePanelPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.InvoiceSearchPanelPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.panel_context}/invoices", defaultActionName="/")
@ResponseErrors(rendered=false)
public class InvoicePanelPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private InvoiceRegistry invoiceRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/front/panel/invoice/index")
	@Result("vars")
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
							InvoicePanelPubResourceMessages.RESOURCE_BUNDLE,
							InvoicePanelPubResourceMessages.index.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	public synchronized Serializable search(
			@DetachedName InvoiceSearchPanelPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		InvoiceSearch search;
		try{
			search = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoicePanelPubResourceMessages.RESOURCE_BUNDLE,
							InvoicePanelPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		
		try{
			DateTimeFormatter dtaFormt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale);
			int page = request.getPage() == null? 0 : request.getPage();
			int firstResult = (page-1)*10;
			int maxResult = 11;
			List<InvoiceResultSearch> values = invoiceRegistry.searchInvoice(search, firstResult, maxResult);
			
			List<InvoiceEntitySearchResultPubEntity> result = values.stream()
					.map((e)->new InvoiceEntitySearchResultPubEntity(e, locale, dtaFormt)).collect(Collectors.toList());
			
			return new InvoiceSearchResultPubEntity(-1, page, result.size() > 10, result.size() > 10? result.subList(0, 9) : result);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							InvoicePanelPubResourceMessages.RESOURCE_BUNDLE,
							InvoicePanelPubResourceMessages.search.error.fail_load_entity,
							
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		
	}
	
	@Action("/show/{id}")
	@View("${plugins.ediacaran.sales.template}/front/panel/invoice/details")
	@Result("vars")
	public Map<String,Object> orderDetail(
			@DetachedName
			InvoicePanelPubEntity invoicePubEntity,
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
							InvoicePanelPubResourceMessages.RESOURCE_BUNDLE,
							InvoicePanelPubResourceMessages.details.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("invoice", invoice);
		return map;
	}
	
}
