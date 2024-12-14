package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.community.ediacaran.user.pub.entity.CountryPubEntity;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.pub.entity.AbstractPubEntity;
import br.com.uoutec.pub.entity.DataValidation;
import br.com.uoutec.pub.entity.IdValidation;

public class AddressPubEntity extends AbstractPubEntity<Address>{

	private static final long serialVersionUID = 1391868122764939558L;

	@Transient
	private Integer id;

	@NotNull(groups = IdValidation.class)
	private String protectedID;
	
	@NotNull(groups={DataValidation.class})
	private CountryPubEntity country;

	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.NAME_FORMAT, groups={DataValidation.class})
	private String firstName;

	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.NAME_FORMAT, groups={DataValidation.class})
	private String lastName;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.ADDRESS_FORMAT, groups={DataValidation.class})
	private String address;
	
	@Pattern(regexp=CommonValidation.ADDRESS_FORMAT, groups={DataValidation.class})
	private String complement;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.NAME_FORMAT, groups={DataValidation.class})
	private String city;
	
	@Pattern(regexp=CommonValidation.NAME_FORMAT, groups={DataValidation.class})
	private String region;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.ZIP, groups={DataValidation.class})
	private String zip;
	
	private Boolean deleted;
	
	@Constructor
	public AddressPubEntity(){
	}
	
	public AddressPubEntity(Address e, Locale locale){
		this.protectedID = e.getProtectedID();
		this.address = e.getAddress();
		this.city = e.getCity();
		this.complement = e.getComplement();
		
		this.country = e.getCountry() == null? null : new CountryPubEntity(e.getCountry(), locale);
		this.id = e.getId();
		this.protectedID = e.getId() == null || e.getId() <= 0? null : SecretUtil.toProtectedID(String.valueOf(e.getId()));
		this.region = e.getRegion();
		this.zip = e.getZip();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CountryPubEntity getCountry() {
		return country;
	}

	public void setCountry(CountryPubEntity country) {
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

	public String getProtectedID() {
		return protectedID;
	}

	public void setProtectedID(String protectedID) {
		this.protectedID = protectedID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	protected void preRebuild(Address instance, boolean reload, boolean override, boolean validate) {
		try {
			this.id = Integer.parseInt(SecretUtil.toID(this.protectedID));
		}
		catch(Throwable ex){
			this.id = 0;
		}
	}
	
	@Override
	protected boolean isEqualId(Address instance) throws Throwable {
		return instance.getId() <= 0? 
				this.id == null :
				this.id != null && instance.getId() == this.id;
	}

	@Override
	protected boolean hasId(Address instance) throws Throwable {
		return instance.getId() > 0;
	}

	@Override
	protected Address reloadEntity() throws Throwable {
		ClientRegistry registry = EntityContextPlugin.getEntity(ClientRegistry.class);
		return registry.getAddressByID(this.id);
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
		throw new ValidationException("id");
	}

	@Override
	protected Address createNewInstance() throws Throwable {
		return new Address();
	}

	@Override
	protected void copyTo(Address o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		SubjectProvider subjectProvider = EntityContextPlugin.getEntity(SubjectProvider.class); 
		Subject subject = subjectProvider.getSubject();
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.FIRST_NAME)) {
			o.setFirstName(this.firstName);
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.LAST_NAME)) {
			o.setLastName(this.lastName);
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.ADDRESS)) {
			o.setAddress(this.address);
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.CITY)) {
			o.setCity(this.city);
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.COMPLEMENT)) {
			o.setComplement(this.complement);
		}
		
		if(country != null && subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.COUNTRY)) {
			o.setCountry(country.rebuild(true, false, false));
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.REGION)) {
			o.setRegion(this.region);
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.ZIP)) {
			o.setZip(this.zip);
		}
		
	}
	
}
