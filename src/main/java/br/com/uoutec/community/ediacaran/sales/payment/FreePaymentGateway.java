package br.com.uoutec.community.ediacaran.sales.payment;

import java.math.BigDecimal;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class FreePaymentGateway implements PaymentGateway{

	@Override
	public void payment(SystemUser user, Order order, Payment payment) throws PaymentGatewayException {
		
		if(payment.getTotal().compareTo(BigDecimal.ZERO) > 0){
			throw new PaymentGatewayException(payment.getTotal().compareTo(BigDecimal.ZERO) + "> 0");
		}
		
	}

	@Override
	public String redirectView(String orderID) throws PaymentGatewayException {
		return null;
	}

	@Override
	public String redirectView(SystemUser user, Order order) throws PaymentGatewayException {
		return null;
	}

	@Override
	public String getOwnerView(Order order) throws PaymentGatewayException {
		return null;
	}

	@Override
	public String getOwnerView(SystemUser user, Order order) throws PaymentGatewayException {
		return null;
	}

	@Override
	public String getView() throws PaymentGatewayException {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		String view = varParser.getValue("{plugins.ediacaran.sales.web_path}:/default_template/front/cart/free_payment.jsp");
		return view;
	}

	@Override
	public String getId() {
		return "free";
	}

	@Override
	public String getName() {
		return "Free Payment";
	}

	@Override
	public boolean isApplicable(Cart cart) {
		return cart.getTotal().compareTo(BigDecimal.ZERO) == 0;
	}

}
