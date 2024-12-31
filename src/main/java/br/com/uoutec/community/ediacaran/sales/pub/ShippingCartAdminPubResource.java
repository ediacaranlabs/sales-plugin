package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.sales.shipping.ProductPackage;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethod;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethodRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingOption;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingRateRequest;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/cart/shipping", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ShippingCartAdminPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private AdminCart adminCart;
	
	@Transient
	@Inject
	private CartService cartService;

	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	@Transient
	@Inject
	private ShippingMethodRegistry shippingMethodRegistry;

	@Transient
	@Inject
	private VarParser varParser;
	
	@Transient
	@Inject
	private CountryRegistry countryRegistry;
	
	public ShippingCartAdminPubResource(){
	}
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/cart/shipping")
	@RequestMethod(RequestMethodTypes.GET)
	@Result("vars")
	public Map<String,Object> showAddress(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {

		try{
			Address origin = new Address();
			origin.setAddressLine1(varParser.getValue("${plugins.ediacaran.system.address_line1_property}"));
			origin.setAddressLine2(varParser.getValue("${plugins.ediacaran.system.address_line2_property}"));
			origin.setCity(varParser.getValue("${plugins.ediacaran.system.city_property}"));
			
			String isoAlpha3 = varParser.getValue("${plugins.ediacaran.system.country_property}");
			origin.setCountry(countryRegistry.getCountryByIsoAlpha3(isoAlpha3));
			
			origin.setRegion(varParser.getValue("${plugins.ediacaran.system.region_property}"));
			origin.setZip(varParser.getValue("${plugins.ediacaran.system.zip_property}"));
			
			Address dest = adminCart.getShippingAddress();

			if(dest == null) {
				Client client = adminCart.getClient();
				Address address = new Address();
				
				address.setAddressLine1(client.getAddressLine1());
				address.setAddressLine2(client.getAddressLine2());
				address.setCity(client.getCity());
				address.setCountry(client.getCountry());
				address.setFirstName(client.getFirstName());
				address.setLastName(client.getLastName());
				address.setRegion(client.getRegion());
				address.setZip(client.getZip());
			}
			
			String currency = null;
			
			List<ProductPackage> packages = new ArrayList<>();
			
			for(ProductRequest pr: adminCart.getCart().getItens()) {
				Map<String, String> data = pr.getAddData();
				String weightSTR = data.get(ShippingMethod.WEIGHT_PROPERTY);
				String heightSTR = data.get(ShippingMethod.HEIGHT_PROPERTY);
				String widthSTR = data.get(ShippingMethod.WIDTH_PROPERTY);
				String depthSTR = data.get(ShippingMethod.DEPTH_PROPERTY);

				float weight = weightSTR == null? 0f : Float.parseFloat(weightSTR);
				float height = heightSTR == null? 0f : Float.parseFloat(heightSTR);
				float width = widthSTR == null? 0f : Float.parseFloat(widthSTR);
				float depth = depthSTR == null? 0f : Float.parseFloat(depthSTR);
				
				ProductPackage pp = new ProductPackage(weight, height, width, depth, Arrays.asList(pr));
				packages.add(pp);
			}
			
			ShippingRateRequest shippingRateRequest = new ShippingRateRequest(origin, dest, currency, packages);
			List<ShippingMethod> shippingMethods = shippingMethodRegistry.getShippingMethods(shippingRateRequest);
			List<ShippingOption> shippingOption = new ArrayList<>();
			
			for(ShippingMethod sm: shippingMethods) {
				shippingOption.addAll(sm.getOptions(shippingRateRequest));
			}
			Map<String,Object> vars = new HashMap<String, Object>();
			
			vars.put("shippingOptions", shippingOption);
			
			return vars;
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ShippingCartAdminPubResource.class, "showshipping", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
	}

	public Cart getCart() {
		return adminCart.getCart();
	}
	
}
