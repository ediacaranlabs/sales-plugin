package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.MappingTypes;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearchResult;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ClientSearchResultPubEntity extends AbstractPubEntity<ClientSearchResult> {
	
	private static final long serialVersionUID = 8112064051350456421L;

	private Boolean hasNextPage;
	
	private Integer maxPages;
	
	private Integer page;
	
	@Basic(mappingType = MappingTypes.OBJECT)
	private List<ClientPubEntity> itens;

	@Constructor
	public ClientSearchResultPubEntity() {
	}
	
	public ClientSearchResultPubEntity(ClientSearchResult e, Locale locale) {
		this.maxPages = e.getMaxPages();
		this.page = e.getPage();
		this.hasNextPage = e.isHasNextPage();
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
	protected boolean isEqualId(ClientSearchResult instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ClientSearchResult instance) throws Throwable {
		return false;
	}

	@Override
	protected ClientSearchResult reloadEntity() throws Throwable {
		return null;
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected ClientSearchResult createNewInstance() throws Throwable {
		return new ClientSearchResult();
	}

	@Override
	protected void copyTo(ClientSearchResult o, boolean reload, boolean override, boolean validate) throws Throwable {
		o.setHasNextPage(this.hasNextPage == null? false: this.hasNextPage.booleanValue());
		o.setMaxPages(this.maxPages == null? -1 : this.maxPages.intValue());
		o.setPage(this.page == null? -1 : this.page.intValue());
		
		if(this.itens != null) {
			List<SystemUser> list = new ArrayList<>();
			for(ClientPubEntity p: this.itens) {
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

	public List<ClientPubEntity> getItens() {
		return itens;
	}

	public void setItens(List<ClientPubEntity> itens) {
		this.itens = itens;
	}

	
}
