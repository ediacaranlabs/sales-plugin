package br.com.uoutec.community.ediacaran.sales.pub;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentRequest;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPanelPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderResultSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderSearchPanelPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.InvoiceRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ShippingRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.community.ediacaran.user.SystemUserIDProvider;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
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
	private ClientRegistry clientRegistry;

	@Transient
	@Inject
	private OrderReportRegistry orderReportRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Transient
	@Inject
	private ShippingRegistry shippingRegistry;
	
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
			vars.put("statusList", Arrays.asList(OrderStatus.getValues()));
			
			return vars;
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.show_orders.error.fail_load_orders, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action(value="/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	public synchronized Serializable search(
			@DetachedName OrderSearchPanelPubEntity request,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale){

		
		OrderSearch orderSearch;
		try{
			orderSearch = request.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.search.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		
		try{
			OrderResultSearch values = orderRegistry.searchOrder(orderSearch);
			return new OrderResultSearchPubEntity(values, locale);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.show_orders.error.fail_load_orders, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		
	}
	
	@Action("/show/{id}")
	@View("${plugins.ediacaran.sales.template}/front/panel/order/details")
	@Result("vars")
	public Map<String,Object> orderDetail(
			@DetachedName
			OrderPanelPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Order order;
		List<Invoice> invoices;
		List<OrderReport> ordersReport;
		List<Shipping> shippings;
		
		//Client client;
		try{
			order = orderPubEntity.rebuild(true, false, true);
			//client = clientRegistry.findClientById(order.getClient().getId());
			invoices = invoiceRegistry.findByOrder(order.getId());
			shippings = shippingRegistry.findByOrder(order.getId());
			ordersReport = orderReportRegistry.findByOrder(order.getId());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		PaymentGateway paymentGateway = null;
		String view                   = null;
		String remotePayment          = null;
		
		try{
			paymentGateway = 
				paymentGatewayRegistry
					.getPaymentGateway(
							order.getPaymentType());
			
			if(paymentGateway != null){
				view = paymentGateway.getOwnerView(new PaymentRequest(order));
				remotePayment = paymentGateway.redirectView(new PaymentRequest(order));
			}
			
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_payment_gateway, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("order",           order);
		map.put("invoices",        invoices);
		map.put("orderReportList", ordersReport);
		map.put("shippings",      shippings);
		map.put("paymentGateway",  paymentGateway);
		map.put("payment_view",    view);
		map.put("remote_payment",  remotePayment);
		return map;
	}

	@Action("/widgets/pending-payment")
	@View("${plugins.ediacaran.sales.template}/front/panel/widgets/pending_payment_confirmation")
	@Result("vars")
	@RequireAnyRole({BasicRoles.CLIENT,BasicRoles.MANAGER,BasicRoles.USER})
	public Map<String,Object> productsWithPendingReceiptInLast60Days(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Client client;
		try{
			client = new Client();
			client.setId(getCurrentUserID());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		OrderResultSearch orders;
		try{
			orders = orderRegistry.searchProductsWithPendingPaymentLast6Days(client, null, null);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_payment_gateway, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orders", orders.getData());
		return map;
	}

	@Action("/widgets/completed")
	@View("${plugins.ediacaran.sales.template}/front/panel/widgets/completed_orders")
	@Result("vars")
	@RequireAnyRole({BasicRoles.CLIENT,BasicRoles.MANAGER,BasicRoles.USER})
	public Map<String,Object> completedOrdersLast30Days(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Client client;
		try{
			client = new Client();
			client.setId(getCurrentUserID());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		OrderResultSearch orders;
		try{
			orders = orderRegistry.searchCompletedOrdersLast30Days(client, null, null);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_payment_gateway, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orders", orders.getData());
		return map;
	}

	@Action("/widgets/paid")
	@View("${plugins.ediacaran.sales.template}/front/panel/widgets/paid_orders")
	@Result("vars")
	@RequireAnyRole({BasicRoles.CLIENT,BasicRoles.MANAGER,BasicRoles.USER})
	public Map<String,Object> paidOrders(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Client client;
		try{
			client = new Client();
			client.setId(getCurrentUserID());
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}

		OrderResultSearch orders;
		try{
			orders = orderRegistry.searchPaidOrders(client, null, null);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderPanelPubResourceMessages.order_detail.error.fail_load_payment_gateway, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orders", orders.getData());
		return map;
	}
	
	public Integer getCurrentUserID() throws SystemUserRegistryException {
		SystemUserRegistry systemUserRegistry = EntityContextPlugin.getEntity(SystemUserRegistry.class);
		Integer userID = systemUserRegistry.getIDBySystemID(SystemUserIDProvider.getSystemUserID());
		
		if(userID == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return userID;
	}
	
}
