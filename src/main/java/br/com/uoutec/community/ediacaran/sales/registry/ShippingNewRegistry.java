package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.persistence.EntityAccessException;

public class ShippingNewRegistry {

	private ClientRegistry clientRegistry;
	
	private OrderRegistry orderRegistry;
	
	private ShippingEntityAccess entityAccess;
	
	public ShippingNewRegistry(ClientRegistry clientRegistry,
			OrderRegistry orderRegistry, ShippingEntityAccess entityAccess) {
		this.clientRegistry = clientRegistry;
		this.orderRegistry = orderRegistry;
		this.entityAccess = entityAccess;
	}

	public Shipping create(Order order, Client client, Shipping shipping, String message
			) throws OrderRegistryException, EntityAccessException, CompletedInvoiceRegistryException, ShippingRegistryException {

		Order actualOrder = getActualOrder(order);
		Client actualclient = getActualClient(actualOrder, client);		
		List<Shipping> actualShipping = getActualShipping(actualOrder, actualclient);
		
		ShippingRegistryUtil.checkShipping(actualOrder, actualShipping, shipping);
		save(shipping, actualOrder);
		
		List<Shipping> allShippings = new ArrayList<>(actualShipping);
		allShippings.add(shipping);
		
		ShippingRegistryUtil.markAsComplete(order, allShippings, EntityContextPlugin.getEntity(OrderRegistry.class));
		
		registerEvent(shipping, actualOrder, message);
		
		return shipping;
	}
	
	private void registerEvent(Shipping invoice, Order order, String message) throws OrderRegistryException {
		orderRegistry.registryLog(order.getId(), message != null? message : "registrado o envio #" + invoice.getId() );
	}
	
	private void save(Shipping invoice, Order order) throws ShippingRegistryException {
		try {
			entityAccess.save(invoice);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new ShippingRegistryException(
				"invoice error: " + order.getId(), e);
		}
	}
	
	private Order getActualOrder(Order order) throws OrderRegistryException {
		return orderRegistry.findById(order.getId());		
	}
	
	private List<Shipping> getActualShipping(Order order, Client client) throws EntityAccessException{
		return entityAccess.findByOrder(order.getId(), client);		
	}
	
	private Client getActualClient(Order order, Client client) throws OrderRegistryException, ShippingRegistryException {
		SystemUser actualUser;
		try{
			actualUser = this.clientRegistry.findClientById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getOwner());
		}
		
		if(actualUser.getId() != client.getId()) {
			throw new ShippingRegistryException("invalid user: " + actualUser.getId()+ " != " + client.getId());
		}
		
		return Client.toClient(actualUser);
	}
	
}
