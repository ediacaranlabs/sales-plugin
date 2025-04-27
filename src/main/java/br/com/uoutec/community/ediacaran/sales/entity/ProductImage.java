package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.Objects;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.registry.ProductUtil;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.ProductRegistryUtil;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ProductImage implements Serializable{
	
	private static final long serialVersionUID = 7350569771599012916L;

	protected String id;
	
	protected transient Path image;
	
	protected volatile transient boolean imageLoaded;
	
	protected String description;

	protected int product;
	
	public ProductImage() {
	}
	
	public ProductImage(String id, int product, Path image, String description) {
		this.id = id;
		this.image = image;
		this.description = description;
		this.product = product;
		this.imageLoaded = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Path getImage() {
		if(!imageLoaded) {
			loadImage();
		}
		return image;
	}

	private synchronized void loadImage() {
		
		if(imageLoaded) {
			return;
		}
		
		ObjectsTemplateManager objectsManager = EntityContextPlugin.getEntity(ObjectsTemplateManager.class);
		ProductRegistryUtil.loadProductImage(this, objectsManager);
	}
	
	public void setImage(Path image) {
		this.image = image;
		this.imageLoaded = true;
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

	public String getPublicThumb() {
		return getImage() == null? null : ProductUtil.getPublicThumbPath(this) + ".png";
	}
	
	public String getProtectedID() {
		return id == null || id == null? null : SecretUtil.toProtectedID(id);		
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
