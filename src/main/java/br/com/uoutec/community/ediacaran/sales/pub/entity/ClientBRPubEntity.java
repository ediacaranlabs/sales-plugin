package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;
import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ClientBR;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;
import br.com.uoutec.community.ediacaran.system.util.DocumentUtil;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.entity.SystemUserBR;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessagesConstants;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;

@EntityInheritance(base=ClientPubEntity.class, name="BRA")
public class ClientBRPubEntity extends ClientPubEntity{

	private static final long serialVersionUID = 1465096757319557192L;

	public static final String EMPTY_DOCUMENT = "***********";
	
	@Pattern(regexp="(\\Q" + EMPTY_DOCUMENT + "\\E)|(" + DocumentUtil.CPF_CNPJ_PATTERN + ")")
	private String document;
	
	@Constructor
	public ClientBRPubEntity(){
	}
	
	public ClientBRPubEntity(SystemUser e, Locale locale){
		super(e, locale);
		
		if(e instanceof SystemUserBR) {
			SystemUserBR o = (SystemUserBR)e;
			if(o.getDocument() != null) {
				this.setDocument(EMPTY_DOCUMENT);
			}
		}
	}
	
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	protected void validate(boolean reload, boolean override, 
			Class<?> ... groups) throws ValidationException {
		
		super.validate(reload, override, groups);
		
		if(document != null) {
			if(!EMPTY_DOCUMENT.equals(document) && !DocumentUtil.isValidCPF_CNPJ(this.document)) {
				String error = ValidatorBean
						.getMessageResourceString(
								MessagesConstants.RESOURCE_BUNDLE,
								MessagesConstants.system.error.not_match_message,
								new Object[]{"document"},
								getLocale()
						);
				throw new ValidationException(error);
			}
		}
		
	}
	
	@Override
	protected void copyTo(SystemUser o, boolean reload, boolean override,
			boolean validate) throws Throwable {

		super.copyTo(o, reload, override, validate);
		
		if(!(o instanceof ClientBR)) {
			return;
		}
		
		ClientBR e = (ClientBR)o;

		SubjectProvider subjectProvider = EntityContextPlugin.getEntity(SubjectProvider.class); 
		Subject subject = subjectProvider.getSubject();
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.DOCUMENT)) {
			if(this.document != null && !EMPTY_DOCUMENT.equals(this.document)) {
				e.setDocument(this.document);
			}
		}
		
	}

	@Override
	protected void loadProperties(GenericPubEntity<SystemUser> e) {
		super.loadProperties(e);
		if(super.getData() != null && super.getData().get("document") != null) {
			if(this.document == null) {
				this.document = String.valueOf(super.getData().get("document"));
			}
		}
	}
	
	protected SystemUser reloadEntity() throws Throwable {
		ClientRegistry registry = EntityContextPlugin.getEntity(ClientRegistry.class);
		Client user = registry.findClientById(super.getId());
		
		if(user != null && !(user instanceof ClientBR)) {
			user = new ClientBR(user);
		}
		
		return user;
	}
	
	@Override
	protected SystemUser createNewInstance() throws Throwable {
		return new ClientBR();
	}
	
}
