package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.pub.ProductViewerHandler;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

@Singleton
public class ProductViewerRegistryRepository implements PublicBean{

	private ConcurrentMap<String, ProductViewerHandler> map;
	
	public ProductViewerRegistryRepository() {
		this.map = new ConcurrentHashMap<>();
	}
	
	public void registerProductViewerHandler(ProductViewerHandler handler) throws ProductViewerRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCTVIEWER_REGISTRY.getRegisterPermission(handler.getID()));
		
		ProductViewerHandler old = map.putIfAbsent(handler.getID(), handler);
		if(old != null) {
			throw new ProductViewerRegistryException("handler already registered: " + handler.getID());
		}
	}
	
	public void unregisterProductViewerHandler(String id) throws ProductViewerRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCTVIEWER_REGISTRY.getRemovePermission(id));
		
		map.remove(id);
	}

	public ProductViewerHandler getProductViewerHandler(String id) {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCTVIEWER_REGISTRY.getGetPermission());
		
		return map.get(id);
	}
	
	public List<ProductViewerHandler> getProductViewerHandlers(){
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCTVIEWER_REGISTRY.getListPermission());
		
		return new ArrayList<>(map.values());
	}
	
}
