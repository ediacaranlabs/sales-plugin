package br.com.uoutec.community.ediacaran.sales.pub;

import br.com.uoutec.i18n.MessageBundle;

public class OrderAdminPubResourceMessages {

	public static final String RESOURCE_BUNDLE = 
			MessageBundle
			.toPackageID(OrderAdminPubResource.class);
	
	public final class index{
		
		public final class error{
			
			public static final String fail_load = "error.fail_load";
			
		}
		
	}

	public final class search {
		
		public final class error {
	
			public static final String fail_load = "search.error.fail_load";
			
		}
		
	}
	
	public final class edit {
		
		public final class error {
			
			public static final String fail_load = "edit.error.fail_load";
			
			public static final String fail_load_payment_gateway = "edit.error.fail_load_payment_gateway";
			
		}
		
	}
	
}
