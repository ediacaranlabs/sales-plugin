package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResultFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductsSimplifiedSearchResultPubEntity 
	extends AbstractPubEntity<ProductSearchResult> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductSimplifiedSearchResultPubEntity> itens;

	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductSearchResultFilterPubEntity> filters;
	
	@Constructor
	public ProductsSimplifiedSearchResultPubEntity() {
	}
	
	public ProductsSimplifiedSearchResultPubEntity(ProductSearchResult e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.isHasNextPage();
		
		if(e.getItens() != null) {
			this.itens = new ArrayList<>();
			for(Product p: e.getItens()) {
				itens.add(new ProductSimplifiedSearchResultPubEntity(p, locale));
			}
		}
		
		if(e.getFilters() != null) {
			this.filters = new ArrayList<>();
			for(ProductSearchResultFilter x: e.getFilters()) {
				this.filters.add(new ProductSearchResultFilterPubEntity(x, locale));
			}
		}
	}

	@Override
	protected boolean isEqualId(ProductSearchResult instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductSearchResult instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductSearchResult reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductSearchResult createNewInstance() throws Throwable {
		return new ProductSearchResult();
	}

	@Override
	protected void copyTo(ProductSearchResult o, boolean reload, boolean override, boolean validate) throws Throwable {
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

	public List<ProductSimplifiedSearchResultPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductSimplifiedSearchResultPubEntity> itens) {
		this.itens = itens;
	}
	
}
