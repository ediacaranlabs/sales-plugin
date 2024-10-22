package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.security.RuntimeSecurityPermission;

@Singleton
public class PaymentGatewayRegistryImp implements PaymentGatewayRegistry {

	public static final String basePermission = "app.registry.sales.payment.";

	private Logger logger = LoggerFactory.getLogger(PaymentGatewayRegistry.class);
	
	private ConcurrentMap<String, PaymentGateway> map;
	
	public PaymentGatewayRegistryImp() {
		this.map = new ConcurrentHashMap<>();
	}
	
	@Override
	public void flush() {
	}

	@Override
	public void registry(PaymentGateway paymentGateway) throws PaymentGatewayException {

		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "register"));

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
	public void remove(String value) throws PaymentGatewayException {

		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "remove"));

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
	public List<PaymentGateway> getPaymentGateways() {
		return map.values().stream().collect(Collectors.toList());
	}

}
