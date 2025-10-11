package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.service.concurrent.ProductExecutorServices;
import br.com.uoutec.community.ediacaran.system.concurrent.ThreadGroupManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class PortalEntitiesPluginInstaller {

	private ThreadGroupManager threadGroupManager;
	
	private ProductExecutorServices productExecutorServices;
	
	public PortalEntitiesPluginInstaller() {
	}
	
	public void install() throws Throwable {
		this.productExecutorServices = EntityContextPlugin.getEntity(ProductExecutorServices.class);
		this.threadGroupManager = EntityContextPlugin.getEntity(ThreadGroupManager.class);
		threadGroupManager.getThreadGroup("default").execute(productExecutorServices);
	}
	
	public void uninstall() throws Throwable {
		productExecutorServices.setActive(false);
		this.productExecutorServices = null;
		this.threadGroupManager = null;
	}
	
}
