package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.front.security.pub.AuthenticationMethodBuilder;
import br.com.uoutec.community.ediacaran.front.security.pub.WebSecurityManagerPlugin;
import br.com.uoutec.community.ediacaran.security.AuthorizationManager;
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
			.addConstraint(varParser.getValue("${plugins.ediacaran.front.panel_context}/*"))
				.addRole(BasicRoles.MANAGER)
				.addRole(BasicRoles.USER)
				.addRole(BasicRoles.CLIENT)
			.addConstraint(varParser.getValue("/templates/default_template/front${plugins.ediacaran.front.panel_context}/*"))
				.addRole(BasicRoles.MANAGER)
				.addRole(BasicRoles.USER)
				.addRole(BasicRoles.CLIENT)
			.form()
				.setOption(AuthenticationMethodBuilder.LOGIN_PAGE, "/login")
				.setOption(AuthenticationMethodBuilder.ERROR_PAGE, "/login?error=true");

		AuthorizationManager am = EntityContextPlugin.getEntity(AuthorizationManager.class);
		
		am.registerAuthorization(SalesUserPermissions.ALL,					"Sales - Manager",				"Sales Manager", 		null);
		
		am.registerAuthorization(SalesUserPermissions.ORDER.ALL,			"Order - Manager",				"Order Manager", 		null);
		am.registerAuthorization(SalesUserPermissions.ORDER.SHOW,			"Order - Show",					"Show order", 			null);
		am.registerAuthorization(SalesUserPermissions.ORDER.CREATE,			"Order - Create",				"Create order", 		null);
		am.registerAuthorization(SalesUserPermissions.ORDER.EDIT,			"Order - Edit",					"Edit order", 			null);
		am.registerAuthorization(SalesUserPermissions.ORDER.SAVE,			"Order - Save",					"Save order", 			null);
		am.registerAuthorization(SalesUserPermissions.ORDER.DELETE,			"Order - Delete",				"Delete order",			null);
		am.registerAuthorization(SalesUserPermissions.ORDER.SEARCH,			"Order - Search",				"Search order", 		null);
		am.registerAuthorization(SalesUserPermissions.ORDER.FIELDS.ALL,		"Order - fields",				"Order fields", 		null);
		am.registerAuthorization(SalesUserPermissions.ORDER.FIELDS.ID,		"Order - Edit id field",		"Id order field", 		null);
		am.registerAuthorization(SalesUserPermissions.ORDER.FIELDS.DATE,	"Order - Edit date field",		"Date order field",		null);
		am.registerAuthorization(SalesUserPermissions.ORDER.FIELDS.ITENS,	"Order - Edit itens field",		"Itens order field", 	null);
		am.registerAuthorization(SalesUserPermissions.ORDER.FIELDS.PAYMENT,	"Order - Edit payment field",	"Payment order field", 	null);
		am.registerAuthorization(SalesUserPermissions.ORDER.FIELDS.STATUS,	"Order - Edit order field",		"Status order field", 	null);

		am.registerAuthorization(SalesUserPermissions.INVOICE.ALL,			"Invoice - Manager",			"Invoice Manager", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.SHOW,			"Invoice - Show",				"Show invoice", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.CREATE,		"Invoice - Create",				"Create invoice", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.EDIT,			"Invoice - Edit",				"Edit invoice", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.SAVE,			"Invoice - Save",				"Save invoice", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.DELETE,		"Invoice - Delete",				"Delete invoice",		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.SEARCH,		"Invoice - Search",				"Search invoice", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.CANCEL,		"Invoice - Cancel",				"Cancel invoice", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.FIELDS.ALL,	"Invoice - fields",				"Invoice fields", 		null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.FIELDS.ID,	"Invoice - Edit id field",		"Id invoice field", 	null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.FIELDS.ORDER,	"Invoice - Edit order field",	"Order invoice field",	null);
		am.registerAuthorization(SalesUserPermissions.INVOICE.FIELDS.ITENS,	"Invoice - Edit itens field",	"Itens invoice field", 	null);
		
	}
	
	public void uninstall() throws Throwable {
		
		AuthorizationManager am = EntityContextPlugin.getEntity(AuthorizationManager.class);
		
		am.unregisterAuthorization(SalesUserPermissions.ALL);
		
		am.unregisterAuthorization(SalesUserPermissions.ORDER.ALL);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.SHOW);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.CREATE);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.EDIT);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.SAVE);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.DELETE);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.SEARCH);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.FIELDS.ALL);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.FIELDS.ID);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.FIELDS.DATE);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.FIELDS.ITENS);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.FIELDS.PAYMENT);
		am.unregisterAuthorization(SalesUserPermissions.ORDER.FIELDS.STATUS);

		am.unregisterAuthorization(SalesUserPermissions.INVOICE.ALL);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.SHOW);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.CREATE);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.EDIT);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.SAVE);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.DELETE);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.SEARCH);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.CANCEL);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.FIELDS.ALL);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.FIELDS.ID);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.FIELDS.ORDER);
		am.unregisterAuthorization(SalesUserPermissions.INVOICE.FIELDS.ITENS);

	}
	
	
}
