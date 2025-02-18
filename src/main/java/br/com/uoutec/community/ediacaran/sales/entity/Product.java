package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.registry.ProductUtil;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Product implements Serializable{

	private static final long serialVersionUID = 2357774653199543812L;

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	protected int id;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128, groups = DataValidation.class)
	protected String name;
	
	protected Path thumb;

	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Size(max=256)
	protected String shortDescription;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.NAME_FORMAT, groups = DataValidation.class)
	@Size(max=2048)
	protected String description;
	
	@NotNull(groups = DataValidation.class)
	protected MeasurementUnit measurementUnit;
	
	@NotNull(groups = DataValidation.class)
	protected String productType;
	
	@NotNull(groups = DataValidation.class)
	protected BigDecimal cost;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.CURRENCY, groups = DataValidation.class)
	protected String currency;

	@NotNull(groups = DataValidation.class)
	protected Set<String> tags;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Path getThumb() {
		return thumb;
	}

	public void setThumb(Path thumb) {
		this.thumb = thumb;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public boolean isFree(){
		return this.cost == null || this.cost.equals(BigDecimal.ZERO);
	}
	
	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public String getCostString(Locale locale) {
		return 
			cost == null || currency == null? 
					"" :
					//currency + " " + cost.setScale(2, BigDecimal.ROUND_UNNECESSARY);
					DecimalFormat.getCurrencyInstance(locale).format(cost.setScale(2, BigDecimal.ROUND_UNNECESSARY));
	}
	
	public String getTagsString() {
		return tags == null? null: StringUtil.toString(tags, ",");
	}
	
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPublicThumb() {
		return thumb == null? null : ProductUtil.getPublicThumbPath(this) + ".png";
	}

	public String getPublicID() {
		return name == null? null : ProductUtil.getPublicID(this);
	}
	
}
