package br.com.uoutec.community.ediacaran.sales.shipping.flatrate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethod;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingOption;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingOptionGroup;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingRateRequest;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.ediacaran.core.plugins.PluginType;
import br.com.uoutec.i18n.MessageBundle;

public class FlatRateShippingMethod implements ShippingMethod{

	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(FlatRateShippingMethod.class);
	
	@Override
	public String getId() {
		return "flatrate";
	}

	@Override
	public String getName() {
		return "Flat Rate";
	}

	@Override
	public boolean isApplicable(ShippingRateRequest request) {
		try {
			ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
			
			for(ProductRequest pp: request.getItens()) {
				
				ProductType productType = productTypeRegistry.getProductType(pp.getProduct().getProductType());
				if(productType.getHandler().isSupportShipping(pp)) {
					return true;
				}
				
			}
			
		}
		catch(Throwable ex) {
			return false;
		}
		
		return false;	
	}

	@Override
	public List<ShippingOption> getOptions(ShippingRateRequest request) {
		
		PluginType pluginType = EntityContextPlugin.getEntity(PluginType.class);
		List<ShippingOption> result = new ArrayList<>();
		boolean flatRateActive = pluginType.getConfiguration().getBoolean("flat_rate_active");
		
		if(!flatRateActive) {
			return result;
		}
		
		String flatRate = pluginType.getConfiguration().getString("flat_rate");
		String flatRateCurrency = pluginType.getConfiguration().getString("flat_rate_currency");
		String flatRateName = pluginType.getConfiguration().getString("flat_rate_name");
		
		
		ShippingOptionGroup sgo = new ShippingOptionGroup("flatrate", "flatrate", flatRateName, flatRateCurrency, result);
		
		for(ProductRequest pr: request.getItens()) {
			BigDecimal v = new BigDecimal(flatRate);
			v = v.multiply(new BigDecimal(pr.getUnits()));
			result.add(new ShippingOption("flatrate", "flatrate", flatRateName, pr.getProduct().getId(), flatRateCurrency, v, v));
		}

		return Arrays.asList(sgo);
	}

	@Override
	public String getView(Shipping value) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		return varParser.getValue("${plugins.ediacaran.sales.web_path}:/default_template/admin/shipping/basic_shipping_type.jsp");
	}

	@Override
	public String getOwnerView(Shipping value) {
		return null;
	}

	@Override
	public String getTrackerAddress(Shipping value) {
		return null;
	}

}
