package br.com.uoutec.community.ediacaran.sales;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CurrencyUtil {

	private static final ConcurrentMap<String, Locale> locales;
	
	static {
		locales = new ConcurrentHashMap<>();
		locales.put("BRL", new Locale("pt", "BR"));
		locales.put("USD", new Locale("en", "US"));
	}
	
	public static String getSymbol(String currency) {
		Locale locale = currency == null? null : locales.get(currency);
		
		if(locale != null) {
			return Currency.getInstance(locale).getSymbol();
		}
		
		return currency;
	}
	
	public static String toString(String currency, BigDecimal value) {

		Locale locale = currency == null? null : locales.get(currency);
		
		if(locale != null) {
			return DecimalFormat.getCurrencyInstance(locale).format(value.setScale(2, BigDecimal.ROUND_UNNECESSARY));
		}
		
		return (currency == null? "" : currency + " ") + value.setScale(2, BigDecimal.ROUND_UNNECESSARY);
	}
	
}
