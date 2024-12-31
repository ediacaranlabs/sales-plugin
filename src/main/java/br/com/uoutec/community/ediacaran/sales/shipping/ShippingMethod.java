package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.List;

public interface ShippingMethod {

	public static final String WEIGHT_PROPERTY	= "weight";
	
	public static final String HEIGHT_PROPERTY	= "height";
	
	public static final String WIDTH_PROPERTY	= "width";

	public static final String DEPTH_PROPERTY	= "depth";
	
	String getId();
	
	String getName();
	
	boolean isApplicable(ShippingRateRequest request);
	
	List<ShippingOption> getOptions(ShippingRateRequest request);
	
}
