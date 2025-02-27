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
		super(userBR);
		if(userBR instanceof SystemUserBR) {
			SystemUserBR user = (SystemUserBR)userBR;
			this.setDocument(user.getDocument());
		}
	}
	
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public boolean isComplete() {
		return super.isComplete() && DocumentUtil.isValidCPF_CNPJ(document == null? null : document );
	}
	
}
