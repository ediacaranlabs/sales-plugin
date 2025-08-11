package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.ActionsPluginInstaller;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductRequestReportEntityAccess;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequestBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;

public class OrderReportRegistryUtil {

	public static void validate(OrderReport e, Class<?> ... groups) throws ValidationException {
		ValidatorBean.validate(e, groups);
	}

	public static boolean exists(OrderReport e, OrderReportEntityAccess entityAccess) throws OrderReportRegistryException {
		try {
			return entityAccess.findById(e.getId()) != null;
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(
				"order report error: " + e.getOrder(), x);
		}
	}
	
	public static void save(OrderReport e, OrderReportEntityAccess entityAccess) throws OrderReportRegistryException {
		try {
			entityAccess.save(e);
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(
				"order report error: " + e.getOrder(), x);
		}
	}

	public static void update(OrderReport e, OrderReportEntityAccess entityAccess) throws OrderReportRegistryException {
		try {
			entityAccess.update(e);
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(
				"order report error: " + e.getOrder(), x);
		}
	}

	public static void delete(OrderReport e, OrderReportEntityAccess entityAccess) throws OrderReportRegistryException {
		try {
			entityAccess.delete(e);
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(
				"order report error: " + e.getOrder(), x);
		}
	}

	public static OrderReport getOrderReportById(String id, OrderReportEntityAccess entityAccess, ClientRegistry clientRegistry) throws OrderReportRegistryException{
		try {
			OrderReport e = entityAccess.findById(id);
			e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
			return e;
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(x);
		}
	}

	public static OrderReport reload(OrderReport o, OrderReportEntityAccess entityAccess, ClientRegistry clientRegistry) throws OrderReportRegistryException{
		try {
			OrderReport e = entityAccess.findById(o.getId());
			e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
			return e;
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(x);
		}
	}
	
	public static void sendToRepository(OrderReportEntityAccess entityAccess) throws OrderReportRegistryException {
		try {
			entityAccess.flush();
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(x);
		}
	}
	
	public static List<OrderReport> findByOrder(Order e, OrderReportEntityAccess entityAccess) throws OrderReportRegistryException {
		try {
			return entityAccess.findByOrder(e.getId());
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(
				"order report error: " + e.getId(), x);
		}
	}
	
	public static OrderReportResultSearch searchOrderReport(OrderReportSearch value, OrderReportIndexEntityAccess indexEntityAccess, ClientRegistry clientRegistry) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getSearchPermission());
		
		try{
			int page = value.getPage() == null? 1 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<OrderReport> list = indexEntityAccess.search(value, firstResult, maxResults);
			List<OrderReport> itens = new ArrayList<>();
			
			for(OrderReport e: list) {
				e = indexEntityAccess.findById(e.getId());
				e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
				itens.add(e);
			}
			
			return new OrderReportResultSearch(itens.size() > maxItens, -1, page, itens.size() > maxItens? itens.subList(0, maxItens -1) : itens);
		}
		catch(Throwable e){
			throw new OrderReportRegistryException(e);
		}
	}

	public static void registerOrderReportRegisterEvent(ActionRegistry actionRegistry, OrderReport e, boolean newOrderReport) {
		
		if(newOrderReport) {
			registerNewOrderReport(actionRegistry, e);
		}
		else {
			registerOrderReportStatusChange(actionRegistry, e);
		}
		
	}
	
	public static void registerNewOrderReport(ActionRegistry actionRegistry, OrderReport e) {
		actionRegistry.executeAction(
				ActionsPluginInstaller.NEW_ORDER_REPORT, 
				ActionExecutorRequestBuilder.builder()
					.withParameter("orderReport", e.getId())
				.build()
		);
	}
	
	public static void registerOrderReportStatusChange(ActionRegistry actionRegistry, OrderReport e) {
		actionRegistry.executeAction(
				ActionsPluginInstaller.CHANGE_ORDER_REPORT_STATUS, 
				ActionExecutorRequestBuilder.builder()
					.withParameter("orderReport", e.getId())
				.build()
		);
	}

	public static ProductRequestReport getProductRequestReport(String id, OrderReport orderReport, ProductRequestReportEntityAccess productRequestReportEntityAccess) throws OrderReportRegistryException{
		
		try {
			ProductRequestReport e = productRequestReportEntityAccess.findById(id);
			return orderReport.getId().equals(e.getOrderReport())? e : null;
		}
		catch(Throwable ex) {
			throw new OrderReportRegistryException(ex);
		}
		
	}
	
	public static List<ProductRequestReport> getProductRequestReportByOrderReport(OrderReport orderReport, ProductRequestReportEntityAccess productRequestReportEntityAccess) throws OrderReportRegistryException{
		
		try {
			return productRequestReportEntityAccess.getByOrderReport(orderReport.getId(), null, null);
		}
		catch(Throwable ex) {
			throw new OrderReportRegistryException(ex);
		}
		
	}
	
	public static OrderReport toOrderReport(Order order, OrderRegistry orderRegistry) throws OrderReportRegistryException {
		
		Order e;
		
		try {
			e = orderRegistry.findById(order.getId());
		}
		catch(Throwable ex) {
			throw new OrderReportRegistryException(ex);
		}
		
		List<ProductRequestReport> list = new ArrayList<>();
		
		for(ProductRequest r: e.getItens()) {
			ProductRequestReport pr = new ProductRequestReport(r);
			pr.setCause(null);
		}
		
		
		OrderReport or = new OrderReport();
		
		or.setClient(e.getClient());
		or.setDate(LocalDateTime.now());
		or.setId(null);
		or.setProducts(list);
		or.setStatus(OrderReportStatus.NEW_REQUEST);
		or.setUser(null);
		
		return or;
	}
	
}
