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
		
		Menu menu = menubar.getMenu("account");
		
		if(menu == null) {
			return;
		}
		
		menu.addItem("orders")
			.setRole(BasicRoles.CLIENT)
			.setIcon("circle")
			.setName("Orders")
			.setResourceBundle(AdminMenuListenerMessages.RESOURCE_BUNDLE)
			.setTemplate(AdminMenuListenerMessages.installMenu.itens.order)
			.setResource("#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders");

		
	}
	
}