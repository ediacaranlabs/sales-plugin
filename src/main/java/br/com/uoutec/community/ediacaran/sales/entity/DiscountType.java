package br.com.uoutec.community.ediacaran.sales.entity;

import java.math.BigDecimal;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.sales.PluginInstaller;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum DiscountType {

	UNIT{
		
		public BigDecimal apply(BigDecimal value, BigDecimal discount){
			
			if(discount != null && discount.compareTo(BigDecimal.ZERO) > 0){
				return 
						discount.compareTo(value) >= 0?
						BigDecimal.ZERO :
						value.subtract(discount);
			}
			
			return value;
		}
		
	},
	
	PERCENTAGE{
		
		public BigDecimal apply(BigDecimal value, BigDecimal discount){
			if(discount != null && 
					discount.compareTo(BigDecimal.ZERO) > 0 && 
					discount.compareTo(PluginInstaller.ONE_HUMDRED) <= 0){
				return 
					value.subtract(
						value.multiply(
							discount.divide(PluginInstaller.ONE_HUMDRED, BigDecimal.ROUND_DOWN)
						)
					);
			}
						
			return value;
		}
		
	};
	
	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(DiscountType.class);
	
	
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
