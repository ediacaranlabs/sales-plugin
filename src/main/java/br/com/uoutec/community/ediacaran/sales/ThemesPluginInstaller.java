package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.front.theme.PluginThemesManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ThemesPluginInstaller {

	public ThemesPluginInstaller() {
	}
	
	public void install() throws Throwable {
		PluginThemesManager ptm = EntityContextPlugin.getEntity(PluginThemesManager.class);
		ptm.registerThemes();
	}
	
	public void uninstall() throws Throwable {
		PluginThemesManager ptm = EntityContextPlugin.getEntity(PluginThemesManager.class);
		ptm.unregisterThemes();
	}
	
}
