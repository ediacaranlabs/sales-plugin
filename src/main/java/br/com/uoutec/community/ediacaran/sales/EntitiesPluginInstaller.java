package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceLoader;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class EntitiesPluginInstaller {

	private String[] packages;
	
	public EntitiesPluginInstaller(String[] packages) {
		this.packages = packages;
	}
	
	public void install() throws Throwable {
		EntityInheritanceLoader loader = EntityContextPlugin.getEntity(EntityInheritanceLoader.class);
		loader.loadEntities(packages);
	}
	
	public void uninstall() throws Throwable {
		EntityInheritanceLoader loader = EntityContextPlugin.getEntity(EntityInheritanceLoader.class);
		loader.removeEntities(packages);
	}
	
}
