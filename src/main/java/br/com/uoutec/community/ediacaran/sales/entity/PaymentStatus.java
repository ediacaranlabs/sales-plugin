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

public enum PaymentStatus {
			
	NEW,
	
	ON_HOLD,
	
	PENDING_PAYMENT,

	PENDING_PAYMENT_CONFIRMATION,
	
	PAYMENT_RECEIVED,
	
	PAYMENT_REVIEW,
	
	SUSPECTED_FRAUD,
	
	REFOUND;
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(PaymentStatus.class);
	
	public String getName(Locale locale) {
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		return i18nRegistry
					.getString(
							RESOURCE_BUNDLE,
							name().toLowerCase(), 
							locale
					);		
	}
	
	public boolean isValidNextStatus(PaymentStatus newStatus) {
		return StatusCheck.isValid(this, newStatus);
	}
	
	private static class StatusCheck {
		
		private static final Map<PaymentStatus, Set<PaymentStatus>> nextState;
		
		static{
			nextState = new HashMap<PaymentStatus, Set<PaymentStatus>>();

			nextState.put(NEW, 
					new HashSet<PaymentStatus>(Arrays.asList(
							PENDING_PAYMENT,
							PENDING_PAYMENT_CONFIRMATION,
							PAYMENT_RECEIVED,
							PAYMENT_REVIEW))
				);

			nextState.put(ON_HOLD, 
					new HashSet<PaymentStatus>(Arrays.asList(PaymentStatus.values()))
				);

			nextState.put(PENDING_PAYMENT_CONFIRMATION, 
					new HashSet<PaymentStatus>(Arrays.asList(
							PAYMENT_RECEIVED,
							PAYMENT_REVIEW,
							SUSPECTED_FRAUD))
				);
			
			nextState.put(PENDING_PAYMENT, 
					new HashSet<PaymentStatus>(Arrays.asList(
							PAYMENT_RECEIVED,
							PAYMENT_REVIEW,
							SUSPECTED_FRAUD))
				);
			
			nextState.put(PAYMENT_RECEIVED, 
					new HashSet<PaymentStatus>(Arrays.asList(
							REFOUND))
				);

			nextState.put(PAYMENT_REVIEW, 
					new HashSet<PaymentStatus>(Arrays.asList(
							SUSPECTED_FRAUD,
							PAYMENT_RECEIVED))
				);
			
			nextState.put(SUSPECTED_FRAUD, 
					new HashSet<PaymentStatus>(Arrays.asList())
				);
			
			nextState.put(REFOUND, 
					new HashSet<PaymentStatus>(Arrays.asList())
				);
			
		}
		
		public static boolean isValid(PaymentStatus currentStatus, PaymentStatus newStatus){
			Set<PaymentStatus> nextStatus = nextState.get(currentStatus);
			return nextStatus != null && nextStatus.contains(newStatus);
		}
		
	}
}
