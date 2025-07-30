package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ShippingSearchResultPubEntity extends AbstractPubEntity<ShippingResultSearch> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ShippingPubEntity> itens;

	@Constructor
	public ShippingSearchResultPubEntity() {
	}
	
	public ShippingSearchResultPubEntity(ShippingResultSearch e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.getHasNextPage();
		this.itens = new ArrayList<>();
		if(e.getItens() != null) {
			for(Shipping p: e.getItens()) {
				ShippingPubEntity x = new ShippingResultPubEntity(p, locale).getType();
				itens.add(x);
			}
		}
	}

	@Override
	protected boolean isEqualId(ShippingResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ShippingResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ShippingResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ShippingResultSearch createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(ShippingResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
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

	public List<ShippingPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ShippingPubEntity> itens) {
		this.itens = itens;
	}

	
}
