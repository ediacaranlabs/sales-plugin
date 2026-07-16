package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.user.SystemUserIDProvider;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class OrderPanelPubEntity 
	extends OrderPubEntity {

	@Transient
	private static final long serialVersionUID = 7794103768364892856L;
	
	public OrderPanelPubEntity() {
		//Ignore validation
		//the copyTo must be empty
		super.setDate(LocalDateTime.now());
		super.setItens(new ArrayList<>());
		super.setPayment(new PaymentPubEntity());
		super.setStatus(OrderStatus.NEW.toString());
		
		ProductRequestPubEntity empty = new ProductRequestPubEntity();
		empty.setCost(BigDecimal.ZERO);
		empty.setCurrency("BRL");
		empty.setOrderId("");
		empty.setSerial("XXXXXXXXXXXXXXX");
		empty.setUnits(0);
		empty.setAddData(new HashMap<>());
		empty.setProduct(new ProductPubEntity());
		empty.setId("00000000000000000");
		
		super.getItens().add(empty);
		
	}
	
	@Override
	protected Order reloadEntity() throws Throwable {
		OrderRegistry r = EntityContextPlugin.getEntity(OrderRegistry.class);
		Order order = r.findById(super.getId());
		return order != null && SystemUserIDProvider.getSystemUser().getId().equals(order.getClient().getId())? order : null;
	}

	protected void copyTo(Order o, boolean reload, boolean override,
			boolean validate) throws Throwable {
	}
	
}
