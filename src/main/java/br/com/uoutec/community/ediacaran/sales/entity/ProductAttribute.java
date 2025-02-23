package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.i18n.ValidationException;

public class ProductAttribute {

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	private int id;
	
	@NotNull(groups = DataValidation.class)
	private String code;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128, groups = DataValidation.class)
	protected String name;

	@NotNull(groups = DataValidation.class)
	protected ProductAttributeType type;
	
	@NotNull(groups = DataValidation.class)
	protected ProductAttributeValueType valueType;
	
	@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Size(max=128)
	protected String description;

	protected boolean allowEmpty;
	
	protected short rows;
	
	protected short minLength;
	
	protected short maxLength;

	protected Double min;
	
	protected Double max;
	
	protected String regex;

	protected short order;

	protected List<ProductAttributeOption> options;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public List<ProductAttributeOption> getOptions() {
		return options;
	}

	public void setOptions(List<ProductAttributeOption> options) {
		this.options = options;
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

	public void validate(String value) throws ValidationException {

		if(value == null || value.isEmpty()) {
			
			if(!allowEmpty) {
				throw new ValidationException(name + " is empty");
			}
			
			return;
		}
		
		if(!options.isEmpty()) {
			for(ProductAttributeOption op: options) {
				if(value.equals(op.getValue())) {
					return;
				}
			}
			
			throw new ValidationException(name + " is invalid");
			
		}
		
		if(minLength > 0 && value.length() < minLength) {
			throw new ValidationException(name + " is invalid");
		}
		
		if(maxLength > 0 && value.length() > maxLength) {
			throw new ValidationException(name + " is invalid");
		}

		if(min != null && ((Number)valueType.toObject(value, null)).doubleValue() < min) {
			throw new ValidationException(name + " is invalid");
		}
		
		if(max != null && ((Number)valueType.toObject(value, null)).doubleValue() > max) {
			throw new ValidationException(name + " is invalid");
		}
		
		if(regex != null && !value.matches(regex)) {
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
		ProductAttribute other = (ProductAttribute) obj;
		return Objects.equals(id, other.id);
	}

}
