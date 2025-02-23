package br.com.uoutec.community.ediacaran.sales.entity;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;
import br.com.uoutec.i18n.MessageBundleThread;

public enum ProductAttributeValueType {

	INTEGER(){

		public Object toObject(String value, Locale locale) {
			return Long.parseLong(value);
		}

		public String toString(Object value, Locale locale) {
			return String.valueOf(value);
		}
		
	},
	DECIMAL(){

		public Object toObject(String value, Locale locale) {
			return Double.parseDouble(value);
		}

		public String toString(Object value, Locale locale) {
			return String.valueOf(value);
		}
		
	},
	DATE(){

		public Object toObject(String value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.parse(value);
		}

		public String toString(Object value, Locale locale) {
			return String.valueOf(value);
		}
		
	},
	DATE_TIME(){

		public Object toObject(String value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.parse(value);
		}

		public String toString(Object value, Locale locale) {
			return String.valueOf(value);
		}
		
	},
	TIME(){

		public Object toObject(String value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.parse(value);
		}

		public String toString(Object value, Locale locale) {
			return String.valueOf(value);
		}
		
	},
	
	;

	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(ProductAttributeType.class);

	public Object toObject(String value, Locale locale) {
		return null;
	}

	public String toString(Object value, Locale locale) {
		return null;
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
