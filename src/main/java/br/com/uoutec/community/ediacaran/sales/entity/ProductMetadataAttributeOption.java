package br.com.uoutec.community.ediacaran.sales.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class ProductMetadataAttributeOption {

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	private int id;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128, groups = DataValidation.class)
	private String value;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 256, groups = DataValidation.class)
	private String description;

	public ProductMetadataAttributeOption() {
	}
	
	public ProductMetadataAttributeOption(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
