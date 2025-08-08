package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface OrderReportRegistry extends PublicBean{

	void registerOrderReport(OrderReport entity) throws  OrderReportRegistryException;
	
	void removeOrderReport(OrderReport entity) throws OrderReportRegistryException;
	
	OrderReport findById(String id) throws OrderReportRegistryException;
	
	List<OrderReport> findByOrder(String id) throws OrderReportRegistryException;
	
	OrderReportResultSearch searchShipping(OrderReportSearch value) throws OrderReportRegistryException;
	
}
