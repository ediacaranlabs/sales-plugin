package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessageResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.i18n.ValidationException;

public interface OrderReportRegistry extends PublicBean {

	void registerOrderReport(OrderReport entity) throws  OrderReportRegistryException, ValidationException;
	
	void removeOrderReport(OrderReport entity) throws OrderReportRegistryException;
	
	OrderReport findById(String id) throws OrderReportRegistryException;

	ProductRequestReport getProductRequestReport(String id, OrderReport orderReport) throws OrderReportRegistryException;
	
	OrderReport toOrderReport(Order order) throws OrderReportRegistryException;
	
	List<OrderReport> findByOrder(Order order) throws OrderReportRegistryException;
	
	OrderReportResultSearch searchOrderReport(OrderReportSearch value) throws OrderReportRegistryException;
	
	void sendMessage(OrderReport orderReport, String message, SystemUser user) throws OrderReportRegistryException;

	OrderReportMessage getMessageById(String id) throws OrderReportRegistryException;
	
	OrderReportMessageResultSearch getMessages(OrderReport orderReport, Integer page, Integer quantityPerPage) throws OrderReportRegistryException;
	
}
