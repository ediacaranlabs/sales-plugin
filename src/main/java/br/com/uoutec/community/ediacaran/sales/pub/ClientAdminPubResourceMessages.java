package br.com.uoutec.community.ediacaran.sales.pub;

import br.com.uoutec.i18n.MessageBundle;

public class ClientAdminPubResourceMessages {

	public static final String RESOURCE_BUNDLE = 
			MessageBundle
			.toPackageID(ClientAdminPubResource.class);
	
	public final class index{
		
		public final class error{
			
			public static final String fail_load = "index.error.fail_load";
			
		}
		
	}
	
	public final class search {
		
		public final class error {
	
			public static final String fail_load_entity = "search.error.fail_load_entity";
			
		}
		
	}

	public final class new_invoice{
		
		public final class error{
			
			public static final String fail_load_entity = "new_invoice.error.fail_load_entity";

			public static final String create_invoice = "new_invoice.error.create_invoice";
			
		}
		
	}

	public final class details{
		
		public final class error{
			
			public static final String fail_load_entity = "details.error.fail_load_entity";
			
		}
		
	}
	
	public final class edit {
		
		public final class error{
			
			public static final String fail_load = "edit.error.failed_load";
			
		}
		
	}

	public final class save{
		
		public final class error{
			
			public static final String register = "save.error.register";
			
		}
		
	}

	public final class cancel{
		
		public final class error{
			
			public static final String fail_load_entity = "cancel.error.fail_load_entity";
			
			public static final String register = "cancel.error.register";
			
		}
		
	}
	
}
