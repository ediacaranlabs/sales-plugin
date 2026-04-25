package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;

import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class RefundSearchPubEntity extends AbstractPubEntity<RefundSearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	private String id;
	
	private Integer client;

	private String clientName;
	
	private LocalDateTime startDate;
	
	private LocalDateTime endDate;
	
	private LocalDateTime startRefundDate;
	
	private LocalDateTime endRefundDate;
	
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

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getStartRefundDate() {
		return startRefundDate;
	}

	public void setStartRefundDate(LocalDateTime startRefundDate) {
		this.startRefundDate = startRefundDate;
	}

	public LocalDateTime getEndRefundDate() {
		return endRefundDate;
	}

	public void setEndRefundDate(LocalDateTime endRefundDate) {
		this.endRefundDate = endRefundDate;
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
	protected boolean isEqualId(RefundSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(RefundSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected RefundSearch reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected RefundSearch createNewInstance() throws Throwable {
		return new RefundSearch();
	}

	@Override
	protected void copyTo(RefundSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setId(id);
		o.setClient(client);
		o.setClientName(clientName);
		o.setPage(page);
		o.setResultPerPage(resultPerPage);
		o.setStartDate(startDate);
		o.setEndDate(endDate);
		o.setStartRefundDate(startRefundDate);
		o.setEndRefundDate(endRefundDate);
	}
	
}
