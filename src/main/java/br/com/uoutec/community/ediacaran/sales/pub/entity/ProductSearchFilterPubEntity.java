package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchAttributeFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchFilter;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductSearchFilterPubEntity extends AbstractPubEntity<ProductSearchFilter> {

	private static final long serialVersionUID = 6527154894335305698L;

	@Transient
	private Integer id;
	
	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductSearchAttributeFilterPubEntity> filters;
	
	@Transient
	private Locale locale;
	
	@Override
	protected void preRebuild(ProductSearchFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(ProductSearchFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductSearchFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductSearchFilter reloadEntity() throws Throwable {
		ProductMetadataRegistry productMetadataRegistry = EntityContextPlugin.getEntity(ProductMetadataRegistry.class);
		ProductMetadata productMetadata = productMetadataRegistry.findProductMetadataById(this.id);
		
		ProductSearchFilter productSearchFilter = new ProductSearchFilter();
		productSearchFilter.setProductMetadata(productMetadata);
		return productSearchFilter;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected ProductSearchFilter createNewInstance() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void copyTo(ProductSearchFilter o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		if(this.filters != null) {
			Set<ProductSearchAttributeFilter> attributeFilters = new HashSet<>();
			for(ProductSearchAttributeFilterPubEntity x: this.filters) {
				x.setLocale(locale);
				attributeFilters.add(x.rebuild(true,true,true));
			}
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

	public List<ProductSearchAttributeFilterPubEntity> getFilters() {
		return filters;
	}

	public void setFilters(List<ProductSearchAttributeFilterPubEntity> filters) {
		this.filters = filters;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}
