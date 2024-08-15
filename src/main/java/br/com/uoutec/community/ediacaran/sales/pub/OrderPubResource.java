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
import org.brandao.brutos.annotation.Intercept;
import org.brandao.brutos.annotation.InterceptedBy;
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
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.entity.AuthenticatedSystemUserPubEntity;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="/service-manager/orders", defaultActionName="/")
@ResponseErrors(rendered=false)
@InterceptedBy(@Intercept(name="securityStack"))
public class OrderPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private OrderRegistry orderRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayRegistry paymentGatewayProvider;
	
	@Action("/")
	@View(value="/service-manager/orders/index.jsp", resolved=true)
	@Result("orders")
	public List<Order> showOrders(
			@DetachedName
			AuthenticatedSystemUserPubEntity systemUserPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{
		
		SystemUser user;
		try{
			user = systemUserPubEntity.rebuild(true, false, false);
			return this.orderRegistry.getOrders(user, null, null);
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

	@Action("/show-order/{id}")
	@View(value="service-manager/orders/show_order")
	@Result("vars")
	public Map<String,Object> orderDetail(
			@DetachedName 
			AuthenticatedSystemUserPubEntity userPubEntity,
			@DetachedName
			OrderPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		Order order;
		SystemUser user;
		try{
			order = orderPubEntity.rebuild(true, false, true);
			user = userPubEntity.rebuild(true, false, false);
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
				paymentGatewayProvider
					.getPaymentGateway(
							order.getPayment().getPaymentType());
			
			if(paymentGateway != null){
				view = paymentGateway.getOwnerView(user, order);
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
		map.put("user",           user);
		map.put("paymentGateway", paymentGateway);
		map.put("payment_view",   view);
		return map;
	}
	
}
