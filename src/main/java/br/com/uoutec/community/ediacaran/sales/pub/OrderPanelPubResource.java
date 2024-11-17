package br.com.uoutec.community.ediacaran.sales.pub;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResult;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPanelPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderSearchAdminPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.panel_context}/orders", defaultActionName="/")
@ResponseErrors(rendered=false)
public class OrderPanelPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private OrderRegistry orderRegistry;

	@Transient
	@Inject
	private InvoiceRegistry invoiceRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/front/panel/order/index")
	@Result("vars")
	public Map<String, Object> showOrders(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		Map<String,Object> vars = new HashMap<>();
		try{
			List<Order> orders = this.orderRegistry.getOrders(null, null);
			vars.put("orders", orders);
			vars.put("statusList", Arrays.asList(OrderStatus.values()));
			
			return vars;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrdePanelrPubResourceMessages.RESOURCE_BUNDLE,
							OrdePanelrPubResourceMessages.show_orders.error.fail_load_orders, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	public synchronized Serializable search(
			@DetachedName OrderSearchAdminPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		OrderSearch orderSearch;
		try{
			orderSearch = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrdePanelrPubResourceMessages.RESOURCE_BUNDLE,
							OrdePanelrPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		
		try{
			DateTimeFormatter dtaFormt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withLocale(locale);
			int page = request.getPage() == null? 0 : request.getPage();
			int firstResult = (page-1)*10;
			int maxResult = 11;
			List<OrderResultSearch> values = orderRegistry.searchOrder(orderSearch, firstResult, maxResult);
			
			List<OrderResult> result = values.stream()
					.map((e)->new OrderResult(e, locale, dtaFormt)).collect(Collectors.toList());
			
			return new OrderSearchResult(-1, page, result.size() > 10, result.size() > 10? result.subList(0, 9) : result);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrdePanelrPubResourceMessages.RESOURCE_BUNDLE,
							OrdePanelrPubResourceMessages.show_orders.error.fail_load_orders, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		
	}
	
	@Action("/show/{id}")
	@View("${plugins.ediacaran.sales.template}/front/panel/order/details_order")
	@Result("vars")
	public Map<String,Object> orderDetail(
			@DetachedName
			OrderPanelPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Order order;
		List<Invoice> invoices;
		try{
			order = orderPubEntity.rebuild(true, false, true);
			invoices = invoiceRegistry.findByOrder(order.getId(), SystemUserRegistry.CURRENT_USER);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrdePanelrPubResourceMessages.RESOURCE_BUNDLE,
							OrdePanelrPubResourceMessages.order_detail.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		PaymentGateway paymentGateway = null;
		String view                   = null;
		
		try{
			paymentGateway = 
				paymentGatewayRegistry
					.getPaymentGateway(
							order.getPayment().getPaymentType());
			
			if(paymentGateway != null){
				view = paymentGateway.getOwnerView(order);
			}
			
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrdePanelrPubResourceMessages.RESOURCE_BUNDLE,
							OrdePanelrPubResourceMessages.order_detail.error.fail_load_payment_gateway, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("order",          order);
		map.put("invoices",       invoices);
		map.put("paymentGateway", paymentGateway);
		map.put("payment_view",   view);
		return map;
	}
	
}
