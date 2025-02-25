package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.List;
import java.util.Map;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;

public class ProductMetadataUpdate extends ProductMetadata {

	private ProductMetadata advice;
	
	private List<ProductMetadataAttributeUpdate> registerAttributes;
	
	private List<ProductMetadataAttributeUpdate> unregisterAttributes;

	public ProductMetadataUpdate(ProductMetadata advice) {
		this.advice = advice;
	}

	public String getProtectedID() {
		return advice.getProtectedID();
	}

	public int getId() {
		return advice.getId();
	}

	public void setId(int id) {
		advice.setId(id);
	}

	public String getName() {
		return advice.getName();
	}

	public void setName(String name) {
		advice.setName(name);
	}

	public Path getThumb() {
		return advice.getThumb();
	}

	public void setThumb(Path thumb) {
		advice.setThumb(thumb);
	}

	public String getDescription() {
		return advice.getDescription();
	}

	public void setDescription(String description) {
		advice.setDescription(description);
	}

	public Map<String, ProductMetadataAttribute> getAttributes() {
		return advice.getAttributes();
	}

	public void setAttributes(Map<String, ProductMetadataAttribute> attributes) {
		advice.setAttributes(attributes);
	}

	public int hashCode() {
		return advice.hashCode();
	}

	public boolean equals(Object obj) {
		return advice.equals(obj);
	}

	public String toString() {
		return advice.toString();
	}

	public List<ProductMetadataAttributeUpdate> getRegisterAttributes() {
		return registerAttributes;
	}

	public void setRegisterAttributes(List<ProductMetadataAttributeUpdate> registerAttributes) {
		this.registerAttributes = registerAttributes;
	}

	public List<ProductMetadataAttributeUpdate> getUnregisterAttributes() {
		return unregisterAttributes;
	}

	public void setUnregisterAttributes(List<ProductMetadataAttributeUpdate> unregisterAttributes) {
		this.unregisterAttributes = unregisterAttributes;
	}
	
}
