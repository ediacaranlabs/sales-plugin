package br.com.uoutec.community.ediacaran.sales.entity;

import java.math.BigDecimal;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.sales.PluginInstaller;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum TaxType {

	UNIT{
		
		public BigDecimal apply(BigDecimal value, BigDecimal tax){
			return tax == null?
					BigDecimal.ZERO : 
					tax;
		}
		
	},
	
	PERCENTAGE{
		
		public BigDecimal apply(BigDecimal value, BigDecimal tax){
			if(tax == null) {
				return BigDecimal.ZERO;
			}
			
			return value.multiply(tax.divide(PluginInstaller.ONE_HUMDRED, BigDecimal.ROUND_DOWN));
		}
		
	};
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(TaxType.class);
	
	
	public String getName(Locale locale) {
		I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
		return i18nRegistry
					.getString(
							RESOURCE_BUNDLE,
							name().toLowerCase(), 
							locale
					);		
	}
	
	public BigDecimal apply(BigDecimal value, BigDecimal discount){
		return value;
	}
	
}
