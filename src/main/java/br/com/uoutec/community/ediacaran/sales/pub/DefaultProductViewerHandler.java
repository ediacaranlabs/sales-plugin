package br.com.uoutec.community.ediacaran.sales.pub;

public class DefaultProductViewerHandler implements ProductViewerHandler{

	private ProductViewer productViewer;
	
	private ProductAdminViewer productAdminViewer;
	
	public DefaultProductViewerHandler() {
		this.productAdminViewer = new DefaultProductAdminViewer();
		this.productViewer = null;
	}
	
	@Override
	public String getID() {
		return "default";
	}

	@Override
	public ProductViewer getProductViewer() {
		return productViewer;
	}

	@Override
	public ProductAdminViewer getProductAdminViewer() {
		return productAdminViewer;
	}

}
