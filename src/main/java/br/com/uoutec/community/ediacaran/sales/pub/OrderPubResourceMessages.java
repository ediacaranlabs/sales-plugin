package br.com.uoutec.community.ediacaran.sales.pub;

import br.com.uoutec.i18n.MessageBundle;

public class OrderPubResourceMessages {

	public static final String RESOURCE_BUNDLE = 
			MessageBundle
			.toPackageID(OrderPubResource.class);
	
	public final class show_orders{
		
		public final class error{
			
			public static final String fail_load_orders = "show_orders.error.fail_load_orders";
			
		}
		
	}
	
	public final class order_detail{
		
		public final class error{
			
			public static final String fail_load_entity = "order_detail.error.fail_load_entity";
			
			public static final String fail_load_payment_gateway = "order_detail.error.fail_load_payment_gateway";
		}
		
	}
}
