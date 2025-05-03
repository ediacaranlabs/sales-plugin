package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.registry.ProductUtil;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class ProductMetadata {

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	protected int id;

	@NotNull(groups = DataValidation.class)
	//@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128, groups = DataValidation.class)
	protected String name;
	
	protected Path thumb;

	@NotNull(groups = DataValidation.class)
	//@Pattern(regexp = CommonValidation.ADDRESS_FORMAT, groups = DataValidation.class)
	@Size(max=256)
	protected String description;
	
	@Valid
	protected Map<String, ProductMetadataAttribute> attributes;
	
	public String getProtectedID() {
		return id <= 0? null : SecretUtil.toProtectedID(String.valueOf(id));		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Path getThumb() {
		return thumb;
	}

	public void setThumb(Path thumb) {
		this.thumb = thumb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, ProductMetadataAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, ProductMetadataAttribute> attributes) {
		this.attributes = attributes;
	}
	
	public List<ProductMetadataAttribute> getAttributeList(){
		return this.attributes == null? null : new ArrayList<>(this.attributes.values());
	}
	
	public String getPublicThumb() {
		return thumb == null? null : ProductUtil.getPublicThumbPath(this) + ".png";
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
		ProductMetadata other = (ProductMetadata) obj;
		return id == other.id;
	}
	
}
