package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.i18n.ValidationException;

public interface OrderReportRegistry extends PublicBean {

	void registerOrderReport(OrderReport entity) throws  OrderReportRegistryException, ValidationException;
	
	void removeOrderReport(OrderReport entity) throws OrderReportRegistryException;
	
	OrderReport findById(String id) throws OrderReportRegistryException;
	
	List<OrderReport> findByOrder(Order order) throws OrderReportRegistryException;
	
	OrderReportResultSearch searchOrderReport(OrderReportSearch value) throws OrderReportRegistryException;

	OrderReport findByIdAndUser(String id, SystemUser user) throws OrderReportRegistryException;
	
	OrderReportResultSearch searchOrderReportByUser(OrderReportSearch value, SystemUser user) throws OrderReportRegistryException;
	
	void setOrderReportUser(OrderReport orderReport, SystemUser user) throws OrderReportRegistryException;
	
	void registerOrderReportMessage(OrderReportMessage message) throws OrderReportRegistryException;

	List<OrderReportMessage> getMessages(OrderReport orderReport, Integer first, Integer max) throws OrderReportRegistryException;
	
}
