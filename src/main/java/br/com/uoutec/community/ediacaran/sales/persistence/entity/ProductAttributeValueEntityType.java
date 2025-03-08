package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Locale;

import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.i18n.MessageBundle;

public enum ProductAttributeValueEntityType {

	TEXT(){

		public Object toValue(Object value) {
			return value;
		}

		public Object parse(Object value) {
			return value;
		}
		
	},
	INTEGER(){

		public Object toValue(Object value) {
			return ((Long)value).intValue();
		}

		public Object parse(Object value) {
			return ((Integer)value).longValue();
		}
		
	},
	DECIMAL(){

		public Object toValue(Object value) {
			return Double.longBitsToDouble((Long)value);
		}

		public Object parse(Object value) {
			return Double.doubleToRawLongBits(((Double)value));
		}
		
	},
	DATE(){

		public Object toValue(Object value) {
			return LocalDateTime.ofEpochSecond((Long)value, 0, ZoneOffset.UTC).toLocalDate();
		}

		public Object parse(Object value) {
			return ((LocalDate)value).atTime(0,0).toEpochSecond(ZoneOffset.UTC);
		}
		
	},
	DATE_TIME(){

		public Object toValue(Object value) {
			return LocalDateTime.ofEpochSecond((Long)value, 0, ZoneOffset.UTC);
		}

		public Object parse(Object value) {
			return ((LocalDateTime)value).toEpochSecond(ZoneOffset.UTC); 		
		}
		
	},
	TIME(){

		public Object toValue(Object value) {
			return LocalDateTime.ofEpochSecond((Long)value, 0, ZoneOffset.UTC).toLocalTime();
		}

		public Object parse(Object value) {
			return ((LocalTime)value).atDate(LocalDate.of(2000, Month.JANUARY, 1)).toEpochSecond(ZoneOffset.UTC);
		}
		
	};

	public static final String RESOURCE_BUNDLE = 
			MessageBundle.toPackageID(ProductAttributeValueEntityType.class);

	public Object toValue(Object value) {
		return null;
	}

	public Object parse(Object value) {
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
