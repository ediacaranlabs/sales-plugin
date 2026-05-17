package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.i18n.ValidationException;

public interface RefundRegistry extends PublicBean{

	void registerRefund(Refund entity) throws RefundRegistryException, ClientRegistryException, 
	ShippingRegistryException, OrderRegistryException, OrderReportRegistryException, ValidationException, 
	PaymentGatewayException, InvoiceRegistryException;
	
	void removeRefund(Refund entity) throws RefundRegistryException;
	
	Refund findRefundById(String id) throws RefundRegistryException;

	RefundResultSearch searchRefund(RefundSearch value) throws RefundRegistryException;
	
	Refund createRefund(Order order, Map<String, Integer> itens) throws RefundRegistryException, InvalidUnitsOrderRegistryException;

	Refund createRefund(Order order) throws RefundRegistryException, InvalidUnitsOrderRegistryException;
	
	void confirmRefund(Refund entity) throws RefundRegistryException, ClientRegistryException, ShippingRegistryException, 
	InvalidUnitsOrderRegistryException, OrderReportRegistryException, OrderRegistryException, PaymentGatewayException;
	
	List<Refund> findRefundByOrder(String id) throws RefundRegistryException;
	
}
