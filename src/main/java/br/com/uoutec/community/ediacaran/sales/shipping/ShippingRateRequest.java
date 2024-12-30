package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.Collections;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Address;

public class ShippingRateRequest {

	private final Address origin;
	
	private final Address dest;
	
	private final String currency;
	
	private final List<ProductPackage> itens;

	public ShippingRateRequest(Address origin, Address dest, String currency, List<ProductPackage> itens) {
		this.origin = new Address(origin);
		this.dest = new Address(dest);
		this.currency = currency;
		this.itens = Collections.unmodifiableList(itens);
	}

	public Address getOrigin() {
		return origin;
	}

	public Address getDest() {
		return dest;
	}

	public String getCurrency() {
		return currency;
	}

	public List<ProductPackage> getItens() {
		return itens;
	}
	
}
