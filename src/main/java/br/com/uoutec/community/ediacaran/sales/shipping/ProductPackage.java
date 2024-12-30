package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;

public class ProductPackage {

	private final float weight;
	
	private final float height;
	
	private final float width;
	
	private final float depth;
	
	private final List<ProductRequest> products;

	@SuppressWarnings("unchecked")
	public ProductPackage(float weight, float height, float width, float depth, List<ProductRequest> products) {
		this.weight = weight;
		this.height = height;
		this.width = width;
		this.depth = depth;
		
		if(products != null) {
			List<ProductRequest> tmp = new ArrayList<>();
			for(ProductRequest pr: products) {
				tmp.add(new ProductRequest(pr));
			}
			this.products = Collections.unmodifiableList(tmp);	
		}
		else {
			this.products = Collections.EMPTY_LIST;
		}
		
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

	public List<ProductRequest> getProducts() {
		return products;
	}

	
}
