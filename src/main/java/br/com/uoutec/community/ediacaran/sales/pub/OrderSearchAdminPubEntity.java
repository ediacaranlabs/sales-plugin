package br.com.uoutec.community.ediacaran.sales.pub;

import br.com.uoutec.community.ediacaran.security.AuthenticationRequiredException;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class OrderSearchAdminPubEntity extends OrderSearchPubEntity {

	private static final long serialVersionUID = 7674988526885634067L;

	@Override
	protected void copyTo(OrderSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		super.copyTo(o, reload, override, validate);
		
		SubjectProvider subjectProvider = EntityContextPlugin.getEntity(SubjectProvider.class); 
		Subject subject = subjectProvider.getSubject();
		
		if(!subject.isAuthenticated()) {
			throw new AuthenticationRequiredException();
		}
		
		SystemUserRegistry registry = EntityContextPlugin.getEntity(SystemUserRegistry.class);
		SystemUser user = registry.getBySystemID(subject.getPrincipal().getUserPrincipal().getName());
		
		if(user == null) {
			throw new AuthenticationRequiredException();
		}
		
		o.setOwner(user.getId());
		o.setOwnerName(null);
	}
	
}
