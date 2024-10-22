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
import org.brandao.brutos.annotation.web.ResponseErrors;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/orders", defaultActionName="/")
@ResponseErrors(rendered=false)
public class OrderPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private OrderRegistry orderRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayRegistry;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/orders/index")
	@Result("orders")
	public List<Order> showOrders(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		try{
			return this.orderRegistry.getOrders(null, null);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPubResourceMessages.RESOURCE_BUNDLE,
							OrderPubResourceMessages.show_orders.error.fail_load_orders, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action("/show/{id}")
	@View("${plugins.ediacaran.sales.template}/admin/orders/show_order")
	@Result("vars")
	public Map<String,Object> orderDetail(
			@DetachedName
			OrderPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Order order;
		try{
			order = orderPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderPubResourceMessages.RESOURCE_BUNDLE,
							OrderPubResourceMessages.order_detail.error.fail_load_entity, 
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
							OrderPubResourceMessages.RESOURCE_BUNDLE,
							OrderPubResourceMessages.order_detail.error.fail_load_payment_gateway, 
							locale);
			
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("order",          order);
		map.put("paymentGateway", paymentGateway);
		map.put("payment_view",   view);
		return map;
	}
	
}
