package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.Collections;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;

public class ShippingRateRequest {

	private final Address origin;
	
	private final Address dest;
	
	private final List<ProductRequest> itens;

	public ShippingRateRequest(Shipping shipping) {
		this(shipping.getOrigin(), shipping.getDest(), shipping.getProducts());
	}
	
	public ShippingRateRequest(Address origin, Address dest, List<ProductRequest> itens) {
		this.origin = new Address(origin);
		this.dest = new Address(dest);
		this.itens = Collections.unmodifiableList(itens);
	}

	public Address getOrigin() {
		return origin;
	}

	public Address getDest() {
		return dest;
	}

	public List<ProductRequest> getItens() {
		return itens;
	}
	
}
