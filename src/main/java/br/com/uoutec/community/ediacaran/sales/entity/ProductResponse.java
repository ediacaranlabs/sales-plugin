package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ProductResponse extends ProductRequest {

	private static final long serialVersionUID = 7081126482240726397L;

	private Set<Invoice> invoices;

	private Set<Shipping> shippings;

	private Set<Refund> refunds;

	private Set<OrderReport> orderReports;
	
	public ProductResponse(Set<Invoice> invoices, Set<Shipping> shippings, Set<Refund> refunds, Set<OrderReport> orderReports) {
		super();
		this.invoices = invoices;
		this.shippings = shippings;
		this.refunds = refunds;
		this.orderReports = orderReports;
	}

	public ProductResponse(ProductRequest value, Set<Invoice> invoices, Set<Shipping> shippings, Set<Refund> refunds, Set<OrderReport> orderReports) {
		super(value);
		this.invoices = invoices;
		this.shippings = shippings;
		this.refunds = refunds;
		this.orderReports = orderReports;
	}

	/*
	public int getQtyInvoices() {
		int value = 0;
		
		if(invoices != null) {
			
			Map<String, ProductRequest> all = 
					ProductRequestUtil.toMapCollections(
							this.invoices.stream()
								.filter((e)->e.getItens() != null && e.getCancelDate() == null)
								.map((e)->e.getItens())
							.collect(Collectors.toList())
					);
						
			ProductRequestUtil.resetUnits(all);
			
			invoices.stream().forEach((e)->{
				if(e.getItens() != null) {
					ProductRequestUtil.addUnits(all, e.getItens());
				}
			});
		
			value = all.get(super.getSerial()).getUnits();
			
		}
		
		return value;
	}
    */
	
	@SuppressWarnings("unchecked")
	public List<Invoice> getInvoices() {
		
		if(invoices != null) {
			List<Invoice> list = new ArrayList<>();
			
			invoices.stream().forEach((i)->{
				
				if(i.getItens() != null) {
					if(i.getItens().stream().anyMatch((e)->getSerial().equals(e.getSerial()))) {
						list.add(i);
					}
				}
				
			});
			
			return list;
		}
		
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public List<Refund> getRefunds() {
		
		if(refunds != null) {
			List<Refund> list = new ArrayList<>();
			
			refunds.stream().forEach((i)->{
				
				if(i.getProducts() != null) {
					if(i.getProducts().stream().anyMatch((e)->getSerial().equals(e.getSerial()))) {
						list.add(i);
					}
				}
				
			});
			
			return list;
		}
		
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public List<OrderReport> geOrderReports() {
		
		if(refunds != null) {
			List<OrderReport> list = new ArrayList<>();
			
			orderReports.stream().forEach((i)->{
				
				if(i.getProducts() != null) {
					if(i.getProducts().stream().anyMatch((e)->getSerial().equals(e.getSerial()))) {
						list.add(i);
					}
				}
				
			});
			
			return list;
		}
		
		return Collections.EMPTY_LIST;
	}
	
	@SuppressWarnings("unchecked")
	public List<Shipping> getShippings() {
		
		if(shippings != null) {
			List<Shipping> list = new ArrayList<>();
			
			shippings.stream().forEach((i)->{
				
				if(i.getProducts() != null) {
					if(i.getProducts().stream().anyMatch((e)->getSerial().equals(e.getSerial()))) {
						list.add(i);
					}
				}
				
			});
			
			return list;
		}
		
		return Collections.EMPTY_LIST;
	}
	
}
