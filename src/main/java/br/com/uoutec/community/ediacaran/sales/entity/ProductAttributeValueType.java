package br.com.uoutec.community.ediacaran.sales.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;
import br.com.uoutec.i18n.MessageBundleThread;

public enum ProductAttributeValueType {

	TEXT(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof String)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object toValue(Object value) {
			return value;
		}

		public Object toPersistence(Object value) {
			return value;
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
		
		public Object toValue(Object value) {
			return ((Long)value).intValue();
		}

		public Object toPersistence(Object value) {
			return ((Integer)value).longValue();
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
		
		public Object toValue(Object value) {
			return Double.longBitsToDouble((Long)value);
		}

		public Object toPersistence(Object value) {
			return Double.doubleToRawLongBits(((Double)value));
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
		
		public Object toValue(Object value) {
			return LocalDateTime.ofEpochSecond((Long)value, 0, ZoneOffset.UTC).toLocalDate();
		}

		public Object toPersistence(Object value) {
			return ((LocalDate)value).atTime(0,0).toEpochSecond(ZoneOffset.UTC);
		}

		public Object parse(String value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.parse(value);
		}

		public String toString(Object value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.format((LocalDate)value);
		}
		
	},
	DATE_TIME(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof LocalDateTime)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object toValue(Object value) {
			return LocalDateTime.ofEpochSecond((Long)value, 0, ZoneOffset.UTC);
		}

		public Object toPersistence(Object value) {
			return ((LocalDateTime)value).toEpochSecond(ZoneOffset.UTC); 		
		}

		public Object parse(String value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.parse(value);
		}

		public String toString(Object value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.format((LocalDate)value);
		}
		
	},
	TIME(){

		public void checkType(Object value) {
			if(value != null && !(value instanceof LocalTime)) {
				throw new IllegalStateException(String.valueOf(value));
			}
		}
		
		public Object toValue(Object value) {
			return LocalDateTime.ofEpochSecond((Long)value, 0, ZoneOffset.UTC).toLocalTime();
		}

		public Object toPersistence(Object value) {
			return ((LocalTime)value).atDate(LocalDate.of(2000, Month.JANUARY, 1)).toEpochSecond(ZoneOffset.UTC);
		}

		public Object parse(String value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.parse(value);
		}

		public String toString(Object value, Locale locale) {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale == null? MessageBundleThread.getLocale() : locale);
			return formatter.format((LocalDate)value);
		}
		
	};

	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(ProductAttributeValueType.class);

	public void checkType(Object value) {
	}
	
	public Object toValue(Object value) {
		return null;
	}

	public Object toPersistence(Object value) {
		return null;
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
