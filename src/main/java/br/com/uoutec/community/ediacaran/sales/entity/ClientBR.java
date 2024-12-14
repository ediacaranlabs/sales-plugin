package br.com.uoutec.community.ediacaran.sales.entity;

import javax.validation.constraints.Pattern;

import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;
import br.com.uoutec.community.ediacaran.system.util.DocumentUtil;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.entity.SystemUserBR;

@EntityInheritance(base=Client.class, name="BRA")
public class ClientBR extends Client{

	private static final long serialVersionUID = 2658609156376778575L;

	@Pattern(regexp=DocumentUtil.CPF_CNPJ_PATTERN)
	protected String document;

	public ClientBR() {
	}

	public ClientBR(SystemUser userBR) {
		SystemUserBR user = (SystemUserBR)userBR;
		this.activated = user.isActivated();
		this.addData = user.getAddData();
		this.address = user.getAddress();
		this.city = user.getCity();
		this.complement = user.getComplement();
		this.country = user.getCountry();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.id = user.getId();
		this.language = user.getLanguage();
		this.lastName = user.getLastName();
		this.organization = user.getOrganization();
		this.phone = user.getPhone();
		this.region = user.getRegion();
		this.template = user.getTemplate();
		this.zip = user.getZip();
		this.document = user.getDocument();
	}
	
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public boolean isComplete() {
		return super.isComplete() && DocumentUtil.isValidCPF_CNPJ(document);
	}
	
}
