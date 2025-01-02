package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class ProductPackage {

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = "[0-9A-Z]+", groups = IdValidation.class)
	@Length(max = 38, min = 10, groups = IdValidation.class)
	private String id;
	
	@Min(value=0, groups = DataValidation.class )
	private float weight;
	
	@Min(value=0, groups = DataValidation.class )
	private float height;
	
	@Min(value=0, groups = DataValidation.class )
	private float width;
	
	@Min(value=0, groups = DataValidation.class )
	private float depth;

	@NotNull(groups = DataValidation.class )
	@Valid
	private List<ProductRequest> products;

	public ProductPackage() {
	}
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public List<ProductRequest> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequest> products) {
		this.products = products;
	}
	
}
