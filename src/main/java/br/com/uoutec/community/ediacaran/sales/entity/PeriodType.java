package br.com.uoutec.community.ediacaran.sales.entity;

import java.util.Locale;

import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum PeriodType {

	HOUR(1),
	
	DAY(24),
	
	MONTH(720),
	
	YEAR(8760);
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(PeriodType.class);
	
	private int hour;
	
	private PeriodType(int hour){
		this.hour = hour;
	}
	
	public int toHours(){
		return this.hour;
	}

	public int toDays(){
		return this.hour/DAY.toHours();
	}

	public int toYear(){
		return this.hour/YEAR.toHours();
	}
	
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
