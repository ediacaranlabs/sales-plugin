package br.com.uoutec.community.ediacaran.sales.pub;

import org.brandao.brutos.ResultAction;
import org.brandao.brutos.web.WebMvcRequest;

import br.com.uoutec.community.ediacaran.front.SectionView;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.registry.ProductWidget;

public class ProductWidgetWrapper implements SectionView{

	private ProductWidget productWidget;
	
	public ProductWidgetWrapper(ProductWidget productWidget) {
		this.productWidget = productWidget;
	}

	@Override
	public ResultAction run(String methodName, Object... params) throws Throwable {
		return getView((WebMvcRequest)params[0]);
	}

	@Override
	public ResultAction getView(WebMvcRequest request) {
		Product product = (Product) request.getAttribute("entity");
		return productWidget.getContent(product);
	}

	public String getId() {
		return productWidget.getId();
	}
	
	public String getTitle() {
		return productWidget.getTitle();
	}
	
	public SectionView getContent() {
		return this;
	}
	
}
