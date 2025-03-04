package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.InvalidRequestException;

public class DefaultProductViewer implements ProductViewer{

	@Override
	public WebResultAction showProductSearch(Locale locale) throws InvalidRequestException {
		WebResultAction ra = new WebResultActionImp();
		ra.setView("${plugins.ediacaran.sales.template}/front/product/search");
		return ra;
	}

	@Override
	public ProductsSimplifiedSearchResultPubEntity searchProduct(ProductSearchPubEntity productSearch, Locale locale)
			throws InvalidRequestException {
		
		ProductRegistry productRegistry = EntityContextPlugin.getEntity(ProductRegistry.class);
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		
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

	@Override
	public WebResultAction showProduct(ProductPubEntity productPubEntity, Locale locale) throws InvalidRequestException {
		
		ProductViewerRegistry productViewerRegistry = EntityContextPlugin.getEntity(ProductViewerRegistry.class);
		ProductRegistry productRegistry = EntityContextPlugin.getEntity(ProductRegistry.class);
		
		Product product = null;
		List<ProductImage> images = null;
		List<ProductWidgetWrapper> widgets = null;
		try{
			product = productPubEntity.rebuild(true, false, true);
			images = productRegistry.getImagesByProduct(product);
			
			List<ProductWidget> productWidgets = productViewerRegistry.getProductViewerWidgets();
			widgets = productWidgets.stream().map((e)->new ProductWidgetWrapper(e)).collect(Collectors.toList());
			
		}
		catch(Throwable ex){
			ex.printStackTrace();
			WebResultAction ra = new WebResultActionImp();
			ra.setResponseStatus(HttpStatus.NOT_FOUND);
			ra.setReason("product not found");
			return ra;
		}

		WebResultAction ra = new WebResultActionImp();
		ra.setView("${plugins.ediacaran.sales.template}/front/product/product_details");
		ra.add("entity", product);
		ra.add("images", images);
		ra.add("widgets", widgets);
		return ra;

	}

}
