package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface RefundRegistry extends PublicBean{

	void registerRefund(Refund entity) throws RefundRegistryException;
	
	void removeRefund(Refund entity) throws RefundRegistryException;
	
	Refund findRefundById(String id) throws RefundRegistryException;

	Refund toRefund(Order order) throws RefundRegistryException;
	
	//ShippingResultSearch searchShipping(ShippingSearch value) throws ShippingRegistryException;
	
	Refund createRefund(Order orderID, Map<String, Integer> itens, String message) throws RefundRegistryException;

	void confirmRefund(Refund shipping) throws RefundRegistryException;
	
	List<Refund> findRefundByOrder(String id) throws RefundRegistryException;
	
}
