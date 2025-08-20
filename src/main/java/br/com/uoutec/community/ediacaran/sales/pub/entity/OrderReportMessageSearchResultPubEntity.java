package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessageResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrderReportMessageSearchResultPubEntity extends AbstractPubEntity<OrderReportMessageResultSearch> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<OrderReportMessageResultSearchItemPubEntity> itens;

	@Constructor
	public OrderReportMessageSearchResultPubEntity() {
	}
	
	public OrderReportMessageSearchResultPubEntity(OrderReportMessageResultSearch e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.getHasNextPage();
		this.itens = new ArrayList<>();
		if(e.getItens() != null) {
			DateTimeFormatter dtaFormt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withLocale(locale);
			for(OrderReportMessage p: e.getItens()) {
				OrderReportMessageResultSearchItemPubEntity x = new OrderReportMessageResultSearchItemPubEntity(p, locale, dtaFormt);
				itens.add(x);
			}
		}
	}

	@Override
	protected boolean isEqualId(OrderReportMessageResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(OrderReportMessageResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected OrderReportMessageResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected OrderReportMessageResultSearch createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(OrderReportMessageResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
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

	public List<OrderReportMessageResultSearchItemPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<OrderReportMessageResultSearchItemPubEntity> itens) {
		this.itens = itens;
	}
	
}
