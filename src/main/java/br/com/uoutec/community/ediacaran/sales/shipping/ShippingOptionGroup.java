package br.com.uoutec.community.ediacaran.sales.shipping;

import java.math.BigDecimal;
import java.util.List;

public class ShippingOptionGroup extends ShippingOption{
	
	private List<ShippingOption> options;
	
	public ShippingOptionGroup() {
	}
	
	public ShippingOptionGroup(String id, String method, String title, String currency, List<ShippingOption> options) {
		super(id, method, title, currency, BigDecimal.ZERO, BigDecimal.ZERO);
		this.options = options;
	}
	
	public List<ShippingOption> getOptions() {
		return options;
	}

	public void setCost(BigDecimal cost) {
		throw new UnsupportedOperationException();
	}

	public void setValue(BigDecimal value) {
		throw new UnsupportedOperationException();
	}
	
	public BigDecimal getCost() {
		BigDecimal r = BigDecimal.ZERO;
		
		if(options == null) {
			return r;
		}
		
		for(ShippingOption o: options) {
			r = r.add(o.getCost());
		}
		
		return r;
	}

	public BigDecimal getValue() {
		BigDecimal r = BigDecimal.ZERO;
		
		if(options == null) {
			return r;
		}
		
		for(ShippingOption o: options) {
			r = r.add(o.getValue());
		}
		
		return r;
	}
}
