package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class ProductViewerRegistryRepository implements PublicBean{

	private ConcurrentMap<String, ProductWidget> widgetMap;
	
	public ProductViewerRegistryRepository() {
		this.widgetMap = new ConcurrentHashMap<>();
	}
	
	public void registerProductViewerWidget(ProductWidget widget) throws ProductViewerRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCTVIEWER_REGISTRY.WIDGET.getRegisterPermission());
		
		ProductWidget old = widgetMap.putIfAbsent(widget.getId(), widget);
		if(old != null) {
			throw new ProductViewerRegistryException("handler already registered: " + widget.getId());
		}
	}
	
	public void unregisterProductViewerWidget(String id) throws ProductViewerRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCTVIEWER_REGISTRY.WIDGET.getRemovePermission());
		
		widgetMap.remove(id);
	}

	public List<ProductWidget> getProductViewerWidgets(){
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCTVIEWER_REGISTRY.WIDGET.getListPermission());
		
		return new ArrayList<>(widgetMap.values());
	}
	
}
