package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.front.security.pub.AuthenticationMethodBuilder;
import br.com.uoutec.community.ediacaran.front.security.pub.WebSecurityManagerPlugin;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class SecurityPluginInstaller {

	public void install() throws Throwable {
		
		//AuthorizationManager am = EntityContextPlugin.getEntity(AuthorizationManager.class);
		
		WebSecurityManagerPlugin webSecurityManagerPlugin = 
				EntityContextPlugin.getEntity(WebSecurityManagerPlugin.class);
		
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		
		webSecurityManagerPlugin
			.addConstraint(varParser.getValue("${plugins.ediacaran.front.manager_context}/*"))
				.addRole(BasicRoles.MANAGER)
			.addConstraint(varParser.getValue("/templates/default_template${plugins.ediacaran.front.manager_context}/*"))
				.addRole(BasicRoles.MANAGER)
			.addConstraint(varParser.getValue("${plugins.ediacaran.front.admin_context}/*"))
				.addRole(BasicRoles.MANAGER)
				.addRole(BasicRoles.USER)
			.addConstraint(varParser.getValue("/templates/default_template${plugins.ediacaran.front.admin_context}/*"))
				.addRole(BasicRoles.MANAGER)
				.addRole(BasicRoles.USER)
			.addConstraint(varParser.getValue("${plugins.ediacaran.marketplace.panel_context}/*"))
				.addRole(BasicRoles.MANAGER)
				.addRole(BasicRoles.USER)
				.addRole(BasicRoles.CLIENT)
			.addConstraint(varParser.getValue("/templates/default_template/front${plugins.ediacaran.marketplace.panel_context}/*"))
				.addRole(BasicRoles.MANAGER)
				.addRole(BasicRoles.USER)
				.addRole(BasicRoles.CLIENT)
			.form()
				.setOption(AuthenticationMethodBuilder.LOGIN_PAGE, "/login")
				.setOption(AuthenticationMethodBuilder.ERROR_PAGE, "/login?error=true");

	}
	
	public void uninstall() throws Throwable {
		
		//AuthorizationManager am = EntityContextPlugin.getEntity(AuthorizationManager.class);

	}
	
	
}
