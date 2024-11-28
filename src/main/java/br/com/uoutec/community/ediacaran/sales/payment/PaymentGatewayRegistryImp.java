package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

@Singleton
public class PaymentGatewayRegistryImp implements PaymentGatewayRegistry {

	private Logger logger = LoggerFactory.getLogger(PaymentGatewayRegistry.class);
	
	private ConcurrentMap<String, PaymentGateway> map;
	
	public PaymentGatewayRegistryImp() {
		this.map = new ConcurrentHashMap<>();
	}
	
	@Override
	public void flush() {
	}

	@Override
	public void registerPaymentGateway(PaymentGateway paymentGateway) throws PaymentGatewayException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PAYMENT_GATEWAY.getRegisterPermission());

		PaymentGateway oldPaymentGateway = map.put(paymentGateway.getId().toLowerCase(), paymentGateway);
		
		if(logger.isTraceEnabled()) {
			if(oldPaymentGateway != null) {
				logger.trace("payment gateway has overridden: old=" + oldPaymentGateway + ", new=" + paymentGateway);
			}
			else {
				logger.trace("payment gateway has added: " + paymentGateway);
			}

		}
	}

	@Override
	public void removePaymentGateway(String value) throws PaymentGatewayException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PAYMENT_GATEWAY.getRemovePermission());

		PaymentGateway oldPaymentGateway;
		
		if((oldPaymentGateway = map.remove(value)) != null) {
			if(logger.isTraceEnabled()) {
				logger.trace("payment gateway has removed: " + oldPaymentGateway);
			}
		}
		
	}

	@Override
	public PaymentGateway getPaymentGateway(String code) {
		return map.get(code);
	}

	@Override
	public List<PaymentGateway> getPaymentGateways(Cart cart, SystemUser user) {
		List<PaymentGateway> result = new ArrayList<>();
		map.values().stream().forEach((e)->{
			if(e.isApplicable(cart, user)) {
				result.add(e);
			}
		});
		return result;
	}

}
