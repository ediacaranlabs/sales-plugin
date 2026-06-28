package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.front.pub.widget.Widget;
import br.com.uoutec.community.ediacaran.front.pub.widget.Widgets;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class WidgetsPluginInstaller {

	public WidgetsPluginInstaller() {
	}
	
	public void install() throws Throwable {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		Widgets wgs = EntityContextPlugin.getEntity(Widgets.class);
		
		wgs.addWidget(
			Widget.builder()
				.withName("pendingConfirmationShipping")
				.withResource(varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/shippings/widgets/pending"))
			.build()
		);
		wgs.addWidget(
				Widget.builder()
					.withName("pendingConfirmationPayment")
					.withResource(varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/widgets/pending-payment"))
				.build()
			);
		wgs.addWidget(
				Widget.builder()
					.withName("activeOrderReport")
					.withResource(varParser.getValue("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/report/widgets/active"))
				.build()
			);
	}
	
	public void uninstall() throws Throwable {
		Widgets wgs = EntityContextPlugin.getEntity(Widgets.class);
		wgs.removeWidget("pendingConfirmationShipping");
		wgs.removeWidget("pendingConfirmationPayment");
		wgs.removeWidget("activeOrderReport");
	}
	
}
