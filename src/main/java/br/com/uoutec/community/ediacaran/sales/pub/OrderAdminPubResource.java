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

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResult;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequiresPermissions;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/orders", defaultActionName="/")
@ResponseErrors(rendered=false)
public class OrderAdminPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private OrderRegistry orderRegistry;

	@Transient
	@Inject
	private SystemUserRegistry systemUserRegistry;

	@Transient
	@Inject
	private ClientRegistry clientRegistry;
	
	@Transient
	@Inject
	private InvoiceRegistry invoiceRegistry;
	
	@Transient
	@Inject
	private ShippingRegistry shippingRegistry;

	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/order/index")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.ORDER.SHOW)
	public Map<String, Object> index(
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
							OrderAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderAdminPubResourceMessages.index.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.ORDER.SEARCH)
	public synchronized Serializable search(
			@DetachedName OrderSearchPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		OrderSearch orderSearch;
		try{
			orderSearch = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderAdminPubResourceMessages.search.error.fail_load, 
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
							OrderAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderAdminPubResourceMessages.search.error.fail_load, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		
	}

	@Action("/payment/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/order/payment_result")
	@Result("order")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.ORDER.PAYMENT)
	public Order payment(
			@DetachedName
			OrderPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
	
		Order order;
		try{
			order = orderPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderAdminPubResourceMessages.payment.error.fail_load, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		try{
			orderRegistry.registerPayment(order);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderAdminPubResourceMessages.payment.error.fail_register, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		return order;
	}
	
	@Action("/edit/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/order/edit")
	@Result("vars")
	@RequireAnyRole(BasicRoles.USER)
	@RequiresPermissions(SalesUserPermissions.ORDER.EDIT)
	public Map<String,Object> edit(
			@DetachedName
			OrderPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Order order;
		List<Invoice> invoices;
		Client client;
		List<Shipping> shippings;
		try{
			order = orderPubEntity.rebuild(true, false, true);
			invoices = invoiceRegistry.findByOrder(order.getId());
			client = clientRegistry.findClientById(order.getOwner());
			shippings = shippingRegistry.findByOrder(order.getId());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderAdminPubResourceMessages.edit.error.fail_load, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		PaymentGateway paymentGateway = null;
		String view                   = null;
		
		try{
			paymentGateway = 
				paymentGatewayRegistry
					.getPaymentGateway(
							order.getPaymentType());
			
			if(paymentGateway != null){
				view = paymentGateway.getOwnerView(new PaymentRequest(client, order.getPayment()));
			}
			
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderAdminPubResourceMessages.RESOURCE_BUNDLE,
							OrderAdminPubResourceMessages.edit.error.fail_load_payment_gateway, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("order",          order);
		map.put("invoices",       invoices);
		map.put("shippings",      shippings);
		map.put("paymentGateway", paymentGateway);
		map.put("payment_view",   view);
		return map;
	}
	
}
