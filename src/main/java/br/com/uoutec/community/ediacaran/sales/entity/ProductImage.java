package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;

import br.com.uoutec.application.io.Path;

public class ProductImage {
	
	protected String id;
	
	protected Path image;
	
	protected String description;

	protected int product;
	
	public ProductImage() {
	}
	
	public ProductImage(String id, int product, Path image, String description) {
		this.id = id;
		this.image = image;
		this.description = description;
		this.product = product;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Path getImage() {
		return image;
	}

	public void setImage(Path image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductImage other = (ProductImage) obj;
		return Objects.equals(id, other.id);
	}
	
}
