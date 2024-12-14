package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.entity.CountryHibernateEntity;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.ediacaran.core.plugins.PublicType;

@Entity
@Table(name="rw_client_address")
@EntityListeners(AddressEntityListener.class)
public class AddressEntity implements PublicType, Serializable{

	private static transient final long serialVersionUID = -5167928569154696530L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_system_user_addr", length=11)
	private Integer id;
	
	@Column(name="cod_system_user", length=11)
	private Integer systemUserID;
	
	@Column(name="dsc_first_name", length=68)
	private String firstName;
	
	@Column(name="dsc_last_name", length=68)
	private String lastName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cod_country")
	private CountryHibernateEntity country;

	@Column(name="set_type", length=32)
	private String type;
	
	@Column(name="dsc_address", length=128)
	private String address;

	@Column(name="dsc_complement", length=68)
	private String complement;

	@Column(name="dsc_city", length=68)
	private String city;

	@Column(name="dsc_region", length=68)
	private String region;
	
	@Column(name="dsc_zip", length=68)
	private String zip;

	public AddressEntity(){
	}
	
	public AddressEntity(Address e){
		this.firstName      = e.getFirstName();
		this.lastName       = e.getLastName();
		this.type           = e.getType();
		this.systemUserID   = e.getOwner();
		this.address        = e.getAddress();
		this.city           = e.getCity();
		this.complement     = e.getComplement();
		this.country        = e.getCountry() == null? null : new CountryHibernateEntity(e.getCountry());
		this.region         = e.getRegion();
		this.zip            = e.getZip();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSystemUserID() {
		return systemUserID;
	}

	public void setSystemUserID(Integer systemUserID) {
		this.systemUserID = systemUserID;
	}

	public CountryHibernateEntity getCountry() {
		return country;
	}

	public void setCountry(CountryHibernateEntity country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Address toEntity(){
		return this.toEntity(null);
	}
	
	public Address toEntity(Address e){
		
		try{
			
			if(e == null) {
				e = new Address();
			}
			
			e.setFirstName(this.firstName);
			e.setLastName(this.lastName);
			e.setAddress(this.address);
			e.setCity(this.city);
			e.setComplement(this.complement);
			e.setCountry(this.country == null? null : this.country.toEntity());
			e.setRegion(this.region);
			e.setZip(this.zip);
			e.setId(this.id);
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
		AddressEntity other = (AddressEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
