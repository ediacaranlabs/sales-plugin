package br.com.uoutec.community.ediacaran.sales.payment;

import java.math.BigDecimal;

import br.com.uoutec.community.ediacaran.sales.entity.FreePayment;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class FreePaymentGateway extends  AbstractPaymentGateway{

	public FreePaymentGateway() {
		super("free", "Free Payment");
	}
	
	public void authorize(Payment payment, PaymentLocation location) throws PaymentGatewayException{
		
		if(payment.getTotal().compareTo(BigDecimal.ZERO) > 0){
			changePaymentStatus(payment, PaymentStatus.SUSPECTED_FRAUD);
			//throw new PaymentGatewayException(payment.getTotal().compareTo(BigDecimal.ZERO) + "> 0");
		}
		else {
			changePaymentStatus(payment, PaymentStatus.PENDING_PAYMENT);
		}
	}

	public void capture(Payment payment, PaymentLocation location) throws PaymentGatewayException{
		changePaymentStatus(payment, PaymentStatus.PAYMENT_RECEIVED);
	}
	
	@Override
	public String redirectView(PaymentRequest paymentRequest) throws PaymentGatewayException {
		return null;
	}

	@Override
	public String getOwnerView(PaymentRequest paymentRequest) throws PaymentGatewayException {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		String view = varParser.getValue("${plugins.ediacaran.sales.web_path}/default_template/front/panel/cart/order_free_payment.jsp");
		return view;
	}

	@Override
	public String getView() throws PaymentGatewayException {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		String view = varParser.getValue("${plugins.ediacaran.sales.web_path}:/default_template/front/panel/cart/free_payment.jsp");
		return view;
	}

	@Override
	public boolean isApplicable(PaymentRequest paymentRequest) {
		return paymentRequest.getPayment().getTotal().compareTo(BigDecimal.ZERO) == 0;
	}

	@Override
	public Payment toPayment(Cart cart) {
		FreePayment payment = new FreePayment();
		payment.setStatus(PaymentStatus.NEW);
		payment.setPaymentType(getId());
		payment.setTax(cart.getTax());
		payment.setDiscount(cart.getTotalDiscount());
		payment.setCurrency(cart.getCurrency());
		payment.setValue(cart.getSubtotal());
		payment.setTotal(cart.getTotal());
		return payment;
	}

}
