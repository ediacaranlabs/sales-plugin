package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import br.com.uoutec.community.ediacaran.sales.entity.InvoiceResultSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.entity.InvoicesResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class InvoiceSearchResultPubEntity extends AbstractPubEntity<InvoicesResultSearch> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private boolean hasNextPage;
	
	private int maxPages;
	
	private int page;
	
	private List<InvoiceResultSearchPubEntity> data;

	public InvoiceSearchResultPubEntity(InvoicesResultSearch invoicesResultSearch, Locale locale) {
		super();
		this.maxPages = invoicesResultSearch.getMaxPages();
		this.page = invoicesResultSearch.getPage();
		this.hasNextPage = invoicesResultSearch.getHasNextPage();
		
		if(invoicesResultSearch.getItens() != null) {
			DateTimeFormatter dtaFormt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale);
			this.data = invoicesResultSearch.getItens().stream()
					.map((e)->new InvoiceResultSearchPubEntity(e, locale, dtaFormt)).collect(Collectors.toList());
		}
	}

	@Override
	protected boolean isEqualId(InvoicesResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(InvoicesResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected InvoicesResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected InvoicesResultSearch createNewInstance() throws Throwable {
		return null;
	}

	@Override
	protected void copyTo(InvoicesResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
	}
	
	public int getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<InvoiceResultSearchPubEntity> getData() {
		return data;
	}

	public void setData(List<InvoiceResultSearchPubEntity> data) {
		this.data = data;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
}
