package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

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
import org.brandao.brutos.annotation.web.MediaTypes;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.ResponseErrors;
import org.brandao.brutos.web.HttpStatus;
import org.brandao.brutos.web.WebResultAction;
import org.brandao.brutos.web.WebResultActionImp;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSimplifiedSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductViewerRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductWidget;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="/products", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductPubResource {

	@Transient
	@Inject
	public ProductRegistry productRegistry;
	
	@Transient
	@Inject
	public I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private Cart cart;
	
	@Transient
	@Inject
	private ProductViewerRegistry productViewerRegistry;
	
	@Action({"/","/category/{category}"})
	@RequestMethod({"POST", "GET"})
	public WebResultAction index(
			@DetachedName
			ProductSearchPubEntity productSearch,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
			) {
		
		ProductSearch search = null;

		try {
			
			if(productSearch == null) {
				productSearch = new ProductSearchPubEntity();
			}
			
			search = productSearch.rebuild(false, true, false);
		}
		catch(Throwable ex) {
			search = new ProductSearch();
		}
		
		try {
			WebResultAction ra = new WebResultActionImp();
			ra.setView("${plugins.ediacaran.sales.template}/front/product/search");
			ra.add("productSearch", search);
			return ra;
		}
		catch(Throwable ex) {
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setContentType(String.class);
			ra.setReason("product viewer misconfiguration");
			return ra;
		}

	}
	
	@Action("/search")
	@RequestMethod("POST")
	@AcceptRequestType(MediaTypes.APPLICATION_JSON)
	@ResponseType(MediaTypes.APPLICATION_JSON)
	@Result(mappingType = MappingTypes.OBJECT)
	public ProductsSimplifiedSearchResultPubEntity searchProduct(
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
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_load_request, 
							locale);
			throw new InvalidRequestException(error, ex);
		}

		ProductSearchResult result;
		
		try{
			result = productRegistry.search(search);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							ProductAdminPubResourceMessages.RESOURCE_BUNDLE,
							ProductAdminPubResourceMessages.search_product.error.fail_search, 
							locale);
			throw new InvalidRequestException(error, ex);
		}
		
		return new ProductsSimplifiedSearchResultPubEntity(result, locale);		
		
	}
	
	@Action({
		"/{name:[^/\\s//\\.]+}/{product.protectedID:[^/\\s//\\.]+}/",
		"/{name:[^/\\s//\\.]+}/{product.protectedID:[^/\\s//\\.]+}"
	})
	public WebResultAction showProduct(
			@Basic(bean = "product")
			ProductPubEntity productPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale) throws InvalidRequestException {
		
		
		try{
			Product product = null;
			List<ProductImage> images = null;
			List<ProductWidgetWrapper> widgets = null;
			
			product = productPubEntity.rebuild(true, false, true);
			images = productRegistry.getImagesByProduct(product);
			
			List<ProductWidget> productWidgets = productViewerRegistry.getProductViewerWidgets();
			widgets = productWidgets.stream().map((e)->new ProductWidgetWrapper(e)).collect(Collectors.toList());
			
			WebResultAction ra = new WebResultActionImp();
			ra.setView("${plugins.ediacaran.sales.template}/front/product/product_details");
			ra.add("entity", product);
			ra.add("images", images);
			ra.add("widgets", widgets);
			return ra;
		}
		catch(Throwable ex) {
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setReason("product viewer misconfiguration");
			return ra;
		}
		
	}

	public Cart getCart() {
		return cart;
	}
	
}
