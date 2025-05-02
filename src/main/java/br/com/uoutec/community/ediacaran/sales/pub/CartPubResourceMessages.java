package br.com.uoutec.community.ediacaran.sales.pub;

import br.com.uoutec.i18n.MessageBundle;

public class CartPubResourceMessages {

	public static final String RESOURCE_BUNDLE = 
			MessageBundle
			.toPackageID(CartPubResource.class);
	
	public final class index{
		
		public final class error{
			
			public static final String generic_error = "index.error.generic_error";
			
		}
		
	}

	public final class update_units{
		
		public final class error{
			
			public static final String invalid_index = "update_units.error.invalid_index";
			
			public static final String update_qty = "update_units.error.update_qty";
			
		}
		
	}

	public final class add{
		
		public final class error{
			
			public static final String product_not_found = "add.error.product_not_found";

			public static final String add_item = "add.error.add_item";
			
		}
		
	}

	public final class remove{
		
		public final class error{
			
			public static final String invalid_index = "remove.error.invalid_index";

			public static final String remove = "remove.error.remove";
			
		}
		
	}

	public final class checkout{
		
		public final class error{
			
			public static final String cart_empty = "checkout.error.cart_empty";

			public static final String invalid_data = "checkout.error.invalid_data";

			public static final String user_exists = "checkout.error.user_exists";
			
			public static final String payment_gateway_load_fail = "checkout.error.payment_gateway_load_fail";
			
			public static final String cart_item_unavailable = "checkout.error.cart_item_unavailable";
			
			public static final String mount_order_fail = "checkout.error.mount_order_fail";
			
			public static final String registry_order_fail = "checkout.error.registry_order_fail";
			
		}
		
	}
	
}
