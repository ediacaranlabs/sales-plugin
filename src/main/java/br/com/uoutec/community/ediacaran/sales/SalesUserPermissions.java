package br.com.uoutec.community.ediacaran.sales;

public final class SalesUserPermissions {

	public static final String ALL 		= "SALES";
	
	public static final class ORDER {

		public static final String ALL 		= SalesUserPermissions.ALL +  ":ORDER";

		public static final String CREATE	= ALL + ":CREATE";
		
		public static final String SHOW		= ALL + ":SHOW";

		public static final String SEARCH	= ALL + ":SEARCH";

		public static final String EDIT		= ALL + ":EDIT";

		public static final String SAVE		= ALL + ":SAVE";

		public static final String PAYMENT	= ALL + ":PAYMENT";
		
		public static final String DELETE	= ALL + ":DELETE";
		
		public static final class FIELDS {

			public static final String ALL 		= SalesUserPermissions.ORDER.ALL +  ":FIELDS";

			public static final String ID 		= ALL +  ":ID";

			public static final String DATE		= ALL +  ":DATE";
			
			public static final String STATUS	= ALL +  ":STATUS";
			
			public static final String PAYMENT	= ALL +  ":PAYMENT";

			public static final String ITENS	= ALL +  ":ITENS";
			
		}
		
	}
	
	public static final class INVOICE {

		public static final String ALL 		= SalesUserPermissions.ALL + ":INVOICE";
		
		public static final String CREATE	= ALL + ":CREATE";
		
		public static final String SHOW		= ALL + ":SHOW";

		public static final String SEARCH	= ALL + ":SEARCH";

		public static final String EDIT		= ALL + ":EDIT";

		public static final String CANCEL	= ALL + ":CANCEL";

		public static final String SAVE		= ALL + ":SAVE";

		public static final String DELETE	= ALL + ":DELETE";
		
		public static final class FIELDS {

			public static final String ALL 		= SalesUserPermissions.INVOICE.ALL +  ":FIELDS";

			public static final String ID 		= ALL +  ":ID";

			public static final String ORDER	= ALL +  ":ORDER";
			
			public static final String ITENS	= ALL +  ":ITENS";
			
		}
		
	}

	public static final class SHIPPING {

		public static final String ALL 		= SalesUserPermissions.ALL + ":SHIPPING";
		
		public static final String CREATE	= ALL + ":CREATE";
		
		public static final String SHOW		= ALL + ":SHOW";

		public static final String SEARCH	= ALL + ":SEARCH";

		public static final String EDIT		= ALL + ":EDIT";

		public static final String CANCEL	= ALL + ":CANCEL";

		public static final String SAVE		= ALL + ":SAVE";

		public static final String DELETE	= ALL + ":DELETE";
		
		public static final class FIELDS {

			public static final String ALL 		= SalesUserPermissions.SHIPPING.ALL +  ":FIELDS";

			public static final String ID 		= ALL +  ":ID";

			public static final String ORDER	= ALL +  ":ORDER";
			
			public static final String ITENS	= ALL +  ":ITENS";
			
		}
		
	}

	public static final class PRODUCT {

		public static final String ALL 		= SalesUserPermissions.ALL + ":PRODUCT";
		
		public static final String CREATE	= ALL + ":CREATE";
		
		public static final String SHOW		= ALL + ":SHOW";

		public static final String SEARCH	= ALL + ":SEARCH";

		public static final String EDIT		= ALL + ":EDIT";

		public static final String SAVE		= ALL + ":SAVE";

		public static final String DELETE	= ALL + ":DELETE";
		
	}
	
	public static final class CLIENT {

		public static final String ALL 		= SalesUserPermissions.ALL + ":CLIENT";
		
		public static final String CREATE	= ALL + ":NEW";
		
		public static final String SHOW		= ALL + ":SHOW";

		public static final String SEARCH	= ALL + ":SEARCH";

		public static final String EDIT		= ALL + ":EDIT";

		public static final String SAVE		= ALL + ":SAVE";

		public static final String DELETE	= ALL + ":DELETE";
		
		public static final class FIELDS {

			public static final String ALL 					= SalesUserPermissions.CLIENT.ALL +  ":FIELDS";

			public static final String ID 					= ALL + ":ID";

			public static final String FIRST_NAME 			= ALL + ":FIRST_NAME";

			public static final String LAST_NAME 			= ALL + ":LAST_NAME";
			
			public static final String EMAIL	 			= ALL + ":EMAIL";
			
			public static final String ORGANIZATION			= ALL + ":ORGANIZATION";
			
			public static final String COUNTRY				= ALL + ":COUNTRY";
			
			public static final String ADDRESS_LINE1		= ALL + ":ADDRESS_LINE2";
			
			public static final String ADDRESS_LINE2		= ALL + ":ADDRESS_LINE2";
			
			public static final String CITY					= ALL + ":CITY";

			public static final String REGION				= ALL + ":REGION";
			
			public static final String ZIP					= ALL + ":ZIP";

			public static final String ACTIVED				= ALL + ":ACTIVED";
			
			public static final String PHONE				= ALL + ":PHONE";
			
			public static final String DOCUMENT				= ALL + ":DOCUMENT";

			public static final String SHIPPING_ADDRESSES	= ALL + ":SHIPPING_ADDRESSES";

			public static final String BILLING_ADDRESS		= ALL + ":BILLING_ADDRESS";
			
		}
		
	}
	
}
