package br.com.uoutec.community.ediacaran.sales;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.community.ediacaran.sales.registry.ProductMetadataRegistry;
import br.com.uoutec.ediacaran.core.plugins.PluginOptionsResolver;
import br.com.uoutec.ediacaran.core.plugins.PluginPropertyOption;

public class DefaultMetadataPluginOptionsResolver 
implements PluginOptionsResolver {

	@Inject
	private ProductMetadataRegistry productMetadataRegistry;
	
	@Override
	public List<PluginPropertyOption> getOptions() {
		
		List<PluginPropertyOption> result = new ArrayList<>();
		try {
			List<ProductMetadata> list = ContextSystemSecurityCheck.doPrivileged(()->{
				return productMetadataRegistry.getAllProductMetadata();
			});
			for(ProductMetadata r: list) {
				result.add(new PluginPropertyOption(String.valueOf(r.getId()), r.getName()));
			}
			
			return result;
		}
		catch(Throwable ex) {
			result.add(new PluginPropertyOption("", "Error: " + ex.getMessage()));
		}
		
		return result;
	}

}
