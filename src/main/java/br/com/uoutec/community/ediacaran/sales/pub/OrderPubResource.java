package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.Intercept;
import org.brandao.brutos.annotation.InterceptedBy;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.ResponseErrors;

import br.com.uoutec.i18n.MessageBundleUtils;
import br.com.uoutec.i18n.MessageLocale;
import br.com.uoutec.portal.entity.Order;
import br.com.uoutec.portal.entity.SystemUser;
import br.com.uoutec.portal.payment.PaymentGateway;
import br.com.uoutec.portal.payment.PaymentGatewayProvider;
import br.com.uoutec.portal.registry.OrderRegistry;
import br.com.uoutec.portal.security.UserPrivilege;
import br.com.uoutec.portal.web.Constants;
import br.com.uoutec.portal.web.pub.admin.messages.OrderPubResourceMessages;
import br.com.uoutec.portal.web.pub.annotation.GuaranteedAccessTo;
import br.com.uoutec.portal.web.pub.entity.OrderPubEntity;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="/service-manager/orders", defaultActionName="/")
@GuaranteedAccessTo(UserPrivilege.class)
@ResponseErrors(rendered=false)
@InterceptedBy(@Intercept(name="securityStack"))
public class OrderPubResource {

	@Transient
	@Inject
	private OrderRegistry orderRegistry;
	
	@Transient
	@Inject
	private PaymentGatewayProvider paymentGatewayProvider;
	
	@Action("/")
	@View(value="/service-manager/orders/index.jsp", resolved=true)
	@Result("orders")
	public List<Order> showOrders(
			@Basic(bean=Constants.SYSTEM_USER_BEAN_NAME, 
			scope=Constants.SYSTEM_USER_SCOPE, mappingType=MappingTypes.VALUE)
			SystemUser systemUser) throws InvalidRequestException{
		
		try{
			return this.orderRegistry.getOrders(systemUser, null, null);
		}
		catch(Throwable ex){
			String error = MessageBundleUtils
					.getMessageResourceString(
							OrderPubResourceMessages.RESOURCE_BUNDLE,
							OrderPubResourceMessages.show_orders.error.fail_load_orders, 
							MessageLocale.getLocale());
			throw new InvalidRequestException(error, ex);
		}
	}

	@Action("/show-order/{id}")
	@View(value="service-manager/orders/show_order")
	@Result("vars")
	public Map<String,Object> orderDetail(
			@DetachedName
			OrderPubEntity orderPubEntity
			) throws InvalidRequestException{
		
		Order order;
		
		try{
			order = orderPubEntity.rebuild(true, false, true);
		}
		catch(Throwable ex){
			String error = MessageBundleUtils
					.getMessageResourceString(
							OrderPubResourceMessages.RESOURCE_BUNDLE,
							OrderPubResourceMessages.order_detail.error.fail_load_entity, 
							MessageLocale.getLocale());
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
				view = 
						paymentGateway
							.getPaymentUserView(
									orderPubEntity.getSystemUser(), 
									order);
			}
		}
		catch(Throwable ex){
			String error = MessageBundleUtils
					.getMessageResourceString(
							OrderPubResourceMessages.RESOURCE_BUNDLE,
							OrderPubResourceMessages.order_detail.error.fail_load_payment_gateway, 
							MessageLocale.getLocale());
			throw new InvalidRequestException(error, ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("order",          order);
		map.put("user",           orderPubEntity.getSystemUser());
		map.put("paymentGateway", paymentGateway);
		map.put("payment_view",   view);
		return map;
	}
	
}
