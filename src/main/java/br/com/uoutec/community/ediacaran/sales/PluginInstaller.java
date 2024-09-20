package br.com.uoutec.community.ediacaran.sales;

import java.math.BigDecimal;

import br.com.uoutec.ediacaran.core.AbstractPlugin;

public class PluginInstaller extends AbstractPlugin {

	public static final BigDecimal ONE_HUMDRED = new BigDecimal(100);

	private SecurityPluginInstaller securityPluginInstaller;
	
	public PluginInstaller() {
		this.securityPluginInstaller = new SecurityPluginInstaller();
	}
	
	@Override
	public void install() throws Throwable {
		securityPluginInstaller.install();
	}

	@Override
	public void uninstall() throws Throwable {
		securityPluginInstaller.uninstall();
	}
	
}
