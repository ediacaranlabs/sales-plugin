package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class RefundSearchResultPubEntity extends AbstractPubEntity<RefundResultSearch> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<RefundResultSearchItemPubEntity> itens;

	@Constructor
	public RefundSearchResultPubEntity() {
	}
	
	public RefundSearchResultPubEntity(RefundResultSearch e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.getHasNextPage();
		this.itens = new ArrayList<>();
		if(e.getItens() != null) {
			DateTimeFormatter dtaFormt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withLocale(locale);
			for(Refund p: e.getItens()) {
				RefundResultSearchItemPubEntity x = new RefundResultSearchItemPubEntity(p, locale, dtaFormt);
				itens.add(x);
			}
		}
	}

	@Override
	protected boolean isEqualId(RefundResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(RefundResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected RefundResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected RefundResultSearch createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(RefundResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
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

	public List<RefundResultSearchItemPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<RefundResultSearchItemPubEntity> itens) {
		this.itens = itens;
	}

	
}
