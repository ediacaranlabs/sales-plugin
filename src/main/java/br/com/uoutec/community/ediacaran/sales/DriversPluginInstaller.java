package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.system.repository.FileManager;
import br.com.uoutec.community.ediacaran.system.repository.FileObjectManagerHandler;
import br.com.uoutec.community.ediacaran.system.repository.FileObjectsManagerDriver;
import br.com.uoutec.community.ediacaran.system.repository.ObjectHandlerImp;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsManagerDriver;
import br.com.uoutec.community.ediacaran.system.repository.ObjectsTemplateManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.web.WebUtil;

public class DriversPluginInstaller {

	private ObjectsManagerDriver productsDriver;
	
	public void install() throws Throwable {

		this.productsDriver = 
				new FileObjectsManagerDriver(
						new FileManager(
								WebUtil.getPublicPath().getPath("/products"),
								new FileObjectManagerHandler("png")
						), SalesPluginConstants.PRODUCTS_DRIVER_NAME
				); 
	
		this.productsDriver.setDefaultObjectHandler(new ObjectHandlerImp());
		
		ObjectsTemplateManager objectsManager = EntityContextPlugin.getEntity(ObjectsTemplateManager.class);
		
		objectsManager.registerDriver(this.productsDriver);
	}

	public void uninstall() throws Throwable {
		
		ObjectsTemplateManager objectsManager = EntityContextPlugin.getEntity(ObjectsTemplateManager.class);
		
		objectsManager.unregisterDriver(this.productsDriver);
	}	
}
