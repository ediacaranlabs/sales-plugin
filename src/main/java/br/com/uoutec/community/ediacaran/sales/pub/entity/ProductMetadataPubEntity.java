package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.front.components.Image;
import br.com.uoutec.community.ediacaran.sales.SalesPluginConstants;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductMetadataPubEntity extends AbstractPubEntity<ProductMetadataUpdate>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	@NotNull(groups=DataValidation.class)
	@Size(max=128,groups=DataValidation.class)
	@Pattern(regexp=CommonValidation.NAME_FORMAT,groups=DataValidation.class)
	private String name;
	
	private Image thumbnail;

	@NotNull(groups=DataValidation.class)
	@Size(min=3, max = 256,groups=DataValidation.class)
	@Pattern(regexp=CommonValidation.NAME_FORMAT,groups=DataValidation.class)
	private String description;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductMetadataAttributePubEntity> attributes;
	
	@Constructor
	public ProductMetadataPubEntity(){
	}

	public ProductMetadataPubEntity(ProductMetadata e, Locale locale){
		this.description = e.getDescription();
		this.id = e.getId();
		this.name = e.getName();
		this.protectedID = e.getProtectedID();
		
		if(e.getAttributes() != null) {
			this.attributes = new ArrayList<>();
			for(ProductMetadataAttribute x: e.getAttributes().values()) {
				this.attributes.add(new ProductMetadataAttributePubEntity(x, locale));
			}
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductMetadataAttributePubEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ProductMetadataAttributePubEntity> attributes) {
		this.attributes = attributes;
	}

	@Override
	protected void preRebuild(ProductMetadataUpdate instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected ProductMetadataUpdate reloadEntity() throws Throwable {
		ProductMetadataRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		ProductMetadata e = entityRegistry.findProductMetadataById(this.id);
		return e == null? null : new ProductMetadataUpdate(e);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductMetadataUpdate createNewInstance() throws Throwable {
		return new ProductMetadataUpdate();
	}

	@Override
	protected void copyTo(ProductMetadataUpdate o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setName(this.name);
		o.setThumb(thumbnail == null? null : thumbnail.save(SalesPluginConstants.WIDTH_PRODUCT_IMAGE, SalesPluginConstants.HEIGHT_PRODUCT_IMAGE));
		o.setDescription(this.description);
		
		if(this.attributes != null) {
			List<ProductMetadataAttribute> list = new ArrayList<>();
			List<ProductMetadataAttributeUpdate> registerList = new ArrayList<>();
			List<ProductMetadataAttributeUpdate> unregisterList = new ArrayList<>();
			
			for(ProductMetadataAttributePubEntity x: this.attributes) {
				ProductMetadataAttribute xe = x.rebuild(x.getProtectedID() != null, true, true);
				if(x.getDeleted() != null && x.getDeleted().booleanValue()) {
					unregisterList.add(new ProductMetadataAttributeUpdate(xe));
				}
				else {
					registerList.add(new ProductMetadataAttributeUpdate(xe));
				}
				list.add(xe);
			}
			o.setAttributes(list.stream().collect(Collectors.toMap((e)->e.getCode(), (e)->e)));
			o.setRegisterAttributes(registerList);
			o.setUnregisterAttributes(unregisterList);
		}
	}

	@Override
	protected boolean isEqualId(ProductMetadataUpdate instance) throws Throwable {
		return instance.getId() <= 0? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductMetadataUpdate instance) throws Throwable {
		return instance.getId() > 0;
	}
	
}
