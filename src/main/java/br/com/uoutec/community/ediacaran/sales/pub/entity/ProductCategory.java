package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.CategoryRegistryUtil;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class ProductCategory implements Serializable {

	private static final long serialVersionUID = -1447420279267594109L;

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	private int id;

	@NotNull(groups = DataValidation.class)
	@Length(max = 128, groups = DataValidation.class)
	private String name;

	protected volatile transient boolean imageLoaded;
	
	private transient Path thumb;

	private String resourceBundle;

	private String template;
	
	@NotNull(groups = DataValidation.class)
	@Size(groups = DataValidation.class, max=2048)
	private String description;
	
	private ProductCategory parent;
	
	private ProductCategory parent1;
	
	private ProductCategory parent2;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(String resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Path getThumb() {
		if(!imageLoaded) {
			loadImage();
		}
		return thumb;
	}

	public void setThumb(Path thumb) {
		this.thumb = thumb;
	}

	private synchronized void loadImage() {
		
		if(imageLoaded) {
			return;
		}
		
		ObjectsTemplateManager objectsManager = EntityContextPlugin.getEntity(ObjectsTemplateManager.class);
		ContextSystemSecurityCheck.doPrivileged(()->{
			CategoryRegistryUtil.loadImage(this, objectsManager);
			return null;
		});
		
	}
	
	public String getPublicThumb() {
		return CategoryRegistryUtil.getPublicThumbPath(this);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductCategory getParent() {
		return parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	public ProductCategory getParent1() {
		return parent1;
	}

	public void setParent1(ProductCategory parent1) {
		this.parent1 = parent1;
	}

	public ProductCategory getParent2() {
		return parent2;
	}

	public void setParent2(ProductCategory parent2) {
		this.parent2 = parent2;
	}
	
}
