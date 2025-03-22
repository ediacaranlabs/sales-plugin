package br.com.uoutec.community.ediacaran.sales.persistence;

import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeSearchResultOptionFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;

public class ProductMetadataAttributeSearchResultOptionEntityFilter {

	private ProductMetadataAttributeOption option;

	private String description;
	
	private Object value;
	
	private boolean selected;
	
	private ProductAttributeValueType type;
	
	public ProductMetadataAttributeSearchResultOptionEntityFilter(ProductMetadataAttributeOption option) {
		this.option = option;
		this.description = option.getDescription();
		this.type = option.getValueType();
		this.value = option.getValue();
	}

	public ProductMetadataAttributeSearchResultOptionEntityFilter(Object value, String description, ProductAttributeValueType type, boolean selected) {
		this.description = description;
		this.type = type;
		this.value = value;
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ProductMetadataAttributeOption getOption() {
		return option;
	}

	public void setOption(ProductMetadataAttributeOption option) {
		this.option = option;
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

	public ProductAttributeValueType getType() {
		return type;
	}

	public void setType(ProductAttributeValueType type) {
		this.type = type;
	}

	public ProductAttributeSearchResultOptionFilter toEntity() {
		ProductAttributeSearchResultOptionFilter e = new ProductAttributeSearchResultOptionFilter();
		
		e.setDescription(this.description);
		e.setType(this.type);
		e.setValue(this.value);
		e.setSelected(this.selected);
		
		return e;
	}
	
}
