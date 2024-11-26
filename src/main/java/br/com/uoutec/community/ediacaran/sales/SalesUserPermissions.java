package br.com.uoutec.community.ediacaran.sales;

public final class SalesUserPermissions {

	public static final String ALL 		= "SALES";
	
	public static final class ORDER {

		public static final String ALL 		= "ORDER";

		public static final String SHOW		= ALL + ":SHOW";

		public static final String SEARCH	= ALL + ":SEARCH";

		public static final String EDIT		= ALL + ":EDIT";
		
	}
}
