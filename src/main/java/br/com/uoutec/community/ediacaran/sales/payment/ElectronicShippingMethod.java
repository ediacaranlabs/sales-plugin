package br.com.uoutec.community.ediacaran.sales.payment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethod;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingOption;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingRateRequest;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ElectronicShippingMethod implements ShippingMethod{

	@Override
	public String getId() {
		return "electronic";
	}

	@Override
	public String getName() {
		return "Electronic delivery";
	}

	@Override
	public boolean isApplicable(ShippingRateRequest request) {
		try {
			ProductTypeRegistry productTypeRegistry = EntityContextPlugin.getEntity(ProductTypeRegistry.class);
			
			for(ProductRequest pp: request.getItens()) {
				
				ProductType productType = productTypeRegistry.getProductType(pp.getProduct().getProductType());
				if(!productType.getHandler().isService(pp)) {
					return false;
				}
				
			}
			
		}
		catch(Throwable ex) {
			return false;
		}
		
		return !request.getItens().isEmpty();
	}

	@Override
	public List<ShippingOption> getOptions(ShippingRateRequest request) {
		return Arrays.asList(new ShippingOption("electronic", getId(), "Electronic delivery", request.getItens().get(0).getCurrency(), BigDecimal.ZERO, BigDecimal.ZERO));
	}

	@Override
	public String getView(Shipping value) {
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		return varParser.getValue("${plugins.ediacaran.sales.web_path}:/default_template/admin/shipping/electronic_shipping.jsp");
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
