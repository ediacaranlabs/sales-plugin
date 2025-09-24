package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategoryResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductCategoryResultSearchPubEntity extends AbstractPubEntity<ProductCategoryResultSearch> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductCategorySearchResultPubEntity> itens;

	@Constructor
	public ProductCategoryResultSearchPubEntity() {
	}
	
	public ProductCategoryResultSearchPubEntity(ProductCategoryResultSearch e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.getHasNextPage();
		this.itens = new ArrayList<>();
		if(e.getItens() != null) {
			for(ProductCategory p: e.getItens()) {
				itens.add(new ProductCategorySearchResultPubEntity(p, locale));
			}
		}
	}

	@Override
	protected boolean isEqualId(ProductCategoryResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductCategoryResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductCategoryResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductCategoryResultSearch createNewInstance() throws Throwable {
		return new ProductCategoryResultSearch();
	}

	@Override
	protected void copyTo(ProductCategoryResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setHasNextPage(this.hasNextPage == null? false: this.hasNextPage.booleanValue());
		o.setMaxPages(this.maxPages == null? -1 : this.maxPages.intValue());
		o.setPage(this.page == null? -1 : this.page.intValue());
		
		if(this.itens != null) {
			List<Product> list = new ArrayList<>();
			for(ProductPubEntity p: this.itens) {
				list.add(p.rebuild(reload, override, validate));
			}
		}
	}

	public Boolean getHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public Integer getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(Integer maxPages) {
		this.maxPages = maxPages;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<ProductCategorySearchResultPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductCategorySearchResultPubEntity> itens) {
		this.itens = itens;
	}
	
}
