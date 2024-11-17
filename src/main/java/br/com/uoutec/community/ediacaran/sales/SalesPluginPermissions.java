package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.application.security.RuntimeSecurityPermission;
import br.com.uoutec.application.security.SecurityPermission;

public final class SalesPluginPermissions {

	public static final String basePermission = "app.registry.sales.";
	
	public static class PAYMENT_GATEWAY {
		
		public static final String basePermission = SalesPluginPermissions.basePermission + "payment.";
		
		public static SecurityPermission getRegisterPermission() {
			return new RuntimeSecurityPermission(basePermission + "register");
		}

		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}
		
	}

	public static class CART {
		
		public static final String basePermission = SalesPluginPermissions.basePermission + "cart.";
		
		public static SecurityPermission getCheckoutPermission() {
			return new RuntimeSecurityPermission(basePermission + "checkout");
		}

		public static SecurityPermission getCheckAvailabilityPermission() {
			return new RuntimeSecurityPermission(basePermission + "check_availability");
		}

		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}

		public static SecurityPermission getAddPermission() {
			return new RuntimeSecurityPermission(basePermission + "add");
		}
		
		public static SecurityPermission getSetQuantityPermission() {
			return new RuntimeSecurityPermission(basePermission + "quantity");
		}
		
	}

	public static class ORDER_REGISTRY {
		
		public static final String basePermission = SalesPluginPermissions.basePermission + "order.";
		
		public static SecurityPermission getRegisterPermission() {
			return new RuntimeSecurityPermission(basePermission + "register");
		}

		public static SecurityPermission getFindPermission() {
			return new RuntimeSecurityPermission(basePermission + "find");
		}

		public static SecurityPermission getCreatePermission() {
			return new RuntimeSecurityPermission(basePermission + "create");
		}
		
		public static SecurityPermission getSearchPermission() {
			return new RuntimeSecurityPermission(basePermission + "search");
		}

		public static SecurityPermission getListPermission() {
			return new RuntimeSecurityPermission(basePermission + "list");
		}
		
		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}

		public static SecurityPermission getAddPermission() {
			return new RuntimeSecurityPermission(basePermission + "add");
		}
		
		public static SecurityPermission getSetQuantityPermission() {
			return new RuntimeSecurityPermission(basePermission + "quantity");
		}

		public static SecurityPermission getRefoundPermission() {
			return new RuntimeSecurityPermission(basePermission + "refound");
		}
		
		public static class REFOUND {

			public static final String basePermission = SalesPluginPermissions.ORDER_REGISTRY.basePermission + "refound.";

			public static SecurityPermission getRevertPermission() {
				return new RuntimeSecurityPermission(basePermission + "revert");
			}
			
		}

		public static class LOGS {

			public static final String basePermission = SalesPluginPermissions.ORDER_REGISTRY.basePermission + "logs.";

			public static SecurityPermission getRegisterPermission() {
				return new RuntimeSecurityPermission(basePermission + "register");
			}
			
			public static SecurityPermission getListPermission() {
				return new RuntimeSecurityPermission(basePermission + "list");
			}
			
		}
		
		public static class GET {

			public static final String basePermission = SalesPluginPermissions.ORDER_REGISTRY.basePermission + "get.";
			
			public static SecurityPermission getProductRequestPermission() {
				return new RuntimeSecurityPermission(basePermission + "product_request");
			}
			
		}
		
	}
	
	public static class PRODUCT_TYPE {
		
		public static final String basePermission = SalesPluginPermissions.basePermission + "product_type.";
		
		public static SecurityPermission getRegisterPermission() {
			return new RuntimeSecurityPermission(basePermission + "register");
		}

		public static SecurityPermission getListPermission() {
			return new RuntimeSecurityPermission(basePermission + "list");
		}
		
		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}

		public static SecurityPermission getGetPermission() {
			return new RuntimeSecurityPermission(basePermission + "get");
		}
		
	}
	
	public static class INVOICE_REGISTRY {

		public static final String basePermission = SalesPluginPermissions.basePermission + "invoice.";
		
		public static SecurityPermission getRegisterPermission() {
			return new RuntimeSecurityPermission(basePermission + "register");
		}

		public static SecurityPermission getListPermission() {
			return new RuntimeSecurityPermission(basePermission + "list");
		}
		
		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}

		public static SecurityPermission getGetPermission() {
			return new RuntimeSecurityPermission(basePermission + "get");
		}
		
		public static SecurityPermission getFindPermission() {
			return new RuntimeSecurityPermission(basePermission + "find");
		}

		public static SecurityPermission getCreatePermission() {
			return new RuntimeSecurityPermission(basePermission + "create");
		}
		
	}
	
}
