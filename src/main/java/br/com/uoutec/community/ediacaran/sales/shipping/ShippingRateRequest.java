package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.Collections;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;

public class ShippingRateRequest {

	private final Address origin;
	
	private final Address dest;
	
	private final float weight;
	
	private final float height;
	
	private final float width;
	
	private final float depth;

	private final List<ProductRequest> itens;

	public ShippingRateRequest(Shipping shipping) {
		this(shipping.getOrigin(), shipping.getDest(), shipping.getWeight(), shipping.getHeight(), shipping.getWidth(), shipping.getDepth(), shipping.getProducts());
	}
	
	public ShippingRateRequest(Address origin, Address dest, float weight, float height, float width, float depth, List<ProductRequest> itens) {
		this.weight = weight;
		this.depth = depth;
		this.height = height;
		this.width = width;
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

	public float getWeight() {
		return weight;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public float getDepth() {
		return depth;
	}
	
}
