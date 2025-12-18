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

	public static class SHIPPING_METHOD {
		
		public static final String basePermission = SalesPluginPermissions.basePermission + "shipping.";
		
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

		public static SecurityPermission getRegisterPaymentPermission() {
			return new RuntimeSecurityPermission(basePermission + "register_payment");
		}

		public static SecurityPermission getUpdatePaymentPermission(String paymentType) {
			return new RuntimeSecurityPermission(basePermission + "register_payment." + paymentType);
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

	public static class PRODUCT {
		
		public static final String basePermission = SalesPluginPermissions.basePermission + "product.";
		
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

		public static class IMAGE {
			
			public static final String basePermission = SalesPluginPermissions.PRODUCT.basePermission + "image.";
			
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

		public static class CATEGORY {

			public static final String basePermission = SalesPluginPermissions.PRODUCT.basePermission + "category.";
			
			public static SecurityPermission getRegisterPermission() {
				return new RuntimeSecurityPermission(basePermission + "register");
			}

			public static SecurityPermission getListPermission() {
				return new RuntimeSecurityPermission(basePermission + "list");
			}
			
			public static SecurityPermission getRemovePermission() {
				return new RuntimeSecurityPermission(basePermission + "remove");
			}

			public static SecurityPermission getSearchPermission() {
				return new RuntimeSecurityPermission(basePermission + "search");
			}
			
			public static SecurityPermission getGetPermission() {
				return new RuntimeSecurityPermission(basePermission + "get");
			}
			
			public static SecurityPermission getFindPermission() {
				return new RuntimeSecurityPermission(basePermission + "find");
			}
			
		}
		
	}

	public static class PRODUCT_METADATA {
		
		public static final String basePermission = SalesPluginPermissions.basePermission + "product_metadata.";
		
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

		public static class ATTRIBUTE {
			
			public static final String basePermission = SalesPluginPermissions.PRODUCT_METADATA.basePermission + "attribute.";
			
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
			
			public static class OPTIONS {
				
				public static final String basePermission = SalesPluginPermissions.PRODUCT_METADATA.ATTRIBUTE.basePermission + "options.";
				
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

		public static SecurityPermission getCancelPermission() {
			return new RuntimeSecurityPermission(basePermission + "cancel");
		}
		
		public static SecurityPermission getCreatePermission() {
			return new RuntimeSecurityPermission(basePermission + "create");
		}
		
	}

	public static class SHIPPING_REGISTRY {

		public static final String basePermission = SalesPluginPermissions.basePermission + "shipping.";
		
		public static SecurityPermission getRegisterPermission() {
			return new RuntimeSecurityPermission(basePermission + "register");
		}

		public static SecurityPermission getListPermission() {
			return new RuntimeSecurityPermission(basePermission + "list");
		}
		
		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}

		public static SecurityPermission getSearchPermission() {
			return new RuntimeSecurityPermission(basePermission + "search");
		}
		
		public static SecurityPermission getGetPermission() {
			return new RuntimeSecurityPermission(basePermission + "get");
		}
		
		public static SecurityPermission getFindPermission() {
			return new RuntimeSecurityPermission(basePermission + "find");
		}

		public static SecurityPermission getConfirmPermission() {
			return new RuntimeSecurityPermission(basePermission + "confirm");
		}
		
		public static SecurityPermission getCancelPermission() {
			return new RuntimeSecurityPermission(basePermission + "cancel");
		}
		
		public static SecurityPermission getCreatePermission() {
			return new RuntimeSecurityPermission(basePermission + "create");
		}
		
	}
	
	public static class CLIENT_REGISTRY {

		public static final String basePermission = SalesPluginPermissions.basePermission + "client.";
		
		public static SecurityPermission getRegisterPermission() {
			return new RuntimeSecurityPermission(basePermission + "register");
		}

		public static SecurityPermission getSearchPermission() {
			return new RuntimeSecurityPermission(basePermission + "search");
		}

		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}
		
		public static SecurityPermission getFindPermission() {
			return new RuntimeSecurityPermission(basePermission + "find");
		}

		public static class ADDRESS {

			public static final String basePermission = SalesPluginPermissions.basePermission + "address.";
			
			public static SecurityPermission getRegisterPermission() {
				return new RuntimeSecurityPermission(basePermission + "register");
			}

			public static SecurityPermission getFindPermission() {
				return new RuntimeSecurityPermission(basePermission + "find");
			}
			
			public static SecurityPermission getRemovePermission() {
				return new RuntimeSecurityPermission(basePermission + "remove");
			}

			public static SecurityPermission getListPermission() {
				return new RuntimeSecurityPermission(basePermission + "list");
			}
			
		}
		
	}

	public static class PRODUCTVIEWER_REGISTRY {

		public static final String basePermission = SalesPluginPermissions.basePermission + "product_viewer.";
		
		public static class WIDGET {

			public static final String basePermission = PRODUCTVIEWER_REGISTRY.basePermission + "widget.";
			
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
		
	}
	
	public static class ORDERREPORT_REGISTRY {

		public static final String basePermission = SalesPluginPermissions.basePermission + "order_report.";
		
		public static SecurityPermission getRegisterPermission() {
			return new RuntimeSecurityPermission(basePermission + "register");
		}

		public static SecurityPermission getListPermission() {
			return new RuntimeSecurityPermission(basePermission + "list");
		}
		
		public static SecurityPermission getRemovePermission() {
			return new RuntimeSecurityPermission(basePermission + "remove");
		}

		public static SecurityPermission getSearchPermission() {
			return new RuntimeSecurityPermission(basePermission + "search");
		}
		
		public static SecurityPermission getGetPermission() {
			return new RuntimeSecurityPermission(basePermission + "get");
		}
		
		public static SecurityPermission getFindPermission() {
			return new RuntimeSecurityPermission(basePermission + "find");
		}

		public static SecurityPermission getCancelPermission() {
			return new RuntimeSecurityPermission(basePermission + "cancel");
		}
		
		public static SecurityPermission getCreatePermission() {
			return new RuntimeSecurityPermission(basePermission + "create");
		}
		
	}

}
