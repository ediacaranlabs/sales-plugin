package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.SalesPluginConstants;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ProductCategory;

public class ProductCategoryUtil {

	public static String getPublicThumbPath(ProductCategory e) {
		
		if(e == null || e.getId() <= 0) {
			return null;
		}
		
		return "/" + getThumbPath("c" + Integer.toString(e.getId(), Character.MAX_RADIX).toLowerCase());
	}
	
	public static String getThumbPath(ProductCategory e) {
		
		if(e == null || e.getId() <= 0) {
			return null;
		}
		
		return getThumbPath("c" + Integer.toString(e.getId(), Character.MAX_RADIX).toLowerCase());
	}
	
	private static String getThumbPath(String id) {
		List<String> partsList = new ArrayList<>();
		
		int maxlen = id.length() / 3;
		maxlen = maxlen == 0? id.length() : maxlen;
		
		char[] chars = id.toCharArray();
		int i = 0;
		
		while(i<chars.length) {
			
			int from = i;
			int to = i + maxlen;
			
			if(to > chars.length) {
				to = chars.length - 1;
			}
			
			char[] tmp = Arrays.copyOfRange(chars, from, to);
			partsList.add(new String(tmp));
			i += maxlen;
			
		}
		
		String path = String.join("/", partsList);
		
		return SalesPluginConstants.CATEGORY_DRIVER_NAME + SalesPluginConstants.IMAGES_CATEGORY_DRIVER_PATH + "/" + path;
	}
	
}
