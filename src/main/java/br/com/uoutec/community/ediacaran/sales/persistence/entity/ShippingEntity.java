package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.shipping.ProductPackage;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;
import br.com.uoutec.ediacaran.core.plugins.PublicType;

@Entity
@Table(name="rw_shipping")
@EntityListeners(AddressEntityListener.class)
public class ShippingEntity implements PublicType, Serializable{

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@Column(name="cod_shipping", length=32)
	private String id;
	
	@Column(name="cod_shipping")
	private String shippingType;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cod_origin_address", referencedColumnName = "cod_address" )
	private AddressEntity origin;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cod_dest_address", referencedColumnName = "cod_address" )
	private AddressEntity dest;

	@OneToMany(mappedBy = "shipping")
	private List<ProductPackageEntity> itens;
	
	@Lob
	private String addData;

	public ShippingEntity(){
	}
	
	public ShippingEntity(Shipping e, Client client){
		this.dest = e.getDest() == null? null : new AddressEntity(e.getDest(), null);
		this.id = e.getId();
		this.origin = e.getOrigin() == null? null : new AddressEntity(e.getOrigin(), null);
		this.shippingType = e.getShippingType();
		this.addData = DataUtil.encode(e.getAddData());
		
		if(e.getItens() != null) {
			this.itens = new ArrayList<>();
			for(ProductPackage p: e.getItens()) {
				this.itens.add(new ProductPackageEntity(p));
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public AddressEntity getOrigin() {
		return origin;
	}

	public void setOrigin(AddressEntity origin) {
		this.origin = origin;
	}

	public AddressEntity getDest() {
		return dest;
	}

	public void setDest(AddressEntity dest) {
		this.dest = dest;
	}

	public List<ProductPackageEntity> getItens() {
		return itens;
	}

	public void setItens(List<ProductPackageEntity> itens) {
		this.itens = itens;
	}

	public String getAddData() {
		return addData;
	}

	public void setAddData(String addData) {
		this.addData = addData;
	}

	public Shipping toEntity(){
		return this.toEntity(null);
	}
	
	public Shipping toEntity(Shipping e){
		
		try{
			
			if(e == null) {
				e =  new Shipping();
			}
			
			e.setAddData(this.addData == null? null : DataUtil.decode(this.addData));
			e.setDest(this.dest == null? null : this.dest.toEntity());
			e.setId(this.id);
			e.setOrigin(this.origin == null? null : origin.toEntity());
			e.setShippingType(this.shippingType);

			if(this.itens != null) {
				ArrayList<ProductPackage> list = new ArrayList<>();
				for(ProductPackageEntity p: this.itens) {
					list.add(p.toEntity());
				}
			}
			return e;
		}
		catch(Throwable ex){
			throw new RuntimeException(ex);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShippingEntity other = (ShippingEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
