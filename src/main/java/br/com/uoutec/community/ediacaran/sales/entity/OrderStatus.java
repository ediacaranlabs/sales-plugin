package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum OrderStatus {

	NEW,
	
	ON_HOLD,
	
	PENDING_PAYMENT,
	
	PAYMENT_RECEIVED,
	
	ORDER_INVOICED,
	
	ORDER_SHIPPED,

	COMPLETE,
	
	CLOSED,
	
	ARCHIVED,
	
	CANCELED,

	REFOUND;
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(OrderStatus.class);
	
	public String getName(Locale locale) {
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		return i18nRegistry
					.getString(
							RESOURCE_BUNDLE,
							name().toLowerCase(), 
							locale
					);		
	}

	public boolean isValidNextStatus(OrderStatus newStatus) {
		return StatusCheck.isValid(this, newStatus);
	}
	
	private static class StatusCheck {
		
		private static final Map<OrderStatus, Set<OrderStatus>> nextState;
		
		static{
			nextState = new HashMap<OrderStatus, Set<OrderStatus>>();
			
			nextState.put(OrderStatus.NEW, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.PENDING_PAYMENT,
							OrderStatus.CANCELED,
							OrderStatus.ON_HOLD))
				);
			
			nextState.put(OrderStatus.ON_HOLD, 
				new HashSet<OrderStatus>(Arrays.asList(OrderStatus.values()))
			);
			
			nextState.put(OrderStatus.PENDING_PAYMENT, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.CANCELED,
							OrderStatus.PAYMENT_RECEIVED))
				);

			nextState.put(OrderStatus.PAYMENT_RECEIVED, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.REFOUND,
							OrderStatus.ORDER_INVOICED))
				);

			nextState.put(OrderStatus.ORDER_INVOICED, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.ORDER_SHIPPED,
							OrderStatus.COMPLETE))
				);

			nextState.put(OrderStatus.ORDER_SHIPPED, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.COMPLETE))
				);

			nextState.put(OrderStatus.REFOUND, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.CLOSED))
				);
			
			nextState.put(OrderStatus.CLOSED, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.ARCHIVED))
				);

			nextState.put(OrderStatus.COMPLETE, 
					new HashSet<OrderStatus>(Arrays.asList(
							OrderStatus.ARCHIVED))
				);
			
			nextState.put(OrderStatus.CANCELED, 
					new HashSet<OrderStatus>(Arrays.asList())
				);
			
		}
		
		public static boolean isValid(OrderStatus currentStatus, OrderStatus newStatus){
			Set<OrderStatus> nextStatus = nextState.get(currentStatus);
			return nextStatus != null && nextStatus.contains(newStatus);
		}
		
	}
	
}
