package br.com.uoutec.community.ediacaran.sales;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.io.Path;
import br.com.uoutec.application.io.Vfs;
import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.PluginType;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.ediacaran.web.tomcat.TomcatUtils;

@Singleton
public class ClientEntityTypes implements PublicBean{

	@Inject
	private EntityInheritanceManager entityInheritanceUtil;

	@Inject
	private VarParser varParser;

	@Inject
	private PluginType pluginType;

	public String getClientEntityView(SystemUser systemUser){
		return ContextSystemSecurityCheck.doPrivileged(()->{
			return getClientEntityView0(systemUser);
		});
	}
	
	private String getClientEntityView0(SystemUser systemUser){
		
		String context = varParser.getValue("${plugins.ediacaran.sales.web_path}");
		String basePublicPath = getAdminPublicPath();
		Country country = systemUser.getCountry();
		
		if(containsLocaleByCountry(country)){
			
			String publicPath =
					basePublicPath +
					"/user-data-" + country.getIsoAlpha2().toLowerCase() + ".jsp";
			
			String realPath = getRealPath(publicPath);
			
			Path path = Vfs.getPath(realPath);
			
			if(path.exists() && path.isFile()) {
				return context + ":" + publicPath;
			}
			
		}
		
		return context + ":" + basePublicPath + "/user-data.jsp";
	}

	
	private boolean containsLocaleByCountry(Country country) {
		return country != null &&
				this.entityInheritanceUtil.getType(SystemUser.class, country.getIsoAlpha3()) != null;
	}

	private String getAdminPublicPath() {
		return varParser.getValue(
				"${plugins.ediacaran.sales.template}${plugins.ediacaran.front.admin_context}"
		);
	}

	private String getRealPath(String path) {
		return TomcatUtils.getPublicPath(pluginType.getConfiguration().getMetadata()) + path;
	}
	
}
