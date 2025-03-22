package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchAttributeFilter;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductSearchAttributeFilterPubEntity 
	extends AbstractPubEntity<ProductSearchAttributeFilter> {

	private static final long serialVersionUID = 626786765026588093L;

	@Transient
	private Integer id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;

	private List<String> values;
	
	@Transient
	private Locale locale;
	
	@Override
	protected void preRebuild(ProductSearchAttributeFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(ProductSearchAttributeFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductSearchAttributeFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductSearchAttributeFilter reloadEntity() throws Throwable {
		ProductMetadataRegistry productMetadataRegistry = EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		ProductMetadataAttribute productMetadataAttribute = productMetadataRegistry.findProductMetadataAttributeById(this.id);
		
		ProductSearchAttributeFilter productSearchAttributeFilter = new ProductSearchAttributeFilter();
		productSearchAttributeFilter.setProductMetadataAttribute(productMetadataAttribute);
		
		return productSearchAttributeFilter;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected ProductSearchAttributeFilter createNewInstance() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void copyTo(ProductSearchAttributeFilter o, boolean reload, boolean override,
			boolean validate) throws Throwable {

		if(values != null) {
			Set<Object> vals = new HashSet<>();
			
			for(String v: this.values) {
				vals.add(o.getProductMetadataAttribute().getValueType().parse(v, locale));
			}
			
			o.setValue(vals);
		}
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}
