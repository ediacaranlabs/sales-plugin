package br.com.uoutec.community.ediacaran.sales.shipping.flatrate;

import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingPubEntity;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=ShippingPubEntity.class, name="flatrate")
public class FlatRateShippingPubEntity extends ShippingPubEntity{

	private static final long serialVersionUID = -4771222331581275998L;
	
	private String trackerID;

	public String getTrackerID() {
		return trackerID;
	}

	public void setTrackerID(String trackerID) {
		this.trackerID = trackerID;
	}
	
	@Override
	protected void copyTo(Shipping o, boolean reload, boolean override,
			boolean validate) throws Throwable {

		super.copyTo(o, reload, override, validate);
		
		if(!(o instanceof FlatRateShipping)) {
			return;
		}
		
		FlatRateShipping e = (FlatRateShipping)o;

		e.setTrackerID(trackerID);
	}

	@Override
	protected void loadProperties(GenericPubEntity<Shipping> e) {
		super.loadProperties(e);
		
		if(super.getData() != null && super.getData().get("trackerID") != null) {
			if(this.trackerID == null) {
				this.trackerID = String.valueOf(super.getData().get("trackerID"));
			}
		}
	}
	
	@Override
	protected Shipping createNewInstance() throws Throwable {
		Shipping e = super.createNewInstance();
		return e == null? null : new FlatRateShipping(e);
	}
	
}
