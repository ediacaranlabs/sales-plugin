package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDate;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;

import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ShippingSearchPubEntity extends AbstractPubEntity<ShippingSearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	private String id;
	
	private Integer client;

	private String clientName;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private LocalDate startReceivedDate;
	
	private LocalDate endReceivedDate;
	
	private Boolean closed;
	
	private Integer page;
	
	@Max(100)
	private Integer resultPerPage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getStartReceivedDate() {
		return startReceivedDate;
	}

	public void setStartReceivedDate(LocalDate startReceivedDate) {
		this.startReceivedDate = startReceivedDate;
	}

	public LocalDate getEndReceivedDate() {
		return endReceivedDate;
	}

	public void setEndReceivedDate(LocalDate endReceivedDate) {
		this.endReceivedDate = endReceivedDate;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getResultPerPage() {
		return resultPerPage;
	}

	public void setResultPerPage(Integer resultPerPage) {
		this.resultPerPage = resultPerPage;
	}

	@Override
	protected boolean isEqualId(ShippingSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ShippingSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ShippingSearch reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected ShippingSearch createNewInstance() throws Throwable {
		return new ShippingSearch();
	}

	@Override
	protected void copyTo(ShippingSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setId(id);
		o.setClient(client);
		o.setClientName(clientName);
		o.setPage(page);
		o.setResultPerPage(resultPerPage);
		o.setStartDate(startDate);
		o.setEndDate(endDate);
		o.setStartReceivedDate(startReceivedDate);
		o.setEndReceivedDate(endReceivedDate);
		o.setClosed(closed);
	}
	
}
