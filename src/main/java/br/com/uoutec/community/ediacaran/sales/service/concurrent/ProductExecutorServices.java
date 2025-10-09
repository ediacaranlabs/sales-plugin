package br.com.uoutec.community.ediacaran.sales.service.concurrent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.uoutec.community.ediacaran.system.concurrent.ThreadGroupManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PluginType;

public class ProductExecutorServices 
	implements Runnable {

	@Inject
	private SelectProductCategoryPortalExecutorService selectProductCategoryPortalExecutorService;
	
	@Inject
	private SelectProductOfferPortalExecutorService selectProductOfferPortalExecutorService;
	
	@Inject
	private SelectProductPortalExecutorService selectProductPortalExecutorService;
	
	@Inject
	private ThreadGroupManager threadGroupManager;
	
	private volatile boolean active;
	
	public ProductExecutorServices() {
	}
	
	@Override
	public void run() {
		
		active = true;
		
		while(active) {
			
			try {
				
				if(checkActive()) {
					
					if(!selectProductCategoryPortalExecutorService.isActive()) {
						selectProductCategoryPortalExecutorService.setActive(true);
						threadGroupManager.getThreadGroup("default").execute(selectProductCategoryPortalExecutorService);
					}

					if(!selectProductOfferPortalExecutorService.isActive()) {
						selectProductOfferPortalExecutorService.setActive(true);
						threadGroupManager.getThreadGroup("default").execute(selectProductOfferPortalExecutorService);
					}

					if(!selectProductPortalExecutorService.isActive()) {
						selectProductPortalExecutorService.setActive(true);
						threadGroupManager.getThreadGroup("default").execute(selectProductPortalExecutorService);
					}
					
				}
				
				Thread.sleep(getSleepTime());
			}
			catch(Throwable ex) {
				active = false;
			}
			
		}
		
	}

	public void setActive(boolean value) {
		this.active = value;
	}
	
	public boolean isActive() {
		return active;
	}
	
	private boolean checkActive() {
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			return pluginType.getConfiguration().getBoolean("portal_service");
		}
		catch(Throwable ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private long getSleepTime() {
		try {
			PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
			int secs = pluginType.getConfiguration().getInt("portal_service_sleep_time");
			return TimeUnit.SECONDS.toMillis(secs);
		}
		catch(Throwable ex) {
			ex.printStackTrace();
			return TimeUnit.SECONDS.toMillis(60);
		}
	}
	
}
