package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RefundSearch implements Serializable {
	
	private static final long serialVersionUID = 2549319270886774890L;

	private String id;
	
	private Integer client;

	private String clientName;
	
	private String order;
	
	private LocalDateTime startDate;
	
	private LocalDateTime endDate;
	
	private LocalDateTime startRefundDate;
	
	private LocalDateTime endRefundDate;
	
	private Integer page;
	
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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

}
