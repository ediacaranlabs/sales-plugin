package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataSearchResult;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ProductMetadatasSearchResultPubEntity extends AbstractPubEntity<ProductMetadataSearchResult> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ProductMetadataSearchResultPubEntity> itens;

	@Constructor
	public ProductMetadatasSearchResultPubEntity() {
	}
	
	public ProductMetadatasSearchResultPubEntity(ProductMetadataSearchResult e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.isHasNextPage();
		this.itens = new ArrayList<>();
		if(e.getItens() != null) {
			for(ProductMetadata p: e.getItens()) {
				itens.add(new ProductMetadataSearchResultPubEntity(p, locale));
			}
		}
	}

	@Override
	protected boolean isEqualId(ProductMetadataSearchResult instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ProductMetadataSearchResult instance) throws Throwable {
		return false;
	}

	@Override
	protected ProductMetadataSearchResult reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ProductMetadataSearchResult createNewInstance() throws Throwable {
		return new ProductMetadataSearchResult();
	}

	@Override
	protected void copyTo(ProductMetadataSearchResult o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setHasNextPage(this.hasNextPage == null? false: this.hasNextPage.booleanValue());
		o.setMaxPages(this.maxPages == null? -1 : this.maxPages.intValue());
		o.setPage(this.page == null? -1 : this.page.intValue());
		
		if(this.itens != null) {
			List<ProductMetadata> list = new ArrayList<>();
			for(ProductMetadataPubEntity p: this.itens) {
				list.add(p.rebuild(reload, override, validate));
			}
			o.setItens(list);
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

	public List<ProductMetadataSearchResultPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductMetadataSearchResultPubEntity> itens) {
		this.itens = itens;
	}
	
}
