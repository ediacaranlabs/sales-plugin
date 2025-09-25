package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.front.components.Image;
import br.com.uoutec.community.ediacaran.sales.SalesPluginConstants;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.registry.ProductCategoryRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductCategoryPubEntity extends AbstractPubEntity<ProductCategory>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;

	@NotNull(groups = DataValidation.class)
	@Length(max = 128, groups = DataValidation.class)
	private String name;

	private Image thumbnail;

	private String resourceBundle;

	private String template;
	
	@NotNull(groups = DataValidation.class)
	@Size(groups = DataValidation.class, max=2048)
	private String description;
	
	private String parent;
	
	private String parent1;
	
	private String parent2;
	
	@Constructor
	public ProductCategoryPubEntity(){
	}

	public ProductCategoryPubEntity(ProductCategory e, Locale locale){
		this.description = e.getDescription();
		this.protectedID = e.getProtectedID();
		this.name = e.getName();
		this.parent = e.getParent() == null? null : e.getParent().getProtectedID();
		this.parent1 = e.getParent1() == null? null : e.getParent1().getProtectedID();
		this.parent2 = e.getParent2() == null? null : e.getParent2().getProtectedID();
	}

	@Override
	protected void preRebuild(ProductCategory instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParent1() {
		return parent1;
	}

	public void setParent1(String parent1) {
		this.parent1 = parent1;
	}

	public String getParent2() {
		return parent2;
	}

	public void setParent2(String parent2) {
		this.parent2 = parent2;
	}

	@Override
	protected ProductCategory reloadEntity() throws Throwable {
		ProductCategoryRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductCategoryRegistry.class);
		return entityRegistry.findById(this.id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductCategory createNewInstance() throws Throwable {
		return new ProductCategory();
	}

	@Override
	protected void copyTo(ProductCategory o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setId(this.id == null? 0 : this.id);
		o.setName(this.name);
		o.setThumb(thumbnail == null? null : thumbnail.save(SalesPluginConstants.WIDTH_PRODUCT_IMAGE, SalesPluginConstants.HEIGHT_PRODUCT_IMAGE));
		
		if(this.parent != null) {
			ProductCategory p = new ProductCategory();
			p.setProtectedID(this.parent);
			o.setParent(p);
		}
		
		if(this.parent1 != null) {
			ProductCategory p = new ProductCategory();
			p.setProtectedID(this.parent1);
			o.setParent1(p);
		}

		if(this.parent2 != null) {
			ProductCategory p = new ProductCategory();
			p.setProtectedID(this.parent2);
			o.setParent2(p);
		}

	}

	@Override
	protected boolean isEqualId(ProductCategory instance) throws Throwable {
		return instance.getId() <= 0? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductCategory instance) throws Throwable {
		return instance.getId() > 0;
	}
	
}
