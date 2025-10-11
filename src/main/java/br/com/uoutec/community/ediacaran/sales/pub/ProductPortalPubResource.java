package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.ActionStrategy;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.annotation.web.WebActionStrategyType;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.sales.service.ProductProtalService;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;

@Singleton
@Controller
@ActionStrategy(WebActionStrategyType.DETACHED)
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductPortalPubResource {

	@Transient
	@Inject
	public ProductProtalService productProtalService;
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/front/product/portal")
	@Result("vars")
	public Map<String,Object> index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
			) {
		
		Map<String,Object> vars = new HashMap<>();
		vars.put("categories", productProtalService.getCategories());
		vars.put("offers", productProtalService.getOffers());
		vars.put("products", productProtalService.getProducts());

		return vars;
	}
	
}
