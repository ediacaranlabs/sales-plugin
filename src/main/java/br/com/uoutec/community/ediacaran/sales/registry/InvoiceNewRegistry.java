package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.persistence.EntityAccessException;

public class InvoiceNewRegistry {

	private ProductTypeRegistry productTypeRegistry;
	
	private SystemUserRegistry systemUserRegistry;
	
	private OrderRegistry orderRegistry;
	
	private InvoiceEntityAccess entityAccess;
	
	public InvoiceNewRegistry(ProductTypeRegistry productTypeRegistry, SystemUserRegistry systemUserRegistry,
			OrderRegistry orderRegistry, InvoiceEntityAccess entityAccess) {
		this.productTypeRegistry = productTypeRegistry;
		this.systemUserRegistry = systemUserRegistry;
		this.orderRegistry = orderRegistry;
		this.entityAccess = entityAccess;
	}

	public Invoice create(Order order, SystemUser systemUser, Invoice invoice, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException, InvoiceRegistryException, EntityAccessException {

		Order actualOrder = getActualOrder(order);
		
		InvoiceRegistryUtil.checkPayment(actualOrder);
		
		SystemUser actualUser = getActualUser(actualOrder, systemUser);		
		List<Invoice> actualInvoices = getActualInvoices(actualOrder, actualUser);
		
		InvoiceRegistryUtil.checkInvoice(actualOrder, actualInvoices, invoice);
		registerProducts(invoice, actualUser, actualOrder);
		
		save(invoice, actualOrder);
		
		List<Invoice> allInvoices = new ArrayList<>(actualInvoices);
		allInvoices.add(invoice);
		
		InvoiceRegistryUtil.markAsComplete(order, allInvoices, EntityContextPlugin.getEntity(OrderRegistry.class));
		
		registerEvent(invoice, actualOrder, message);
		
		return invoice;
	}
	
	private void registerEvent(Invoice invoice, Order order, String message) throws OrderRegistryException {
		orderRegistry.registryLog(order.getId(), message != null? message : "Criada a fatura #" + invoice.getId() );
	}
	
	private void save(Invoice invoice, Order order) throws InvoiceRegistryException {
		try {
			entityAccess.save(invoice);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(
				"invoice error: " + order.getId(), e);
		}
	}
	private void registerProducts(Invoice invoice, SystemUser user, Order order) throws InvoiceRegistryException {
		for(ProductRequest productRequest: invoice.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.registryItem(user, order, productRequest);
			}
			catch(Throwable e){
				throw new InvoiceRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId(), e);
			}
		}
	}
	
	private Order getActualOrder(Order order) throws OrderRegistryException {
		return orderRegistry.findById(order.getId());		
	}
	
	private List<Invoice> getActualInvoices(Order order, SystemUser user) throws EntityAccessException{
		return entityAccess.findByOrder(order.getId(), user);		
	}
	
	private SystemUser getActualUser(Order order, SystemUser user) throws OrderRegistryException, InvoiceRegistryException {
		SystemUser actualUser;
		try{
			actualUser = this.systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getOwner());
		}
		
		if(actualUser.getId() != user.getId()) {
			throw new InvoiceRegistryException("invalid user: " + actualUser.getId()+ " != " + user.getId());
		}
		
		return actualUser;
	}
	
}
