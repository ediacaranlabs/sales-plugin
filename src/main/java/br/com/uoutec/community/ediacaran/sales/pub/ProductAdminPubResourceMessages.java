package br.com.uoutec.community.ediacaran.sales.pub;

import br.com.uoutec.i18n.MessageBundle;

public class ProductAdminPubResourceMessages {

	public static final String RESOURCE_BUNDLE = 
			MessageBundle
			.toPackageID(ProductAdminPubResource.class);
	
	public final class search_product{
		
		public final class error{
			
			public static final String fail_load_request = "search_product.error.fail_load_request";
			
			public static final String fail_search = "search_product.error.fail_search";
			
		}
		
	}
	
}
