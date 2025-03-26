package br.com.uoutec.community.ediacaran.sales.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum ProductAttributeValueType {

	TEXT(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof String)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object parse(String value, Locale locale) {
			return value;
		}

		public String toString(Object value, Locale locale) {
			return (String) value;
		}
		
	},
	INTEGER(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof Integer)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object parse(String value, Locale locale) {
			return Integer.parseInt(value);
		}

		public String toString(Object value, Locale locale) {
			return String.valueOf((Integer)value);
		}
		
	},
	DECIMAL(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof Double)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object parse(String value, Locale locale) {
			return Double.parseDouble(value);
		}

		public String toString(Object value, Locale locale) {
			return String.valueOf((Double)value);
		}
		
	},
	DATE(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof LocalDate)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object parse(String value, Locale locale) {
			/*
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale).withZone(ZoneId.systemDefault());
			return formatter.parse(value);
			*/
			return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
		}

		public String toString(Object value, Locale locale) {
			/*
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.format((LocalDate)value);
			*/
			return DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate)value);
		}
		
	},
	DATE_TIME(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof LocalDateTime)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object parse(String value, Locale locale) {
			/*
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.parse(value);
			*/
			return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}

		public String toString(Object value, Locale locale) {
			/*
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.format((LocalDate)value);
			*/
			return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDate)value);
		}
		
	},
	TIME(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof LocalTime)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object parse(String value, Locale locale) {
			/*
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale == null? Locale.getDefault() : locale);
			return formatter.parse(value);
			*/
			return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);
		}

		public String toString(Object value, Locale locale) {
			/*
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale == null? Locale.getDefault() : locale);
			return formatter.format((LocalDate)value);
			*/
			return DateTimeFormatter.ISO_LOCAL_TIME.format((LocalDate)value);
		}
		
	};

	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(ProductAttributeValueType.class);

	public void checkType(Object value) {
	}
	
	public Object parse(String value, Locale locale) {
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
