package br.com.uoutec.community.ediacaran.sales.pub.entity;

import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.security.AuthenticationRequiredException;
import br.com.uoutec.community.ediacaran.user.SystemUserIDProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class InvoiceSearchPanelPubEntity extends InvoiceSearchPubEntity {

	private static final long serialVersionUID = 7674988526885634067L;

	@Override
	protected void copyTo(InvoiceSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		super.copyTo(o, reload, override, validate);
		
		SystemUserRegistry registry = EntityContextPlugin.getEntity(SystemUserRegistry.class);
		SystemUser user = registry.getBySystemID(SystemUserIDProvider.getSystemUserID());
		
		if(user == null) {
			throw new AuthenticationRequiredException();
		}
		
		o.setCanceled(false);
		o.setOwner(user.getId());
		o.setOwnerName(null);
	}
	
}
