package br.com.uoutec.community.ediacaran.sales;

import java.math.BigDecimal;

import br.com.uoutec.ediacaran.core.AbstractPlugin;

public class PluginInstaller extends AbstractPlugin {

	public static final BigDecimal ONE_HUMDRED = new BigDecimal(100);

	private SecurityPluginInstaller securityPluginInstaller;
	
	private I18nPluginInstaller i18nPluginInstaller;
	
	private PaymentGatewayPluginInstaller paymentGatewayPluginInstaller;
	
	public PluginInstaller() {
		this.securityPluginInstaller = new SecurityPluginInstaller();
		this.i18nPluginInstaller = new I18nPluginInstaller();
		this.paymentGatewayPluginInstaller = new PaymentGatewayPluginInstaller();
	}
	
	@Override
	public void install() throws Throwable {
		securityPluginInstaller.install();
		i18nPluginInstaller.install();
		paymentGatewayPluginInstaller.install();
	}

	@Override
	public void uninstall() throws Throwable {
		securityPluginInstaller.uninstall();
		i18nPluginInstaller.uninstall();
		paymentGatewayPluginInstaller.uninstall();
	}
	
}
