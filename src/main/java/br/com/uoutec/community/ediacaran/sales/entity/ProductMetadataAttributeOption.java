package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class ProductMetadataAttributeOption {

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	private int id;
	
	@Min(value = 1, groups = IdValidation.class)
	private int productMetadataAttribute;
	
	@NotNull(groups = DataValidation.class)
	private Object value;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.ADDRESS_FORMAT, groups = DataValidation.class)
	@Length(max = 256, groups = DataValidation.class)
	private String description;

	public ProductMetadataAttributeOption() {
	}
	
	public ProductMetadataAttributeOption(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getProtectedID() {
		return id <= 0? null : SecretUtil.toProtectedID(id + "-" + productMetadataAttribute);		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProductMetadataAttribute() {
		return productMetadataAttribute;
	}

	public void setProductMetadataAttribute(int productMetadataAttribute) {
		this.productMetadataAttribute = productMetadataAttribute;
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
		ProductMetadataAttributeOption other = (ProductMetadataAttributeOption) obj;
		return id == other.id;
	}
	
}
