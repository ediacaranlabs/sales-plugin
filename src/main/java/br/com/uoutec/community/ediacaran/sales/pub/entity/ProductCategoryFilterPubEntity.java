package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategoryFilter;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.IdValidation;

public class ProductCategoryFilterPubEntity extends AbstractPubEntity<ProductCategoryFilter>{

	private static final long serialVersionUID = -5240855789107084675L;

	@Transient
	private Integer id;

	@NotNull(groups = IdValidation.class)
	private String protectedID;

	private String title;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductCategoryFilterPubEntity> subcategories;
	
	@Constructor
	public ProductCategoryFilterPubEntity(){
	}

	public ProductCategoryFilterPubEntity(ProductCategoryFilter e, Locale locale){
		this.id = e.getProductCategory();
		this.protectedID = SecretUtil.toProtectedID(String.valueOf(e.getProductCategory()));
		this.title = e.getTitle();
		
		if(e.getSubcategories() != null) {
			this.subcategories = new ArrayList<>();
			for(ProductCategoryFilter x: e.getSubcategories()) {
				this.subcategories.add(new ProductCategoryFilterPubEntity(x, locale));
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ProductCategoryFilterPubEntity> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<ProductCategoryFilterPubEntity> subcategories) {
		this.subcategories = subcategories;
	}

	@Override
	protected void preRebuild(ProductCategoryFilter instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
		}
	}

	@Override
	protected ProductCategoryFilter reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected ProductCategoryFilter createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(ProductCategoryFilter o, boolean reload, boolean override,
			boolean validate) throws Throwable {
	}

	@Override
	protected boolean isEqualId(ProductCategoryFilter instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductCategoryFilter instance) throws Throwable {
		return false;
	}
	
}
