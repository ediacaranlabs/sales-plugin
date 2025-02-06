package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrdersResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class OrdersResultSearchPubEntity extends AbstractPubEntity<OrdersResultSearch>{

	private static final long serialVersionUID = -4626442588141322167L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	private List<OrderResultSearchPubEntity> data;

	public OrdersResultSearchPubEntity() {
	}

	public OrdersResultSearchPubEntity(OrdersResultSearch e, Locale locale) {
		this.hasNextPage = e.isHasNextPage();
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.data = new ArrayList<>();
		
		DateTimeFormatter dtaFormt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withLocale(locale);
		for(OrderResultSearch x: e.getData()) {
			this.data.add(new OrderResultSearchPubEntity(x, locale, dtaFormt));
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

	public List<OrderResultSearchPubEntity> getData() {
		return data;
	}

	public void setData(List<OrderResultSearchPubEntity> data) {
		this.data = data;
	}

	@Override
	protected boolean isEqualId(OrdersResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(OrdersResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected OrdersResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected OrdersResultSearch createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(OrdersResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
	}
	
}
