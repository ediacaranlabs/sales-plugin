package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.front.pub.Menu;
import br.com.uoutec.community.ediacaran.front.pub.MenuBar;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.system.repository.ObjectMetadata;
import br.com.uoutec.community.ediacaran.system.repository.ObjectValue;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsManagerDriver.ObjectsManagerDriverListener;
import br.com.uoutec.community.ediacaran.system.repository.PathMetadata;

public class AdminMenuListener  
	implements ObjectsManagerDriverListener {

	private static final String ADMIN_MENU_BAR_PATH     = "/admin";
	
	private static final String ADMIN_MENU_BAR          = "adminmenubar";
	
	@SuppressWarnings("unused")
	private static final String ADMIN_TOP_MENU_BAR      = "admintopmenubar";
	
	public void afterLoad(ObjectMetadata omd, ObjectValue obj) {
		
		if(obj == null) {
			return;
		}

		PathMetadata pmd = omd.getPathMetadata();
		
		if(obj.getObject() instanceof MenuBar && pmd.getPath().equals(ADMIN_MENU_BAR_PATH)) {
			
			if(pmd.getId().equals(ADMIN_MENU_BAR)) {
				installMenu((MenuBar)obj.getObject());
			}
			
		}
		
	}
	
	private void installMenu(MenuBar menubar) {
		
		Menu accountMenu = menubar.getMenu("account");
		
		if(accountMenu != null) {
			accountMenu.addItem("orders")
				.setIcon("circle")
				.setName("Orders")
				.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
				.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.account_menu.itens.orders)
				.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders")
			.getParent()
				.addItem("invoices")
				.setIcon("circle")
				.setName("Invoices")
				.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
				.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.account_menu.itens.invoices)
				.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/invoices");
		}
		

		Menu salesMenu = menubar.getMenu("sales");

		if(salesMenu == null) {
			menubar.addMenu("sales")
				.setIcon("circle")
				.setName("Sales")
				.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
				.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.name)
				.setRole(BasicRoles.USER)
				.setPermission(SalesUserPermissions.ORDER.ALL)
				.addItem("orders")
					.setIcon("circle")
					.setName("Orders")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.orders)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders")
					.setRole(BasicRoles.USER)
					.setPermission(SalesUserPermissions.ORDER.SHOW)
				.getParent()
					.addItem("invoices")
					.setIcon("circle")
					.setName("Invoices")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.invoices)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/invoices")
					.setRole(BasicRoles.USER)
					.setPermission(SalesUserPermissions.ORDER.SHOW)
				.getParent()
					.addItem("clients")
					.setIcon("circle")
					.setName("Clients")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.clients)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients")
					.setRole(BasicRoles.USER)
					.setPermission(SalesUserPermissions.CLIENT.SHOW);
		}
		
	}
	
}