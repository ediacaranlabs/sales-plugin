package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.ActionsPluginInstaller;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessageResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.OrderReportMessageEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ProductRequestReportEntityAccess;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequestBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

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

	public static OrderReport getOrderReportById(String id, OrderReportEntityAccess entityAccess, ClientRegistry clientRegistry, OrderRegistry orderRegistry) throws OrderReportRegistryException{
		try {
			OrderReport e = entityAccess.findById(id);
			e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
			e.setOrder(e.getOrder() == null? null : orderRegistry.findById(e.getOrder().getId()));
			return e;
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(x);
		}
	}

	public static OrderReport reload(OrderReport o, OrderReportEntityAccess entityAccess, ClientRegistry clientRegistry, OrderRegistry orderRegistry) throws OrderReportRegistryException{
		try {
			OrderReport e = entityAccess.findById(o.getId());
			e.setClient(e.getClient() == null? null : clientRegistry.findClientById(e.getClient().getId()));
			e.setOrder(e.getOrder() == null? null : orderRegistry.findById(e.getOrder().getId()));
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

	public static void sendToRepository(OrderReportMessageEntityAccess entityAccess) throws OrderReportRegistryException {
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
	
	public static OrderReportResultSearch searchOrderReport(OrderReportSearch value, OrderReportEntityAccess entityAccess, OrderReportIndexEntityAccess indexEntityAccess, ClientRegistry clientRegistry, OrderRegistry orderRegistry) throws OrderReportRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_REGISTRY.getSearchPermission());
		
		try{
			int page = value.getPage() == null? 1 : value.getPage().intValue();
			int maxItens = value.getResultPerPage() == null? 10 : value.getResultPerPage();
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			List<OrderReport> list = indexEntityAccess.search(value, firstResult, maxResults);
			List<OrderReport> itens = new ArrayList<>();
			
			for(OrderReport e: list) {
				e = reload(e, entityAccess, clientRegistry, orderRegistry);
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
			list.add(pr);
		}
		
		
		OrderReport or = new OrderReport();
		
		or.setClient(e.getClient());
		or.setDate(LocalDateTime.now());
		or.setId(null);
		or.setProducts(list);
		or.setStatus(OrderReportStatus.NEW_REQUEST);
		or.setUser(null);
		
		try {
			or.setOrder(orderRegistry.findById(order.getId()));
		}
		catch(Throwable ex) {
			throw new OrderReportRegistryException(ex);
		}
		
		return or;
	}

	public static void checkOrderReportStatus(OrderReport e, OrderRegistry orderRegistry, OrderReportEntityAccess entityAccess) throws OrderReportRegistryException {
		
		//check order not is closed
		
		Order order;
		
		try {
			order = orderRegistry.findById(e.getOrder().getId());
		}
		catch(Throwable ex) {
			throw new OrderReportRegistryException(ex);
		}
		
		if(order.isClosed()) {
			throw new OrderReportRegistryException("order is closed");
		}
		
		//check if not exist reported product request
		
		Set<String> reportedProductRequest;
		
		try {
			reportedProductRequest = getReportedProductRequest(e, order, entityAccess);
		}
		catch(Throwable ex) {
			throw new OrderReportRegistryException(ex);
		}
		
		Set<String> actualReportedProductRequest = e.getProducts().stream().map((i)->i.getSerial()).collect(Collectors.toSet());
		
		for(String s: reportedProductRequest) {
			actualReportedProductRequest.remove(s);
		}
		
		if(actualReportedProductRequest.isEmpty()) {
			throw new OrderReportRegistryException("product request has been reported");
		}
		

	}
	
	public static Set<String> getReportedProductRequest(OrderReport e, Order order, OrderReportEntityAccess entityAccess) throws EntityAccessException {
		
		Set<String> result = new HashSet<>();
		List<OrderReport> list = entityAccess.findByOrder(e.getOrder().getId());
		
		for(OrderReport or: list) {
			
			if(or.isClosed()) {
				continue;
			}
			
			for(ProductRequestReport prr: or.getProducts()) {
				result.add(prr.getSerial());
			}
			
		}
		
		return result;
	}

	public static OrderReportMessage toOrderReportMessage(OrderReport e, String message, LocalDateTime date, SystemUser user) throws OrderReportRegistryException {
		OrderReportMessage x = new OrderReportMessage();
		x.setDate(date);
		x.setMessage(message);
		x.setOrderReport(e.getId());
		x.setUser(user);
		return x;
	}
	
	public static void registerMessage(OrderReportMessage e, OrderReportMessageEntityAccess entityAccess) throws OrderReportRegistryException {
		try {
			entityAccess.save(e);
		}
		catch(Throwable x){
			throw new OrderReportRegistryException(
				"order report error: " + e.getOrderReport(), x);
		}
	}

	public static OrderReportMessageResultSearch getOrderReportMessageByOrderReport(OrderReport orderReport, 
			Integer page, Integer maxItens, OrderReportMessageEntityAccess entityAccess) throws OrderReportRegistryException{
		
		try{
			page = page == null? 1 : page;
			maxItens = maxItens == null? 10 : maxItens;
			
			int firstResult = (page - 1)*maxItens;
			int maxResults = maxItens + 1;
			
			List<OrderReportMessage> itens = entityAccess.getByOrderReport(orderReport.getId(), firstResult, maxResults);
			return new OrderReportMessageResultSearch(itens.size() > maxItens, -1, page, itens.size() > maxItens? itens.subList(0, maxItens -1) : itens);
		}
		catch(Throwable e){
			throw new OrderReportRegistryException(e);
		}
		
	}
	
}
