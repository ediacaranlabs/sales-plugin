package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.ResultAction;
import org.brandao.brutos.annotation.AcceptRequestType;
import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.ResponseType;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.MediaTypes;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.RequestMethodTypes;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.ClientEntityTypes;
import br.com.uoutec.community.ediacaran.sales.ProductTypeViewHandler;
import br.com.uoutec.community.ediacaran.sales.entity.AdminCart;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.sales.services.CartService;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.error.ErrorMappingProvider;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.admin_context}/cart/products", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductsCartAdminPubResource {

	@Transient
	@Inject
	private ErrorMappingProvider errorMappingProvider;
	
	@Transient
	@Inject
	private VarParser varParser;
	
	@Transient
	@Inject
	private AdminCart adminCart;
	
	@Transient
	@Inject
	private CartService cartService;

	@Transient
	@Inject
	private ClientEntityTypes clientEntityTypes;

	@Transient
	@Inject
	private CountryRegistry countryRegistry;

	@Transient
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Transient
	@Inject
	private ProductTypeRegistry productTypeRegistry;

	@Transient
	@Inject
	private SubjectProvider subjectProvider;
	
	public ProductsCartAdminPubResource(){
	}
	
	@Action("/")
	@View("${plugins.ediacaran.sales.template}/admin/cart/products")
	public void index() {
	}
	
	@Action("/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	public ProductsSearchResultPubEntity searchProduct(
			@DetachedName
			ProductSearchPubEntity productSearch,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException{
		
		ProductSearch search = null;
		try{
			search = productSearch.rebuild(false, true, true);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ProductsCartAdminPubResource.class, "searchProduct", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		ProductSearchResult result;
		try{
			result = cartService.search(search);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ProductsCartAdminPubResource.class, "searchProduct", "load", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		return new ProductsSearchResultPubEntity(result, locale);
	}
	
	@Action("/{protectedID}")
	@RequestMethod(RequestMethodTypes.GET)
	@ResponseErrors(rendered=false, name="exception")
	public ResultAction productForm(
			@DetachedName
			ProductPubEntity productPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException{

		Product product;
		ProductTypeViewHandler productTypeViewHandler;
		try{
			product = productPubEntity.rebuild(true, false, false);
			
			ProductType productType = productTypeRegistry.getProductType(product.getProductType());
			productTypeViewHandler = productType.getViewHandler();
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ProductsCartAdminPubResource.class, "productForm", "loadData", locale, ex);
			throw new InvalidRequestException(error, ex);
		}

		ResultAction ra;
		
		try{
			ra = productTypeViewHandler.getProductFormView(product, locale);
			ra.add("product", product);
		}
		catch(Throwable ex){
			String error = this.errorMappingProvider.getError(ProductsCartAdminPubResource.class, "productForm", "view", locale, ex);
			throw new InvalidRequestException(error, ex);
		}
		
		return ra;
		
	}
	
	public Cart getCart() {
		return adminCart.getCart();
	}
	
}
