package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Locale;

import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum ProductVisibility {

	SHOW,
	
	HIDE;
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(ProductVisibility.class);
	
	public String getName(Locale locale) {
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		return i18nRegistry
					.getString(
							RESOURCE_BUNDLE,
							name().toLowerCase(), 
							locale
					);		
	}
	
}
