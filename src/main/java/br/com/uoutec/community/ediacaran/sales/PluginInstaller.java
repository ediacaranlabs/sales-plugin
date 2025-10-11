package br.com.uoutec.community.ediacaran.sales;

import java.math.BigDecimal;

import br.com.uoutec.ediacaran.core.AbstractPlugin;

public class PluginInstaller extends AbstractPlugin {

	public static final BigDecimal ONE_HUMDRED = new BigDecimal(100);

	private SecurityPluginInstaller securityPluginInstaller;
	
	private I18nPluginInstaller i18nPluginInstaller;
	
	private PaymentGatewayPluginInstaller paymentGatewayPluginInstaller;
	
	private CartErrorsPluginInstaller cartErrorsPluginInstaller;
	
	private MenuPluginInstaller menuPluginInstaller;
	
	private EntitiesPluginInstaller entitiesPluginInstaller;
	
	private ShippingMethodPluginInstaller shippingMethodPluginInstaller;
	
	private ActionsPluginInstaller actionsPluginInstaller;
	
	private DriversPluginInstaller driversPluginInstaller;
	
	private ProductViewerPluginInstaller productViewerPluginInstaller;
	
	private ProductTypeInstaller productTypeInstaller;
	
	private ShippingMethodInstaller shippingMethodInstaller;
	
	private SimplePaymentGatewayInstaller simplePaymentGatewayInstaller;
	
	private PortalEntitiesPluginInstaller portalEntitiesPluginInstaller;
	
	public PluginInstaller() {
		this.portalEntitiesPluginInstaller = new PortalEntitiesPluginInstaller();
		this.securityPluginInstaller = new SecurityPluginInstaller();
		this.i18nPluginInstaller = new I18nPluginInstaller();
		this.paymentGatewayPluginInstaller = new PaymentGatewayPluginInstaller();
		this.cartErrorsPluginInstaller = new CartErrorsPluginInstaller();
		this.menuPluginInstaller = new MenuPluginInstaller();
		this.entitiesPluginInstaller = new EntitiesPluginInstaller(getPackagesNames());
		this.shippingMethodPluginInstaller = new ShippingMethodPluginInstaller();
		this.productTypeInstaller = new ProductTypeInstaller();
		this.actionsPluginInstaller = new ActionsPluginInstaller();
		this.driversPluginInstaller = new DriversPluginInstaller();
		this.productViewerPluginInstaller = new ProductViewerPluginInstaller();
		this.shippingMethodInstaller = new ShippingMethodInstaller();
		this.simplePaymentGatewayInstaller = new SimplePaymentGatewayInstaller();
	}
	
	@Override
	public void install() throws Throwable {
		portalEntitiesPluginInstaller.install();
		securityPluginInstaller.install();
		i18nPluginInstaller.install();
		paymentGatewayPluginInstaller.install();
		cartErrorsPluginInstaller.install();
		menuPluginInstaller.install();
		entitiesPluginInstaller.install();
		shippingMethodPluginInstaller.install();
		productTypeInstaller.install();
		actionsPluginInstaller.install();
		driversPluginInstaller.install();
		productViewerPluginInstaller.install();
		shippingMethodInstaller.install();
		simplePaymentGatewayInstaller.install();
	}

	@Override
	public void uninstall() throws Throwable {
		portalEntitiesPluginInstaller.uninstall();
		simplePaymentGatewayInstaller.uninstall();
		shippingMethodInstaller.uninstall();
		productViewerPluginInstaller.uninstall();
		securityPluginInstaller.uninstall();
		i18nPluginInstaller.uninstall();
		paymentGatewayPluginInstaller.uninstall();
		cartErrorsPluginInstaller.uninstall();
		menuPluginInstaller.uninstall();
		entitiesPluginInstaller.uninstall();
		shippingMethodPluginInstaller.uninstall();
		actionsPluginInstaller.uninstall();
		driversPluginInstaller.uninstall();
		productTypeInstaller.uninstall();
	}
	
}
