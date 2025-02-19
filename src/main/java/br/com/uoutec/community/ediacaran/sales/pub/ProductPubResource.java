package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.Locale;

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

import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductSearchPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductsSimplifiedSearchResultPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.ProductViewerRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="/products", defaultActionName="/")
@ResponseErrors(code=HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductPubResource {

	@Transient
	@Inject
	private ProductViewerRegistry productViewerRegistry;
	
	@Action("/")
	public WebResultAction index(
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
			) {
		
		try {
			ProductViewerHandler handler = productViewerRegistry.getProductViewerHandler();
			WebResultAction ra = handler.getProductViewer().showProductSearch(locale);
			
			if(ra == null) {
				throw new NullPointerException("ResultAction");
			}
			
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
		
		try {
			ProductViewerHandler handler = productViewerRegistry.getProductViewerHandler();
			return handler.getProductViewer().searchProduct(productSearch, locale);
		}
		catch(Throwable ex) {
			return null;
		}
		
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
		
		try {
			ProductViewerHandler handler = productViewerRegistry.getProductViewerHandler();
			WebResultAction ra = handler.getProductViewer().showProduct(productPubEntity, locale);
			
			if(ra == null) {
				throw new NullPointerException("WebResultAction");
			}
			
			return ra;
		}
		catch(Throwable ex) {
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			ra.setReason("product viewer misconfiguration");
			return ra;
		}
		
	}

}
