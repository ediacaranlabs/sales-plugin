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
	
}
