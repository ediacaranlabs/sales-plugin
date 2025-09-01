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

public enum OrderStatusValue implements OrderStatus {

	NEW(){
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	ON_HOLD(){

		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	PENDING_PAYMENT(){
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	PAYMENT_RECEIVED(){

		public boolean isAllowedCreateInvoice() {
			return true;
		}
		
		public boolean isAllowedChangeInvoice() {
			return true;
		}

		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	ORDER_INVOICED(){

		public boolean isAllowedCreateShipping() {
			return true;
		}
		
		public boolean isAllowedChangeShipping() {
			return true;
		}

		public boolean isAllowedCreateOrderReport() {
			return true;
		}
		
		public boolean isAllowedChangeOrderReport() {
			return true;
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},
	
	ORDER_SHIPPED(){
		
		public boolean isAllowedCreateOrderReport() {
			return false;
		}
		
		public boolean isAllowedChangeOrderReport() {
			return false;
		}
		
		@Override
		public boolean isClosed() {
			return false;
		}
		
	},

	COMPLETE(),
	
	CLOSED(),
	
	ARCHIVED(),
	
	CANCELED(),

	REFOUND();
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(OrderStatusValue.class);
	
	public String getName(Locale locale) {
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		return i18nRegistry
					.getString(
							RESOURCE_BUNDLE,
							name().toLowerCase(), 
							locale
					);		
	}

	public boolean isValidNextStatus(OrderStatusValue newStatus) {
		return StatusCheck.isValid(this, newStatus);
	}
	
	private static class StatusCheck {
		
		private static final Map<OrderStatusValue, Set<OrderStatusValue>> nextState;
		
		static{
			nextState = new HashMap<OrderStatusValue, Set<OrderStatusValue>>();
			
			nextState.put(OrderStatusValue.NEW, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.PENDING_PAYMENT,
							OrderStatusValue.CANCELED,
							OrderStatusValue.ON_HOLD))
				);
			
			nextState.put(OrderStatusValue.ON_HOLD, 
				new HashSet<OrderStatusValue>(Arrays.asList(OrderStatusValue.values()))
			);
			
			nextState.put(OrderStatusValue.PENDING_PAYMENT, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.CANCELED,
							OrderStatusValue.PAYMENT_RECEIVED))
				);

			nextState.put(OrderStatusValue.PAYMENT_RECEIVED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.REFOUND,
							OrderStatusValue.ORDER_INVOICED))
				);

			nextState.put(OrderStatusValue.ORDER_INVOICED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.PAYMENT_RECEIVED,
							OrderStatusValue.ORDER_SHIPPED,
							OrderStatusValue.COMPLETE))
				);

			nextState.put(OrderStatusValue.ORDER_SHIPPED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.ORDER_INVOICED,
							OrderStatusValue.COMPLETE))
				);

			nextState.put(OrderStatusValue.REFOUND, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.CLOSED))
				);
			
			nextState.put(OrderStatusValue.CLOSED, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.ARCHIVED))
				);

			nextState.put(OrderStatusValue.COMPLETE, 
					new HashSet<OrderStatusValue>(Arrays.asList(
							OrderStatusValue.ARCHIVED))
				);
			
			nextState.put(OrderStatusValue.CANCELED, 
					new HashSet<OrderStatusValue>(Arrays.asList())
				);
			
		}
		
		public static boolean isValid(OrderStatusValue currentStatus, OrderStatusValue newStatus){
			if(currentStatus.equals(newStatus)) {
				return true;
			}
			Set<OrderStatusValue> nextStatus = nextState.get(currentStatus);
			return nextStatus != null && nextStatus.contains(newStatus);
		}
		
	}

	@Override
	public boolean isValidNextStatus(OrderStatus newStatus) {
		return isValidNextStatus((OrderStatusValue)newStatus);
	}

	@Override
	public boolean isClosed() {
		return true;
	}
	
	public boolean isAllowedCreateInvoice() {
		return false;
	}
	
	public boolean isAllowedChangeInvoice() {
		return false;
	}

	public boolean isAllowedCreateShipping() {
		return false;
	}
	
	public boolean isAllowedChangeShipping() {
		return false;
	}

	public boolean isAllowedCreateOrderReport() {
		return false;
	}
	
	public boolean isAllowedChangeOrderReport() {
		return false;
	}
	
}
