package br.com.uoutec.community.ediacaran.sales.payment;

import java.math.BigDecimal;

import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class FreePaymentGateway implements PaymentGateway{

	private final String id;
	
	public FreePaymentGateway() {
		this.id = "free";
	}
	
	@Override
	public void payment(PaymentRequest paymentRequest) throws PaymentGatewayException {
		
		if(paymentRequest.getTotal().compareTo(BigDecimal.ZERO) > 0){
			throw new PaymentGatewayException(paymentRequest.getTotal().compareTo(BigDecimal.ZERO) + "> 0");
		}
		
	}

	@Override
	public String redirectView(PaymentRequest paymentRequest) throws PaymentGatewayException {
		return null;
	}

	@Override
	public String getOwnerView(PaymentRequest paymentRequest) throws PaymentGatewayException {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		String view = varParser.getValue("${plugins.ediacaran.sales.web_path}/default_template/front/cart/order_free_payment.jsp");
		return view;
	}

	@Override
	public String getView() throws PaymentGatewayException {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		String view = varParser.getValue("${plugins.ediacaran.sales.web_path}:/default_template/front/cart/free_payment.jsp");
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
	public boolean isApplicable(PaymentRequest paymentRequest) {
		return paymentRequest.getTotal().compareTo(BigDecimal.ZERO) == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FreePaymentGateway other = (FreePaymentGateway) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
