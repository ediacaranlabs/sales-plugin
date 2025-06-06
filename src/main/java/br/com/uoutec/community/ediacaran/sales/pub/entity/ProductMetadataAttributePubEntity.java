package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;
import org.hibernate.validator.constraints.Length;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductMetadataAttributePubEntity extends AbstractPubEntity<ProductMetadataAttributeUpdate>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;

	@Transient
	private Integer productMetadata;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	@NotNull(groups = DataValidation.class)
	@Size(max=32,groups=DataValidation.class)
	@Pattern(regexp="[a-z0-9]+(_[a-z0-9]+)*",groups=DataValidation.class)
	private String code;
	
	@NotNull(groups=DataValidation.class)
	//@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128, groups = DataValidation.class)
	private String name;
	
	@Size(min=3,groups=DataValidation.class)
	private String description;

	@NotNull(groups = DataValidation.class)
	private ProductAttributeType type;
	
	@NotNull(groups = DataValidation.class)
	private ProductAttributeValueType valueType;
	
	private Boolean allowEmpty;

	private Boolean show;
	
	private Boolean showForm;

	private Boolean filter;
	
	private Short rows;
	
	private Short minLength;
	
	private Short maxLength;

	private Double min;
	
	private Double max;
	
	@Size(max=128)
	private String regex;

	private Short order;

	private Boolean deleted;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductMetadataAttributeOptionPubEntity> options;
	
	@Transient
	@NotNull(groups=DataValidation.class)
	private Locale locale;
	
	@Constructor
	public ProductMetadataAttributePubEntity(){
	}

	public ProductMetadataAttributePubEntity(ProductMetadataAttribute e, Locale locale){
		this.protectedID = e.getId() <= 0? null : e.getProtectedID();
		this.allowEmpty = e.isAllowEmpty();
		this.code = e.getCode();
		this.description = e.getDescription();
		this.id = e.getId();
		this.max = e.getMax();
		this.maxLength = e.getMaxLength();
		this.min = e.getMin();
		this.minLength = e.getMinLength();
		this.name = e.getName();
		this.order = e.getOrder();
		this.productMetadata = e.getProductMetadata();
		this.regex = e.getRegex();
		this.rows = e.getRows();
		this.type = e.getType();
		this.valueType = e.getValueType();
		this.show = e.isShow();
		this.showForm = e.isShowForm();
		this.filter = e.isFilter();
		
		if(e.getOptions() != null) {
			this.options = new ArrayList<>();
			for(ProductMetadataAttributeOption x: e.getOptions()) {
				this.options.add(new ProductMetadataAttributeOptionPubEntity(x, locale));
			}
		}
	}
	
	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(int productMetadata) {
		this.productMetadata = productMetadata;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ProductAttributeType getType() {
		return type;
	}

	public void setType(ProductAttributeType type) {
		this.type = type;
	}

	public ProductAttributeValueType getValueType() {
		return valueType;
	}

	public void setValueType(ProductAttributeValueType valueType) {
		this.valueType = valueType;
	}

	public Boolean getAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(Boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public Short getRows() {
		return rows;
	}

	public void setRows(Short rows) {
		this.rows = rows;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public Boolean getShowForm() {
		return showForm;
	}

	public void setShowForm(Boolean showForm) {
		this.showForm = showForm;
	}

	public Boolean getFilter() {
		return filter;
	}

	public void setFilter(Boolean filter) {
		this.filter = filter;
	}

	public Short getMinLength() {
		return minLength;
	}

	public void setMinLength(Short minLength) {
		this.minLength = minLength;
	}

	public Short getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Short maxLength) {
		this.maxLength = maxLength;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public Short getOrder() {
		return order;
	}

	public void setOrder(Short order) {
		this.order = order;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setProductMetadata(Integer productMetadata) {
		this.productMetadata = productMetadata;
	}

	public List<ProductMetadataAttributeOptionPubEntity> getOptions() {
		return options;
	}

	public void setOptions(List<ProductMetadataAttributeOptionPubEntity> options) {
		this.options = options;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	protected void preRebuild(ProductMetadataAttributeUpdate instance, boolean reload, boolean override, boolean validate) {
		
		try {
			String str = SecretUtil.toID(this.protectedID);
			String[] parts = str.split("\\-");
			if(parts.length != 2) {
				throw new IllegalStateException();
			}
			this.id = Integer.parseInt(parts[0]);
			this.productMetadata = Integer.parseInt(parts[1]);
		}
		catch(Throwable ex){
			this.id = 0;
			this.productMetadata = 0;
		}
	}
	
	@Override
	protected ProductMetadataAttributeUpdate reloadEntity() throws Throwable {
		ProductMetadataRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		ProductMetadataAttribute e = entityRegistry.findProductMetadataAttributeById(this.id);
		return e == null? null : new ProductMetadataAttributeUpdate(e);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductMetadataAttributeUpdate createNewInstance() throws Throwable {
		return new ProductMetadataAttributeUpdate();
	}

	@Override
	protected void copyTo(ProductMetadataAttributeUpdate o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setName(this.name);
		o.setAllowEmpty(this.allowEmpty == null? false : this.allowEmpty.booleanValue());
		o.setCode(this.code);
		o.setDescription(this.description);
		o.setMax(this.max);
		o.setMaxLength(this.maxLength == null? 0 : this.maxLength.shortValue());
		o.setMin(this.min);
		o.setMinLength(this.minLength == null? 0 : this.minLength.shortValue());
		o.setName(this.name);
		o.setOrder(this.order == null? 0 : this.order);
		o.setProductMetadata(this.productMetadata == null? 0 : this.productMetadata.intValue());
		o.setRegex(this.regex);
		o.setRows(this.rows == null? 0 : this.rows.shortValue());
		o.setType(this.type);
		o.setValueType(this.valueType);
		o.setShow(this.show == null? false : this.show.booleanValue());
		o.setShowForm(this.showForm == null? false : this.showForm.booleanValue());
		o.setFilter(this.filter == null? false : this.filter.booleanValue());
		
		int index = 0;
		if(this.options != null) {
			List<ProductMetadataAttributeOption> list = new ArrayList<>();
			List<ProductMetadataAttributeOptionUpdate> register = new ArrayList<>();
			List<ProductMetadataAttributeOptionUpdate> unregister = new ArrayList<>();
			
			for(ProductMetadataAttributeOptionPubEntity x: this.options) {
				
				x.setValueType(this.valueType);
				x.setLocale(this.locale);
				
				ProductMetadataAttributeOptionUpdate e = x.rebuild(x.getProtectedID() != null, true, true);
				e.setIndex(index++);
				
				list.add(e);
				
				if(x.getProtectedID() != null) {
					if(x.getDeleted() != null && x.getDeleted().booleanValue()) {
						unregister.add(e);
						continue;
					}
				}

				register.add(e);
				
			}
			
			o.setOptions(list);
			o.setRegisterOptions(register);
			o.setUnregisterOptions(unregister);
		}
		
	}

	@Override
	protected boolean isEqualId(ProductMetadataAttributeUpdate instance) throws Throwable {
		return instance.getId() <= 0? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductMetadataAttributeUpdate instance) throws Throwable {
		return instance.getId() > 0;
	}
	
}
