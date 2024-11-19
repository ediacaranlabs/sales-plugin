package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;

import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class InvoiceSearchPubEntity extends AbstractPubEntity<InvoiceSearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	private String id;
	
	private Integer owner;

	private String ownerName;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private OrderStatus status;

	private BigDecimal minTotal;

	private BigDecimal maxTotal;
	
	private String order;
	
	private Integer page;
	
	@Max(100)
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Override
	protected boolean isEqualId(InvoiceSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(InvoiceSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected InvoiceSearch reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected InvoiceSearch createNewInstance() throws Throwable {
		return new InvoiceSearch();
	}

	@Override
	protected void copyTo(InvoiceSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setEndDate(endDate);
		o.setId(id);
		o.setMaxTotal(maxTotal);
		o.setMinTotal(minTotal);
		o.setOwner(owner);
		o.setPage(page);
		o.setResultPerPage(resultPerPage);
		o.setStartDate(startDate);
		o.setOwnerName(this.ownerName);
		o.setOrder(order);
	}
	
}
