package br.com.uoutec.community.ediacaran.sales.pub.entity;

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
	private int productMetadata;
	
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
	protected ProductAttributeType type;
	
	@NotNull(groups = DataValidation.class)
	protected ProductAttributeValueType valueType;
	
	protected Boolean allowEmpty;
	
	protected Short rows;
	
	protected Short minLength;
	
	protected Short maxLength;

	protected Double min;
	
	protected Double max;
	
	protected String regex;

	protected Short order;

	protected List<ProductMetadataAttributeOption> options;
	
	@Constructor
	public ProductMetadataAttributePubEntity(){
	}

	public ProductMetadataAttributePubEntity(ProductMetadataAttribute e, Locale locale){
		this.protectedID = e.getId() <= 0? null : SecretUtil.toProtectedID(e.getId() + "-" + e.getProductMetadata());
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

	public List<ProductMetadataAttributeOption> getOptions() {
		return options;
	}

	public void setOptions(List<ProductMetadataAttributeOption> options) {
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
