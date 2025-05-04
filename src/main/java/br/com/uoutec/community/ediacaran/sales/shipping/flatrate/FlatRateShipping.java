package br.com.uoutec.community.ediacaran.sales.shipping.flatrate;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=Shipping.class, name="mock")
public class FlatRateShipping extends Shipping{

	private static final long serialVersionUID = -8406256945093259558L;

	private String trackerID;

	public FlatRateShipping() {
	}
	
	public FlatRateShipping(Shipping e) {
		super.setAddData(e.getAddData());
		super.setCancelDate(e.getCancelDate());
		super.setCancelJustification(e.getCancelJustification());
		super.setDate(e.getDate());
		super.setDepth(e.getDepth());
		super.setDest(e.getDest());
		super.setHeight(e.getHeight());
		super.setId(e.getId());
		super.setOrder(e.getOrder());
		super.setOrigin(e.getOrigin());
		super.setProducts(e.getProducts());
		super.setShippingType(e.getShippingType());
		super.setWeight(e.getWeight());
		super.setWidth(e.getWeight());
		super.setClient(e.getClient());
	}
	
	public String getTrackerID() {
		return trackerID;
	}

	public void setTrackerID(String trackerID) {
		this.trackerID = trackerID;
	}
	
}
