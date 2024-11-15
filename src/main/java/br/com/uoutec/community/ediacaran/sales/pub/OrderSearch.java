package br.com.uoutec.community.ediacaran.sales.pub;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;

public class OrderSearch implements Serializable{
	
	private static final long serialVersionUID = 2549319270886774890L;

	private String id;
	
	private Integer owner;

	private String ownerName;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private OrderStatus status;

	private BigDecimal minTotal;

	private BigDecimal maxTotal;
	
	private Integer page;
	
	private Integer resultPerPage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public BigDecimal getMinTotal() {
		return minTotal;
	}

	public void setMinTotal(BigDecimal minTotal) {
		this.minTotal = minTotal;
	}

	public BigDecimal getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(BigDecimal maxTotal) {
		this.maxTotal = maxTotal;
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
