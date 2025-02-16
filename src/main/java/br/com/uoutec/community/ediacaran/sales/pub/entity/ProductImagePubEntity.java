package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.front.components.Image;
import br.com.uoutec.community.ediacaran.sales.SalesPluginConstants;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductImagePubEntity extends AbstractPubEntity<ProductImage>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private String id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	private Image image;
	
	@NotNull(groups=DataValidation.class)
	@Size(min=3,groups=DataValidation.class)
	private String description;
	private Boolean deleted;
	
	@Constructor
	public ProductImagePubEntity(){
	}

	public ProductImagePubEntity(ProductImage e, Locale locale){
		this.description = e.getDescription();
		this.protectedID = e.getId() == null? null : SecretUtil.toProtectedID(String.valueOf(e.getId()));
		
		if(e.getImage() != null) {
			this.image = new Image();
			this.image.setTmpFile(e.getImage());
		}
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	protected void preRebuild(ProductImage instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = SecretUtil.toID(this.protectedID);
		}
		catch(Throwable ex){
			this.id = null;
		}
	}
	
	@Override
	protected ProductImage reloadEntity() throws Throwable {
		ProductRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductRegistry.class);
		return entityRegistry.getImagesByID(id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductImage createNewInstance() throws Throwable {
		return new ProductImage();
	}

	@Override
	protected void copyTo(ProductImage o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setId(this.id);
		o.setImage(image == null? null : image.save(SalesPluginConstants.WIDTH_PRODUCT_IMAGE, SalesPluginConstants.HEIGHT_PRODUCT_IMAGE));
	}

	@Override
	protected boolean isEqualId(ProductImage instance) throws Throwable {
		return instance.getId() == null? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductImage instance) throws Throwable {
		return instance.getId() != null;
	}
	
}
