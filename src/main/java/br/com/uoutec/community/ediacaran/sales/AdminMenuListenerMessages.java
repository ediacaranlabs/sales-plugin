package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.i18n.MessageBundle;

public class AdminMenuListenerMessages {

	public static final String RESOURCE_BUNDLE = MessageBundle.toPackageID(AdminMenuListener.class);
	
	public static class installMenu {
		
		public static final class admin_menu {
			
			public static final class account_menu {

				public static final String name = "admin_menu.account_menu";

				public static final class itens {
					
					public static final String orders = "admin_menu.account_menu.itens.orders";
	
					public static final String invoices = "admin_menu.account_menu.itens.invoices";
					
				}
				
			}
			
			public static final class sales_menu {

				public static final String name = "admin_menu.sales_menu";
				
				public static final class itens {
					
					public static final String orders = "admin_menu.sales_menu.itens.orders";
	
					public static final String invoices = "admin_menu.sales_menu.itens.invoices";

					public static final String shippings = "admin_menu.sales_menu.itens.shippings";
					
					public static final String clients = "admin_menu.sales_menu.itens.clients";
					
					public static final String products = "admin_menu.sales_menu.itens.products";

					public static final String product_metadata = "admin_menu.sales_menu.itens.product_metadata";
					
				}
				
			}
			
		}
		
	}
	
}
