package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingsResultSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ShippingsSearchResultPubEntity extends AbstractPubEntity<ShippingsResultSearch> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ClientPubEntity> itens;

	@Constructor
	public ShippingsSearchResultPubEntity() {
	}
	
	public ShippingsSearchResultPubEntity(ShippingsResultSearch e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.getHasNextPage();
		this.itens = new ArrayList<>();
		if(e.getItens() != null) {
			for(Client p: e.getItens()) {
				ClientPubEntity x = new ClientPubEntity(p, locale).getType();
				x.setData(null);
				itens.add(x);
			}
		}
	}

	@Override
	protected boolean isEqualId(ShippingsResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ShippingsResultSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ShippingsResultSearch reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ShippingsResultSearch createNewInstance() throws Throwable {
		return new ClientSearchResult();
	}

	@Override
	protected void copyTo(ShippingsResultSearch o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setHasNextPage(this.hasNextPage == null? false: this.hasNextPage.booleanValue());
		o.setMaxPages(this.maxPages == null? -1 : this.maxPages.intValue());
		o.setPage(this.page == null? -1 : this.page.intValue());
		
		if(this.itens != null) {
			List<Client> list = new ArrayList<>();
			for(ClientPubEntity p: this.itens) {
				list.add((Client)p.rebuild(reload, override, validate));
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

	public List<ClientPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ClientPubEntity> itens) {
		this.itens = itens;
	}

	
}
