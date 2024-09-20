package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.front.objects.MenubarObjectsManagerDriver;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsManagerDriver;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class MenuPluginInstaller {

	private AdminMenuListener adminMenuListener;
	
	public MenuPluginInstaller() {
		this.adminMenuListener = new AdminMenuListener();
	}
	
	public void install() throws Throwable {
		ObjectsTemplateManager objectsManager = EntityContextPlugin.getEntity(ObjectsTemplateManager.class);
		ObjectsManagerDriver menubarDriver = objectsManager.getDriver(MenubarObjectsManagerDriver.DRIVER_NAME);
		
		menubarDriver.addListener(adminMenuListener);
	}
	
	public void uninstall() throws Throwable {
		ObjectsTemplateManager objectsManager = EntityContextPlugin.getEntity(ObjectsTemplateManager.class);
		ObjectsManagerDriver menubarDriver = objectsManager.getDriver(MenubarObjectsManagerDriver.DRIVER_NAME);
		
		menubarDriver.removeListener(adminMenuListener);
	}
	
}
