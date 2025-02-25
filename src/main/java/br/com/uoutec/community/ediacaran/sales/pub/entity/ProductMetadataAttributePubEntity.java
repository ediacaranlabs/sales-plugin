package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.application.validation.CommonValidation;
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

public class ProductMetadataAttributePubEntity extends AbstractPubEntity<ProductMetadataAttribute>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;

	@Transient
	private Integer productMetadata;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	@NotNull(groups = DataValidation.class)
	private String code;
	
	@NotNull(groups=DataValidation.class)
	@Size(min=3,groups=DataValidation.class)
	@Pattern(regexp=CommonValidation.NAME_FORMAT,groups=DataValidation.class)
	private String name;
	
	@NotNull(groups=DataValidation.class)
	@Size(min=3,groups=DataValidation.class)
	private String description;

	@NotNull(groups = DataValidation.class)
	private ProductAttributeType type;
	
	@NotNull(groups = DataValidation.class)
	private ProductAttributeValueType valueType;
	
	private Boolean allowEmpty;
	
	private Short rows;
	
	private Short minLength;
	
	private Short maxLength;

	private Double min;
	
	private Double max;
	
	private String regex;

	private Short order;

	private Boolean deleted;
	
	private List<ProductMetadataAttributeOptionPubEntity> options;
	
	@Constructor
	public ProductMetadataAttributePubEntity(){
	}

	public ProductMetadataAttributePubEntity(ProductMetadataAttribute e, Locale locale){
		this.protectedID = e.getId() <= 0? null : SecretUtil.toProtectedID(String.valueOf(e.getId()));
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

	@Override
	protected void preRebuild(ProductMetadataAttribute instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected ProductMetadataAttribute reloadEntity() throws Throwable {
		ProductMetadataRegistry entityRegistry = 
				EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		return entityRegistry.findProductMetadataAttributeById(this.id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductMetadataAttribute createNewInstance() throws Throwable {
		return new ProductMetadataAttribute();
	}

	@Override
	protected void copyTo(ProductMetadataAttribute o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setDescription(this.description);
		o.setName(this.name);
		o.setAllowEmpty(this.allowEmpty == null? true : this.allowEmpty.booleanValue());
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
		o.setRows(this.rows);
		o.setType(this.type);
		o.setValueType(this.valueType);
		
		if(this.options != null) {
			List<ProductMetadataAttributeOption> list = new ArrayList<>();
			for(ProductMetadataAttributeOptionPubEntity x: this.options) {
				list.add(x.rebuild(x.getProtectedID() != null, true, true));
			}
			o.setOptions(list);
		}
		
	}

	@Override
	protected boolean isEqualId(ProductMetadataAttribute instance) throws Throwable {
		return instance.getId() <= 0? 
					this.id == null :
					this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(ProductMetadataAttribute instance) throws Throwable {
		return instance.getId() > 0;
	}
	
}
