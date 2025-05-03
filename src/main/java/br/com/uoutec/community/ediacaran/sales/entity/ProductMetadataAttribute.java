package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.i18n.ValidationException;

public class ProductMetadataAttribute {

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	protected int id;

	@Min(value = 1, groups = IdValidation.class)
	protected int productMetadata;
	
	@NotNull(groups = DataValidation.class)
	@Size(max=32,groups=DataValidation.class)
	@Pattern(regexp="[a-z0-9]+(_[a-z0-9]+)*",groups=DataValidation.class)
	protected String code;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.ADDRESS_FORMAT)
	//@Length(max = 128, groups = DataValidation.class)
	protected String name;

	@NotNull(groups = DataValidation.class)
	protected ProductAttributeType type;
	
	@NotNull(groups = DataValidation.class)
	protected ProductAttributeValueType valueType;
	
	//@Pattern(regexp = CommonValidation.ADDRESS_FORMAT, groups = DataValidation.class)
	@Size(max=128)
	protected String description;

	protected boolean allowEmpty;

	protected boolean show;

	protected boolean showForm;
	
	protected boolean filter;
	
	protected short rows;
	
	protected short minLength;
	
	protected short maxLength;

	protected Double min;
	
	protected Double max;
	
	@Size(max=128)
	protected String regex;

	@Size(max=32)
	protected String prefix;
	
	@Size(max=32)
	protected String suffix;
	
	protected short order;

	protected List<ProductMetadataAttributeOption> options;
	
	public String getProtectedID() {
		return id <= 0? null : SecretUtil.toProtectedID(id + "-" + productMetadata);		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		if(this.id != 0) {
			throw new IllegalStateException("id");
		}
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public short getMinLength() {
		return minLength;
	}

	public void setMinLength(short minLength) {
		this.minLength = minLength;
	}

	public short getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(short maxLength) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductMetadataAttributeOption> getOptions() {
		if(!optionsLoaded) {
			loadOptions();
		}
		return options;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	protected volatile boolean optionsLoaded = false;
	
	private synchronized void loadOptions() {
		
		if(optionsLoaded) {
			return;
		}
		
		this.options = ContextSystemSecurityCheck.doPrivileged(()->{
			
			if(this.id <= 0) {
				return new ArrayList<>();
			}
			
			ProductMetadataRegistry registry = EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
			List<ProductMetadataAttributeOption> tmp = registry.getProductMetadataAttributeOptions(this);
			optionsLoaded = true;
			return tmp;
		});
		
	}
	
	
	public void setOptions(List<ProductMetadataAttributeOption> options) {
		this.options = options;
		this.optionsLoaded = true;
	}

	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public short getRows() {
		return rows;
	}

	public void setRows(short rows) {
		this.rows = rows;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public short getOrder() {
		return order;
	}

	public void setOrder(short order) {
		this.order = order;
	}

	public int getProductMetadata() {
		return productMetadata;
	}

	public void setProductMetadata(int productMetadata) {
		this.productMetadata = productMetadata;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isMultiselect() {
		return 
				ProductAttributeValueType.TEXT == this.valueType || 
				ProductAttributeType.MULTISELECT == this.type || 
				ProductAttributeType.MULTISELECT_LIST == this.type;
	}

	public boolean isSelect() {
		return 
				ProductAttributeType.SELECT == this.type || 
				ProductAttributeType.MULTISELECT == this.type || 
				ProductAttributeType.MULTISELECT_LIST == this.type ||
				ProductAttributeType.SELECT_LIST == this.type;
	}
	
	public void validate(Object value) throws ValidationException {

		this.valueType.checkType(value);
		
		switch (valueType) {
		case TEXT:
			
			if(value == null || ((String)value).isEmpty()) {
				
				if(!allowEmpty) {
					throw new ValidationException(name + " is empty");
				}
				
				return;
			}
			
			if(minLength > 0 && ((String)value).length() < minLength) {
				throw new ValidationException(name + " is invalid");
			}
			
			if(maxLength > 0 && ((String)value).length() > maxLength) {
				throw new ValidationException(name + " is invalid");
			}
			
			if(regex != null && !((String)value).matches(regex)) {
				throw new ValidationException(name + " is invalid");
			}
			
			break;
		case INTEGER:
		case DECIMAL:
			
			if(value == null) {
				
				if(!allowEmpty) {
					throw new ValidationException(name + " is empty");
				}
				
				return;
			}
			
			if(min != null && ((Number)value).doubleValue() < min) {
				throw new ValidationException(name + " is invalid");
			}
			
			if(max != null && ((Number)value).doubleValue() > max) {
				throw new ValidationException(name + " is invalid");
			}
			
		case DATE:
		case DATE_TIME:
		case TIME:
			break;
		}

		if(!options.isEmpty() && isSelect()) {
			for(ProductMetadataAttributeOption o: options) {
				if(o.getValue().equals(value)) {
					return;
				}
			}
			throw new ValidationException(name + " is invalid");
		}
		
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
		ProductMetadataAttribute other = (ProductMetadataAttribute) obj;
		return Objects.equals(id, other.id);
	}

}
