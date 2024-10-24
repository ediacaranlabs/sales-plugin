package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.system.i18n.Plugini18nManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class I18nPluginInstaller {

	public void install() throws Throwable{
		Plugini18nManager pi18n = EntityContextPlugin.getEntity(Plugini18nManager.class);
		pi18n.registerLanguages();
	}
	
	public void uninstall() throws Throwable{
		Plugini18nManager pi18n = EntityContextPlugin.getEntity(Plugini18nManager.class);
		pi18n.unregisterLanguages();
	}
	
}
