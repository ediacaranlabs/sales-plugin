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
				.setRole(BasicRoles.USER, BasicRoles.MANAGER)
				.setPermission(SalesUserPermissions.ORDER.ALL)
				.addItem("orders")
					.setIcon("circle")
					.setName("Orders")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.orders)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders")
					.setRole(BasicRoles.USER, BasicRoles.MANAGER)
					.setPermission(SalesUserPermissions.ORDER.SHOW)
				.getParent()
					.addItem("order_report")
					.setIcon("circle")
					.setName("Order report")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.order_report)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/report")
					.setRole(BasicRoles.USER, BasicRoles.MANAGER)
					.setPermission(SalesUserPermissions.ORDER.SHOW)
				.getParent()
					.addItem("invoices")
					.setIcon("circle")
					.setName("Invoices")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.invoices)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/invoices")
					.setRole(BasicRoles.USER, BasicRoles.MANAGER)
					.setPermission(SalesUserPermissions.ORDER.SHOW)
				.getParent()
					.addItem("shippings")
					.setIcon("circle")
					.setName("Shippings")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.shippings)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings")
					.setRole(BasicRoles.USER, BasicRoles.MANAGER)
					.setPermission(SalesUserPermissions.SHIPPING.SHOW)
				.getParent()
					.addItem("clients")
					.setIcon("circle")
					.setName("Clients")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.clients)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients")
					.setRole(BasicRoles.USER, BasicRoles.MANAGER)
					.setPermission(SalesUserPermissions.CLIENT.SHOW)
				.getParent()
					.addItem("products")
					.setIcon("circle")
					.setName("Products")
					.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
					.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.products)
					.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products")
					.setRole(BasicRoles.USER, BasicRoles.MANAGER)
					.setPermission(SalesUserPermissions.PRODUCT.SHOW)
			.getParent()
				.addItem("productMetadata")
				.setIcon("circle")
				.setName("productMetadata")
				.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
				.setTemplate(AdminMenuListenerMessages.installMenu.admin_menu.sales_menu.itens.product_metadata)
				.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/product-metadata")
				.setRole(BasicRoles.USER, BasicRoles.MANAGER)
				.setPermission(SalesUserPermissions.PRODUCT_METADATA.SHOW);
		}
		
	}
	
}