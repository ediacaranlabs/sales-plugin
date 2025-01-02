package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.ediacaran.core.plugins.PublicType;

@Entity
@Table(name="rw_shipping")
@EntityListeners(AddressEntityListener.class)
public class ShippingEntity implements PublicType, Serializable{

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_shipping", length=11)
	private Integer id;
	
	@Column(name="cod_shipping")
	private String shippingType;
	
	@OneToMany(mappedBy="shipping", fetch=FetchType.LAZY)
	private List<ProductRequestEntity> itens;

	public ShippingEntity(){
	}
	
	public ShippingEntity(Address e, Client client){
	}


	public Address toEntity(){
		return this.toEntity(null);
	}
	
	public Address toEntity(Address e){
		
		try{
			
			if(e == null) {
				e =  new Address();
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
