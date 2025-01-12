package br.com.uoutec.community.ediacaran.sales.shipping;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingPubEntity;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=ShippingPubEntity.class, name="electronic")
public class ElectronicShippingPubEntity extends ShippingPubEntity{

	private static final long serialVersionUID = -4771222331581275998L;
	
	private String trackerID;

	public String getTrackerID() {
		return trackerID;
	}

	public void setTrackerID(String trackerID) {
		this.trackerID = trackerID;
	}
	
	@Override
	protected Shipping createNewInstance() throws Throwable {
		Shipping e = super.createNewInstance();
		return e == null? null : new ElectronicShipping(e);
	}
	
}
