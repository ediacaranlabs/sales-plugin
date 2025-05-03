package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductUtil;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.ProductRegistryUtil;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.i18n.ValidationException;

public class Product implements Serializable {

	private static final long serialVersionUID = 2357774653199543812L;

	@NotNull(groups = IdValidation.class)
	@Min(value = 1, groups = IdValidation.class)
	protected int id;
	
	@NotNull(groups = DataValidation.class)
	//@Pattern(regexp = CommonValidation.NAME_FORMAT)
	@Length(max = 128, groups = DataValidation.class)
	protected String name;
	
	protected transient Path thumb;
	
	protected volatile transient boolean imageLoaded;

	@NotNull(groups = DataValidation.class)
	//@Pattern(regexp = CommonValidation.WORD_NUM, groups = DataValidation.class)
	@Size(max=256)
	protected String shortDescription;
	
	@NotNull(groups = DataValidation.class)
	protected ProductVisibility visibility;
	
	@NotNull(groups = DataValidation.class)
	//@Pattern(regexp = CommonValidation.WORD_NUM, groups = DataValidation.class)
	@Size(max=2048)
	protected String description;
	
	@NotNull(groups = DataValidation.class)
	protected MeasurementUnit measurementUnit;
	
	@NotNull(groups = DataValidation.class)
	protected String productType;

	@Min(value = 1, groups = DataValidation.class)
	protected int metadata;
	
	@NotNull(groups = DataValidation.class)
	protected BigDecimal cost;
	
	@NotNull(groups = DataValidation.class)
	@Pattern(regexp = CommonValidation.CURRENCY, groups = DataValidation.class)
	protected String currency;

	protected Set<String> tags;
	
	protected Map<String, ProductAttributeValue> attributes;
	
	public Product() {
		this.attributes = new HashMap<>();
		this.visibility = ProductVisibility.SHOW;
	}
	
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
		if(!imageLoaded) {
			loadImage();
		}
		return thumb;
	}

	private synchronized void loadImage() {
		
		if(imageLoaded) {
			return;
		}
		
		ObjectsTemplateManager objectsManager = EntityContextPlugin.getEntity(ObjectsTemplateManager.class);
		ProductRegistryUtil.loadProductImage(this, objectsManager);
	}
	
	public void setThumb(Path thumb) {
		this.thumb = thumb;
		this.imageLoaded = true;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getMetadata() {
		return metadata;
	}

	public void setMetadata(int metadata) {
		this.metadata = metadata;
		this.productMetadata = null;
	}

	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public ProductVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(ProductVisibility visibility) {
		this.visibility = visibility;
	}

	public Map<String, ProductAttributeValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, ProductAttributeValue> attributes) {
		this.attributes = attributes;
	}

	public List<ProductAttribute> getProductAttributes(Boolean visible) throws ProductRegistryException{
		Set<String> keys = new HashSet<>(this.attributes.keySet());
		List<ProductAttribute> r = new ArrayList<>();
		
		for(String key: keys) {
			
			ProductMetadataAttribute pa = getProductMetadataAttribute(key);
			
			if(pa == null) {
				continue;
			}
			
			if(visible != null && (pa.isShow() && !visible)) {
				continue;
			}
			
			ProductAttributeValue val = attributes.get(key);
			
			Object[] values = val.getValues();
			
			if(values.length == 1) {
				r.add(new ProductAttribute(pa.getName(), String.valueOf(values[0])));
			}
			if(values.length > 1) {
				r.add(new ProductAttribute(pa.getName(), Arrays.toString(values)));
			}
			
		}
		
		Collections.sort(r, (a,b)->a.getName().compareTo(b.getName()));
		
		return r;
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
					currency + " " + cost.setScale(2, BigDecimal.ROUND_UNNECESSARY);
					//DecimalFormat.getCurrencyInstance(locale).format(cost.setScale(2, BigDecimal.ROUND_UNNECESSARY));
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
		return getThumb() == null? null : ProductUtil.getPublicThumbPath(this) + ".png";
	}

	public String getPublicID() {
		return name == null? null : ProductUtil.getPublicID(this);
	}
	
	public Object getAttribute(String code) {
		return this.attributes.get(code);
	}

	public void setAttribute(String code, String ... value) throws ProductRegistryException, ValidationException {
		
		ProductMetadata productMetadata = getProductMetadata();
		ProductMetadataAttribute attr = productMetadata.getAttributes().get(code);
		
		if(attr == null) {
			
			productMetadata = getDefaultProductMetadata();
			
			if(productMetadata != null) {
				attr = productMetadata.getAttributes().get(code);
			}
			
		}
		
		if(attr == null) {
			throw new IllegalStateException(code);
		}
		
		ProductMetadataAttribute fAttr = attr;
		Object[] values = Arrays.stream(value).map((e)->fAttr.getValueType().parse(e, null)).toArray(Object[]::new);
		setAttribute(code, attr, values);
	}
	
	public void setAttribute(String code, Locale locale, String ... value) throws ProductRegistryException, ValidationException {
		
		ProductMetadata productMetadata = getProductMetadata();
		ProductMetadataAttribute attr = productMetadata.getAttributes().get(code);
		
		if(attr == null) {
			
			productMetadata = getDefaultProductMetadata();
			
			if(productMetadata != null) {
				attr = productMetadata.getAttributes().get(code);
			}
			
		}
		
		if(attr == null) {
			throw new IllegalStateException(code);
		}
		
		ProductMetadataAttribute fAttr = attr;
		Object[] values = Arrays.stream(value).map((e)->fAttr.getValueType().parse(e, locale)).toArray(Object[]::new);
		setAttribute(code, attr, values);
	}

	public void setAttribute(String code, Object ... value) throws ProductRegistryException, ValidationException {
		
		ProductMetadata productMetadata = getProductMetadata();
		ProductMetadataAttribute attr = productMetadata.getAttributes().get(code);
		
		if(attr == null) {
			
			productMetadata = getDefaultProductMetadata();
			
			if(productMetadata != null) {
				attr = productMetadata.getAttributes().get(code);
			}
			
		}
		
		if(attr == null) {
			throw new IllegalStateException(code);
		}
		
		setAttribute(code, attr, value);
	}
	
	private void setAttribute(String code, ProductMetadataAttribute attr, Object[] value) throws ProductRegistryException, ValidationException {
		
		if(attr == null) {
			throw new IllegalStateException(code);
		}
		

		ProductAttributeValue v = this.attributes.get(code);
		
		if(v == null) {
			v = new ProductAttributeValue(attr);
			this.attributes.put(code, v);
		}
		
		for(Object o: value) {
			attr.validate(o);
		}
		v.setValue(value);
	}
	
	private ProductMetadata getDefaultProductMetadata() throws ProductRegistryException {
		
		if(this.productMetadata == null) {
			loadMetadata();			
		}
		
		return this.defaultProductMetadata;
	}
	
	private ProductMetadata getProductMetadata() throws ProductRegistryException {
		
		if(this.productMetadata == null) {
			loadMetadata();			
		}
		
		return this.productMetadata;
	}
	
	/* metadata */
	
	private ProductMetadataAttribute getProductMetadataAttribute(String code) throws ProductRegistryException {
		
		ProductMetadata productMetadata = getProductMetadata();
		ProductMetadataAttribute attr = productMetadata.getAttributes().get(code);
		
		if(attr == null) {
			
			productMetadata = getDefaultProductMetadata();
			
			if(productMetadata != null) {
				attr = productMetadata.getAttributes().get(code);
			}
			
		}
		
		return attr;
	}
	
	private transient volatile ProductMetadata productMetadata;
	
	private transient volatile ProductMetadata defaultProductMetadata;
	
	private synchronized void loadMetadata() throws ProductRegistryException {
		
		ContextSystemSecurityCheck.doPrivileged(()->{
			
			if(this.productMetadata == null) {
				ProductMetadataRegistry productMetadataRegistry = EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
				this.productMetadata = productMetadataRegistry.findProductMetadataById(this.metadata);
				this.defaultProductMetadata = productMetadataRegistry.getDefaultProductMetadata();
			}
			
			return null;
		});
		
	}
}
